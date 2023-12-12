package brickGame.Controller;

import brickGame.Model.GameElements.GameBlock;
import brickGame.Model.GameElements.BonusBlock;
import brickGame.Model.GameElements.PenaltyBlock;
import brickGame.Model.GameModel;
import brickGame.View.GameView;
import brickGame.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The GameController class is responsible for controlling the game flow, handling user input,
 * managing the game state, and interacting with the GameModel and GameView components.
 */
public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final GameModel model;
    private final GameView view;
    private GameEngine engine;
    Stage primaryStage;
    private boolean loadFromSave = false;
    private boolean leveldone = false;
    private final int sceneHeigt = 700;
    /**
     * Constructs a GameController object with an associated GameModel and GameView.
     */
    public GameController() {
        this.model = new GameModel();
        this.view = new GameView(this);
        this.engine = new GameEngine();
    }
    /**
     * Starts the game by initializing the game elements, setting up the UI, and starting the game engine.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
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
                initgameelements();
            }

            view.initroot();
            view.initscene(primaryStage);
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
            leveldone = false;
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Exception occurred in GameController.start", e);
        }
    }
    /**
     * Initializes various game elements, including the ball, break, game board model, and start menu button.
     */
    private void initgameelements() {
        initBall();
        view.initBreak();
        model.initBoardModel();
        model.setInitialblockcount(model.getBlocks().size());
        view.initstartmenubutton();
    }
    /**
     * Sets up event handlers for the load and new game buttons in the view.
     */
    private void handleloadnewgamebutton() {
        view.getLoad().setOnAction(event -> {
            loadGame();
            view.setLoadvisible(false);
            view.setNewGamevisible(false);
        });
        view.getNewGame().setOnAction(event -> {
            initGameEngine();
            view.setLoadvisible(false);
            view.setNewGamevisible(false);
        });
    }
    /**
     * Initializes the game engine, sets up event handling, and starts the engine with a specified frames per second (fps).
     */
    private void initGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }
    /**
     * Initializes the ball's position and the corresponding view.
     */
    private void initBall() {
        model.initBallPos();
        view.initBallView();
    }
    /**
     * Handles keyboard events, including movement, saving the game, and pausing/resuming the game.
     *
     * @param event The KeyEvent representing the pressed key.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                model.move(GameModel.getLEFT());
                break;
            case RIGHT:
                model.move(GameModel.getRIGHT());
                break;
            case S:
                saveGame();
                break;
            case SPACE:
                pauseresumeGame();
                break;
        }
    }
    /**
     * Pauses or resumes the game based on the current state of the game engine.
     * Displays corresponding messages and updates the background music accordingly.
     */
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
    /**
     * Applies physics to the ball, updating its position and handling collisions with walls, paddles, and blocks.
     */
    private void setPhysicsToBall() {
        model.updateBallPos();
        handleWallCollisions();
        model.handlePaddleCollision();
        model.handleBlockCollisions();
    }
    /**
     * Handles collisions with walls, including the right wall, left wall, and ceiling.
     * Calls handlefloorcolide() to handle collisions with the floor.
     */
    private void handleWallCollisions() {
        model.handlerightwallcolide();
        model.handleleftwallcolide();
        model.handleceilingcolide();
        handlefloorcolide();
    }
    /**
     * Handles collisions with the floor, updating the game model and view accordingly.
     * Triggers game over if the player runs out of hearts.
     */
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
    /**
     * Displays the game over message, handles game over logic in the model, and stops the game engine.
     */
    private void handlegameover() {
        view.showGameOver();
        model.handlefloorcolidegameover();
        engine.stop();
    }
    /**
     * Checks the count of destroyed blocks and proceeds to the next level if all blocks are destroyed.
     */
    private void checkDestroyedCount() {
        if (model.getDestroyedBlockCount() == model.getInitialblockcount() && !leveldone) {

            leveldone = true;
            nextLevel();
        }
    }
    /**
     * Saves the current state of the game, including the level, score, ball position, paddle position, and block information.
     * This method runs in a separate thread to avoid blocking the main application.
     */
    private void saveGame() {
        model.getLoadSave().savegamedata(model);
        view.showMessage("Game Saved");
    }
    /**
     * Loads a saved game state, including the level, score, ball position, paddle position, and block information.
     * Initializes the game with the loaded state.
     */
    private void loadGame() {
        model.getLoadSave().loadgamedata(model);
        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred in loadgame", e);
        }
    }
    /**
     * Advances the game to the next level, resetting necessary parameters and initializing the game elements.
     */
    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                model.setvX(2.000);
                engine.stop();
                model.resetCollisionFlags();
                model.getBallob().setGoDownBall(true);
                model.setGoldStauts(false);
                model.setExistHeartBlock(false);
                model.setTime(0);
                model.setGoldTime(0);
                engine.stop();
                model.getBlocks().clear();
                model.getChocos().clear();
                model.getBombs().clear();
                model.setDestroyedBlockCount(0);
                leveldone = false;
                start(primaryStage);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception occurred in nextLevel", e);
            }
        });
    }
    /**
     * Restarts the game with initial settings, including level, score, ball position, paddle position, and block configuration.
     */
    public void restartGame() {

        try {
            model.setLevel(0);
            model.setHeart(5);
            model.setScore(0);
            model.setvX(2.000);
            model.setDestroyedBlockCount(0);
            model.resetCollisionFlags();
            model.getBallob().setGoDownBall(true);
            model.setGoldStauts(false);
            model.setExistHeartBlock(false);
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
    /**
     * Updates the game view, handling non-normal block interactions and playing background music.
     */
    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            view.initView();
            model.initchocobombModel();
        });
        handlenotnormalblock();
        model.getSoundManager().playBackgroundMusic(0.5);
    }
    /**
     * Handles interactions with non-normal blocks when the ball is within the appropriate vertical range.
     * Checks for collisions with each non-normal block, updates the view, and handles specific block types.
     */
    private void handlenotnormalblock() {
        if (model.getBallob().getyBall() >= GameBlock.getPaddingTop() && model.getBallob().getyBall() <= (GameBlock.getHeight() * (model.getLevel() + 1)) + GameBlock.getPaddingTop()) {
            for (final GameBlock block : model.getBlocks()) {
                double hitCode = block.checkHitToBlock(model.getBallob().getxBall(), model.getBallob().getyBall(), model.getBallob().getBallRadius());
                if (hitCode != GameBlock.NO_HIT) {
                    view.notnormalblockView(block);
                    model.incScore(1);
                    block.isDestroyed = true;
                    model.incDestroyedBlockCount();
                    model.resetCollisionFlags();
                    checkhitnotnormalblock(block);
                    model.checkhitcode(hitCode);
                }
            }
        }
    }
    /**
     * Checks the type of special block and performs specific actions based on its type.
     *
     * @param block The non-normal block to check and handle.
     */
    private void checkhitnotnormalblock(GameBlock block) {
        if (block.type == GameBlock.BLOCK_CHOCO) {
            view.handlehitbonusblockView(block);
        }
        if (block.type == GameBlock.BLOCK_STAR) {
            model.handlehitgoldblockModel();
            view.handlehitgoldblockView();
        }
        if (block.type == GameBlock.BLOCK_HEART) {
            model.handlehitheartblockModel();
        }
        if (block.type == GameBlock.BLOCK_BOMB) {
            view.handlehitbombblockView(block);
        }
    }
    /**
     * Initialization method called when the game controller is initialized.
     * This method can be overridden to perform any necessary setup during the game initialization phase.
     */
    @Override
    public void onInit() {

    }
    /**
     * Handles physics-related updates, including checking destroyed block counts, ball physics, bomb interactions,
     * removal of gold status, and bonus object interactions.
     */
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        handlebombobject();
        handleremovegoldstatus();
        handlebonusobject();
    }
    /**
     * Handles the movement and collisions of bomb objects in the game.
     */
    private void handlebombobject() {
        List<PenaltyBlock> bombsToRemove = new ArrayList<>();
        for (PenaltyBlock bomb : model.getBombs()) {
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
    /**
     * Handles the removal of gold status if a certain time has passed since acquiring it.
     */
    private void handleremovegoldstatus() {
        if (model.getTime() - model.getGoldTime() > 5000) {
            model.handleremovegoldModel();
            view.handleremovegoldView();
        }
    }
    /**
     * Handles the movement, collisions, and interactions of bonus objects, such as chocos, in the game.
     */
    private void handlebonusobject() {
        List<BonusBlock> chocosToRemove = new ArrayList<>();
        for (BonusBlock choco : model.getChocos()) {
            if (choco.getY() > sceneHeigt || choco.isTaken()) {
                continue;
            }
            if (model.chocoHitsBreak(choco)) {
                model.handleChocoHitModel(choco);
                view.handleChocoHitView(choco);
                chocosToRemove.add(choco);
            }
            choco.updateChocoPosition(choco, model);
        }
        model.getChocos().removeAll(chocosToRemove);
    }
    /**
     * Handles time updates in the game.
     *
     * @param time The current time value.
     */
    @Override
    public void onTime(long time) {
        model.setTime(time);
    }
    /**
     * Returns true if the game is loaded from a save, false otherwise.
     *
     * @return True if loaded from save, false otherwise.
     */
    public boolean isLoadFromSave() {
        return loadFromSave;
    }
    /**
     * Returns the instance of the game model associated with this game controller.
     *
     * @return The game model instance.
     */
    public GameModel getModel() {
        return model;
    }
}