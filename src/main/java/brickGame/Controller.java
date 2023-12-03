package brickGame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static String savePath    = "C:/save/save.mdds";
    private static String savePathDir = "C:/save/";
    private Model model;
    private View view;
    private GameEngine engine;
    Stage primaryStage;
    private boolean loadFromSave = false;
    private boolean leveldone = false;
    private int sceneHeigt = 700;
    private int destroyedBlockCount = 0;

    public Controller() {
        this.model = new Model();
        this.view = new View(this);
        this.engine = new GameEngine();
    }

    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            if (!loadFromSave) {
                model.inclevel();
                if (model.getLevel() > 1 && model.getLevel() < 18) {
                    view.showMessage("Level Up :)");
                    model.getSoundManager().playLevelUpSound();
                }
                if (model.getLevel() == 19) {
                    view.showWin();
                    model.getSoundManager().playYouWinSound();
                    return;
                }
                initBall();
                initBreak();
                model.initBoardModel();
                view.initstartmenubutton();
            }

            view.initroot();
            view.initscene(primaryStage);
            model.getSoundManager().playBackgroundMusic();
            primaryStage.getScene().setOnKeyPressed(this);

            if (!loadFromSave) {
                if (model.getLevel() > 1 && model.getLevel() < 19) {
                    if (model.getLevel() == 18) {
                        view.initfinallevel();
                    }
                    view.setLoadvisible(false);
                    view.setNewGamevisible(false);
                    initGameEngine();
                }
                handleloadnewgamebutton();
            } else {
                initGameEngine();
                loadFromSave = false;
            }
            addbombchoco();
            leveldone = false;
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Exception occurred in Controller.start", e);
        }
    }

    private void handleloadnewgamebutton() {
        view.getLoad().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame();
                view.setLoadvisible(false);
                view.setNewGamevisible(false);
            }
        });
        view.getNewGame().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                initGameEngine();
                view.setLoadvisible(false);
                view.setNewGamevisible(false);
            }
        });
    }

    private void addbombchoco() {
        for (Bonus choco : model.getChocos()) {
            if (!choco.isTaken()) {
                view.root.getChildren().add(choco.getChoco());
            }
        }

        for (Bomb bomb : model.getBombs()) {
            if (!bomb.isTaken()) {
                view.root.getChildren().add(bomb.getBomb());
            }
        }
    }

    private void initGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }

    private void initBall() {
        model.initBallPos();
        view.initBallView();
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                model.move(Model.getLEFT());
                break;
            case RIGHT:
                model.move(Model.getRIGHT());
                break;
            case S:
                saveGame();
                break;
            case SPACE:
                pauseresumeGame();
                break;
        }
    }

    private void pauseresumeGame() {
        if (engine.isRunning()) {
            engine.pause();
            model.getSoundManager().pauseBackgroundMusic();
            view.showGamePaused();
            model.getSoundManager().playPauseSound();
        } else {
            engine.resume();
            view.showMessage("Game Resumed");
            model.getSoundManager().resumeBackgroundMusic();
            view.removeGamePaused();
        }
    }

    private void setPhysicsToBall() {
        model.updateBallPos();
        handleWallCollisions();
        model.handlePaddleCollision();
        model.handleBlockCollisions();
    }

    private void handleWallCollisions() {
        model.handlerightwallcolide();
        model.handleleftwallcolide();
        model.handleceilingcolide();
        handlefloorcolide();
    }

    private void handlefloorcolide() {
        if (model.getBallob().getyBall() >= sceneHeigt - model.getBallob().getBallRadius()) {
            model.handlefloorcolideModel();
            model.getBallob().setGoDownBall(false);
            if (!model.isGoldStauts()) {
                model.dreHeart();
                view.handlefloorcolideView();

                if (model.getHeart() == 0) {
                    handlegameover();
                }
            }
        }
    }

    private void handlegameover() {
        view.showGameOver();
        model.handlefloorcolidegameover();
        engine.stop();
    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == model.getBlocks().size() && !leveldone) {
            nextLevel();
            leveldone = true;
        }
    }

    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {

                outputStream.writeInt(model.getLevel());
                outputStream.writeInt(model.getScore());
                outputStream.writeInt(model.getHeart());
                outputStream.writeInt(destroyedBlockCount);
                outputStream.writeDouble(model.getBallob().getxBall());
                outputStream.writeDouble(model.getBallob().getyBall());
                outputStream.writeDouble(model.getPaddle().getxBreak());
                outputStream.writeDouble(model.getPaddle().getyBreak());
                outputStream.writeDouble(model.getPaddle().getCenterBreakX());
                outputStream.writeLong(model.getTime());
                outputStream.writeLong(model.getGoldTime());
                outputStream.writeDouble(model.getvX());
                outputStream.writeBoolean(model.isExistHeartBlock());
                outputStream.writeBoolean(model.isGoldStauts());
                outputStream.writeBoolean(model.getBallob().isGoDownBall());
                outputStream.writeBoolean(model.getBallob().isGoRightBall());
                outputStream.writeBoolean(model.isColideToBreak());
                outputStream.writeBoolean(model.isColideToBreakAndMoveToRight());
                outputStream.writeBoolean(model.isColideToRightWall());
                outputStream.writeBoolean(model.isColideToLeftWall());
                outputStream.writeBoolean(model.isColideToRightBlock());
                outputStream.writeBoolean(model.isColideToBottomBlock());
                outputStream.writeBoolean(model.isColideToLeftBlock());
                outputStream.writeBoolean(model.isColideToTopBlock());

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
                for (Block block : model.getBlocks()) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializables);

                view.showMessage("Game Saved");


            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException occurred in saveGame", e);
            }
        }).start();
    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();
        model.setExistHeartBlock(loadSave.isExistHeartBlock);
        model.setGoldStauts(loadSave.isGoldStauts);
        model.getBallob().setGoDownBall(loadSave.goDownBall);
        model.getBallob().setGoRightBall(loadSave.goRightBall);
        model.setColideToBreak(loadSave.colideToBreak);
        model.setColideToBreakAndMoveToRight(loadSave.colideToBreakAndMoveToRight);
        model.setColideToRightWall(loadSave.colideToRightWall);
        model.setColideToLeftWall(loadSave.colideToLeftWall);
        model.setColideToRightBlock(loadSave.colideToRightBlock);
        model.setColideToBottomBlock(loadSave.colideToBottomBlock);
        model.setColideToLeftBlock(loadSave.colideToLeftBlock);
        model.setColideToTopBlock(loadSave.colideToTopBlock);
        model.setLevel(loadSave.level);
        model.setScore(loadSave.score);
        model.setHeart(loadSave.heart);
        destroyedBlockCount = loadSave.destroyedBlockCount;
        model.getBallob().setxBall(loadSave.xBall);
        model.getBallob().setyBall(loadSave.yBall);
        model.getPaddle().setxBreak(loadSave.xBreak);
        model.getPaddle().setyBreak(loadSave.yBreak);
        model.getPaddle().setCenterBreakX(loadSave.centerBreakX);
        model.setTime(loadSave.time);
        model.setGoldTime(loadSave.goldTime);
        model.setvX(loadSave.vX);
        model.getBlocks().clear();
        model.getChocos().clear();
        model.getBombs().clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            model.getBlocks().add(new Block(ser.row, ser.j, model.getColors()[r % model.getColors().length], ser.type));
        }

        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred in loadgame", e);
        }
    }

    private void nextLevel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    model.setvX(1.000);
                    engine.stop();
                    model.resetCollisionFlags();
                    model.getBallob().setGoDownBall(true);
                    model.setGoldStauts(false);
                    model.setExistHeartBlock(false);
                    model.setHitTime(0);
                    model.setTime(0);
                    model.setGoldTime(0);
                    engine.stop();
                    model.getBlocks().clear();
                    model.getChocos().clear();
                    model.getBombs().clear();
                    destroyedBlockCount = 0;
                    leveldone = false;
                    start(primaryStage);

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Exception occurred in nextLevel", e);
                }
            }
        });
    }

    public void restartGame() {

        try {
            model.setLevel(0);
            model.setHeart(3);
            model.setScore(0);
            model.setvX(1.000);
            destroyedBlockCount = 0;
            model.resetCollisionFlags();
            model.getBallob().setGoDownBall(true);
            model.setGoldStauts(false);
            model.setExistHeartBlock(false);
            model.setHitTime(0);
            model.setTime(0);
            model.setGoldTime(0);
            model.getBlocks().clear();
            model.getChocos().clear();
            model.getBombs().clear();

            start(primaryStage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred in restartgame", e);
        }
    }

    @Override
    public void onUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.initView();
                model.initchocobombModel();
            }
        });
        handlenotnormalblock();
    }

    private void handlenotnormalblock() {
        if (model.getBallob().getyBall() >= Block.getPaddingTop() && model.getBallob().getyBall() <= (Block.getHeight() * (model.getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : model.getBlocks()) {
                double hitCode = block.checkHitToBlock(model.getBallob().getxBall(), model.getBallob().getyBall(), model.getBallob().getBallRadius());
                if (hitCode != Block.NO_HIT) {
                    view.notnormalblockView(block);
                    model.incScore(1);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    model.resetCollisionFlags();
                    checkhitnotnormalblock(block);
                    model.checkhitcode(hitCode);
                }
            }
        }
    }

    private void checkhitnotnormalblock(Block block) {
        if (block.type == Block.BLOCK_CHOCO) {
            view.handlehitbonusblockView(block);
        }
        if (block.type == Block.BLOCK_STAR) {
            model.handlehitgoldblockModel();
            view.handlehitgoldblockView();
        }
        if (block.type == Block.BLOCK_HEART) {
            model.handlehitheartblockModel();
        }
        if (block.type == Block.BLOCK_BOMB) {
            view.handlehitbombblockView(block);
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();

        handlebombobject();
        handlegoldtime();
        handlebonusobject();
    }

    private void handlebombobject() {
        List<Bomb> bombsToRemove = new ArrayList<>();
        for (Bomb bomb : model.getBombs()) {
            bomb.updatePosition();
            if (model.bombHitsPaddle(bomb)) {
                model.handleBombPaddleCollisionModel(bomb);
                view.handleBombPaddleCollisionView(bomb);
                bombsToRemove.add(bomb);
            } else if (bomb.getY() > sceneHeigt) {
                bombsToRemove.add(bomb);
            }
        }
        model.getBombs().removeAll(bombsToRemove);
    }

    private void handlegoldtime() {
        if (model.getTime() - model.getGoldTime() > 5000) {
            model.handlegoldModel();
            view.handleremovegoldView();
        }
    }

    private void handlebonusobject() {
        List<Bonus> chocosToRemove = new ArrayList<>();
        for (Bonus choco : model.getChocos()) {
            if (choco.getY() > sceneHeigt || choco.isTaken()) {
                continue;
            }
            if (model.chocoHitsBreak(choco)) {
                model.handleChocoHitModel(choco);
                view.handleChocoHitView(choco);
                chocosToRemove.add(choco);
            }
            model.updateChocoPosition(choco);
        }
        model.getChocos().removeAll(chocosToRemove);
    }

    @Override
    public void onTime(long time) {
        model.setTime(time);
    }

    public boolean isLoadFromSave() {
        return loadFromSave;
    }
    private void initBreak() {
        view.initBreakView();
    }
    public Model getModel() {
        return model;
    }
}