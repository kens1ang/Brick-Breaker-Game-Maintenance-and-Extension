package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private int level = 0;
    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 500;
    private int sceneHeigt = 700;
    private static int LEFT  = 1;
    private static int RIGHT = 2;
    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private int       ballRadius = 10;

    private int destroyedBlockCount = 0;

    private double v = 1.000;

    private int  heart    = 100000000;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;

    private GameEngine engine;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir = "C:/save/";

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;

    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        if (loadFromSave == false) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            initBall();
            initBreak();
            initBoard();

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            load.setTranslateX(220);
            load.setTranslateY(300);
            newGame.setTranslateX(220);
            newGame.setTranslateY(340);

        }


        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);
        if (loadFromSave == false) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (loadFromSave == false) {
            if (level > 1 && level < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            load.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadGame();

                    load.setVisible(false);
                    newGame.setVisible(false);
                }
            });

            newGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    engine = new GameEngine();
                    engine.setOnAction(Main.this);
                    engine.setFps(120);
                    engine.start();

                    load.setVisible(false);
                    newGame.setVisible(false);
                }
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }


    }

    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                //System.out.println("colors " + r % (colors.length));
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }

    float oldXBreak;

    private void move(final int direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        LOGGER.log(Level.SEVERE, "Interrupted Exception in move method", e);
                        Thread.currentThread().interrupt();                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }

    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        int lowestBlockY = (int) ((level + 1) * Block.getHeight() + Block.getPaddingTop());
        int paddleYPosition = (int) yBreak;
        int safeSpawnStartY = lowestBlockY + ballRadius;
        int safeSpawnEndY = paddleYPosition - ballRadius;
        if (safeSpawnStartY < safeSpawnEndY) {
            yBall = random.nextInt(safeSpawnEndY - safeSpawnStartY) + safeSpawnStartY;
        } else {
            yBall = safeSpawnStartY;
        }
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
    }


    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;

    private double vX = 1.000;
    private double vY = 1.000;


    private void resetColideFlags() {

        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;

        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {
            //vX = 1.000;
            resetColideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeigt) {
            resetColideFlags();
            goDownBall = false;
            if (!isGoldStauts) {
                //TODO gameover
                heart--;
                new Score().show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            //return;
        }

        if (yBall >= yBreak - ballRadius) {
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetColideFlags();
                colideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / ((double) breakWidth / 2);

                vX = calculateHorizontalVelocity(relation);

                goRightBall = xBall < centerBreakX;
            }
        }

        if (xBall >= sceneWidth) {
            resetColideFlags();
            //vX = 1.000;
            colideToRightWall = true;
        }

        if (xBall <= 0) {
            resetColideFlags();
            //vX = 1.000;
            colideToLeftWall = true;
        }

        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

//        Wall Colide

        if (colideToRightWall) {
            goRightBall = false;
        }

        if (colideToLeftWall) {
            goRightBall = true;
        }

//        Block Colide

        if (colideToRightBlock) {
            goRightBall = true;
        }

        if (colideToLeftBlock) {
            goRightBall = false;
        }

        if (colideToTopBlock) {
            goDownBall = false;
        }

        if (colideToBottomBlock) {
            goDownBall = true;
        }
    }

    private double calculateHorizontalVelocity(double relation) {
        if (Math.abs(relation) <= 0.3) {
            return Math.abs(relation);
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            return (Math.abs(relation) * 1.5) + (level / 3.500);
        } else {
            return (Math.abs(relation) * 2) + (level / 3.500);
        }
    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStauts);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(colideToBreak);
                outputStream.writeBoolean(colideToBreakAndMoveToRight);
                outputStream.writeBoolean(colideToRightWall);
                outputStream.writeBoolean(colideToLeftWall);
                outputStream.writeBoolean(colideToRightBlock);
                outputStream.writeBoolean(colideToBottomBlock);
                outputStream.writeBoolean(colideToLeftBlock);
                outputStream.writeBoolean(colideToTopBlock);

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializables);

                new Score().showMessage("Game Saved", Main.this);


            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException occurred in saveGame", e);
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStauts = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.colideToBreak;
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        colideToRightWall = loadSave.colideToRightWall;
        colideToLeftWall = loadSave.colideToLeftWall;
        colideToRightBlock = loadSave.colideToRightBlock;
        colideToBottomBlock = loadSave.colideToBottomBlock;
        colideToLeftBlock = loadSave.colideToLeftBlock;
        colideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred in nextLevel", e);
        }
    }

    private void nextLevel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    vX = 1.000;

                    engine.stop();
                    resetColideFlags();
                    goDownBall = true;

                    isGoldStauts = false;
                    isExistHeartBlock = false;


                    hitTime = 0;
                    time = 0;
                    goldTime = 0;

                    engine.stop();
                    blocks.clear();
                    chocos.clear();
                    destroyedBlockCount = 0;
                    start(primaryStage);

                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Exception occurred in nextLevel", e);
                }
            }
        });
    }

    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetColideFlags();
            goDownBall = true;

            isGoldStauts = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            start(primaryStage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred in restartGame", e);
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                scoreLabel.setText("Score: " + score);
                heartLabel.setText("Heart : " + heart);

                rect.setX(xBreak);
                rect.setY(yBreak);
                ball.setCenterX(xBall);
                ball.setCenterY(yBall);

                for (Bonus choco : chocos) {
                    choco.choco.setY(choco.y);
                }
            }
        });


        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                double hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    new Score().show(block.x, block.y, 1, this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    //System.out.println("size is " + blocks.size());
                    resetColideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                root.getChildren().add(choco.choco);
                            }
                        });
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        System.out.println("gold ball");
                        root.getStyleClass().add("goldRoot");
                        isGoldStauts = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        colideToRightBlock = true;
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        colideToBottomBlock = true;
                    } else if (hitCode == Block.HIT_LEFT) {
                        colideToLeftBlock = true;
                    } else if (hitCode == Block.HIT_TOP) {
                        colideToTopBlock = true;
                    }

                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }


    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();

        if (time - goldTime > 5000 && isGoldStauts) {
            Platform.runLater(() -> {
                ball.setFill(new ImagePattern(new Image("ball.png")));
                root.getStyleClass().remove("goldRoot");
            });
            isGoldStauts = false;
        }

        List<Bonus> toRemove = new ArrayList<>();
        for (Bonus choco : chocos) {
            if (choco.y > sceneHeigt || choco.taken) {
                continue;
            }
            if (chocoHitsBreak(choco)) {
                handleChocoHit(choco);
                toRemove.add(choco);
            }
            updateChocoPosition(choco);
        }

        chocos.removeAll(toRemove);
    }

    private boolean chocoHitsBreak(Bonus choco) {
        return choco.y >= yBreak && choco.y <= yBreak + breakHeight &&
                choco.x >= xBreak && choco.x <= xBreak + breakWidth;
    }

    private void handleChocoHit(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        Platform.runLater(() -> choco.choco.setVisible(false));
        score += 3;
        new Score().show(choco.x, choco.y, 3, this);
    }

    private void updateChocoPosition(Bonus choco) {
        choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

}
