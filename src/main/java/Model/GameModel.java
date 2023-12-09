package Model;

import GameElements.GameBlock;
import GameElements.BonusBlock;
import GameElements.PenaltyBlock;
import brickGame.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameModel {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final GameBall ballob;
    private final GamePaddle paddle;
    private final SoundManager soundManager;
    private int  heart = 100;
    private int level = 0;
    private int score = 0;
    private long time = 0;
    private long goldTime = 0;
    private final int sceneWidth = 500;
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private double vX = 2.000;
    private boolean isGoldStauts                = false;
    private boolean isExistHeartBlock           = false;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;
    private final ArrayList<GameBlock> blocks = new ArrayList<>();
    private final ArrayList<BonusBlock> chocos = new ArrayList<>();
    private final ArrayList<PenaltyBlock> bombs = new ArrayList<>();
    private final Color[]          colors = new Color[]{
            Color.rgb(64, 224, 208),
            Color.rgb(255, 105, 180),
            Color.rgb(143, 0, 255),
            Color.rgb(57, 255, 20),
            Color.rgb(255, 69, 0),
            Color.rgb(135, 206, 235),
            Color.rgb(255, 247, 0),
            Color.rgb(0, 163, 232),
            Color.rgb(158, 196, 0),
            Color.rgb(255, 127, 39),
            Color.rgb(111, 45, 168),
            Color.rgb(237, 28, 36),
            Color.rgb(255, 242, 0),
    };

    public GameModel() {
        this.paddle = new GamePaddle();
        this.ballob = new GameBall();
        this.soundManager = new SoundManager();
    }

    public void initBallPos() {
        randomballspawn();
        setballpos();
    }

    private void randomballspawn() {
        Random rand = new Random();
        ballob.setGoRightBall(rand.nextBoolean());
        ballob.setGoDownBall(rand.nextBoolean());
    }

    private void setballpos() {
        double horizontalCenter = sceneWidth / 2.0;
        double lowestBlockBottom = ((level + 1) * GameBlock.getHeight()) + GameBlock.getPaddingTop();
        double paddleTop = paddle.getyBreak();
        double verticalCenter = (lowestBlockBottom + paddleTop) / 2.0;
        ballob.setxBall(horizontalCenter);
        ballob.setyBall(verticalCenter);
    }

    public void initBoardModel() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < calculateBlockCount(); j++) {
                int r = random.nextInt(500);
                int type = determineBlockType(r);

                blocks.add(new GameBlock(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    private int calculateBlockCount() {
        return (level == 18) ? 13 : level + 1;
    }

    private int determineBlockType(int randomNumber) {
        if (level == 18) {
            return GameBlock.BLOCK_BOMB;
        } else {
            if (randomNumber % 5 == 0) {
                return GameBlock.BLOCK_NORMAL;
            } else if (randomNumber % 10 == 1) {
                return GameBlock.BLOCK_CHOCO;
            } else if (randomNumber % 10 == 2) {
                return isExistHeartBlock ? GameBlock.BLOCK_NORMAL : GameBlock.BLOCK_HEART;
            } else if (randomNumber % 10 == 3) {
                return GameBlock.BLOCK_STAR;
            } else if (randomNumber % 10 == 7) {
                return GameBlock.BLOCK_BOMB;
            } else {
                return GameBlock.BLOCK_NORMAL;
            }
        }
    }

    public void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 4;
            for (int i = 0; i < 30; i++) {
                if (paddle.getxBreak() == (sceneWidth - paddle.getBreakWidth()) && direction == RIGHT) {
                    return;
                }
                if (paddle.getxBreak() == 0 && direction == LEFT) {
                    return;
                }
                if (direction == RIGHT) {
                    paddle.addxBreak();
                } else {
                    paddle.minusxBreak();
                }
                paddle.setCenterBreakX(paddle.getxBreak() + paddle.getHalfBreakWidth());
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Interrupted Exception in move method", e);
                    Thread.currentThread().interrupt();
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }

    public void updateBallPos() {
        double vY = 2.000;
        if (ballob.isGoDownBall()) {
            ballob.incyBall(vY);
        } else {
            ballob.dreyBall(vY);
        }

        if (ballob.isGoRightBall()) {
            ballob.incxBall(vX);
        } else {
            ballob.drexBall(vX);
        }
    }

    public void handlerightwallcolide() {
        if (ballob.getxBall() >= sceneWidth - ballob.getBallRadius()) {
            resetCollisionFlags();
            colideToRightWall = true;
            ballob.setGoRightBall(false);
        }
    }

    public void handleleftwallcolide() {
        if (ballob.getxBall() <= ballob.getBallRadius()) {
            resetCollisionFlags();
            colideToLeftWall = true;
            ballob.setGoRightBall(true);
        }
    }

    public void handleceilingcolide() {
        if (ballob.getyBall() <= ballob.getBallRadius()) {
            resetCollisionFlags();
            ballob.setGoDownBall(true);
        }
    }

    public void handlefloorcolideModel() {
        resetCollisionFlags();
        if (!isGoldStauts) {
            soundManager.playMinusHeartSound();
        }
    }

    public void handlefloorcolidegameover() {
        soundManager.playGameOverSound();
    }

    public void handlePaddleCollision() {
        if (ballob.getyBall() >= paddle.getyBreak() - ballob.getBallRadius() && ballob.getyBall() - ballob.getBallRadius() <= paddle.getyBreak() + paddle.getBreakHeight() &&
                ballob.getxBall() >= paddle.getxBreak() - ballob.getBallRadius() && ballob.getxBall() - ballob.getBallRadius() <= paddle.getxBreak() + paddle.getBreakWidth()) {
            resetCollisionFlags();
            colideToBreak = true;
            ballob.setGoDownBall(false);
            updateVelocityOnPaddleCollision();
            colideToBreakAndMoveToRight = ballob.getxBall() - paddle.getCenterBreakX() > 0;
        }
        if (colideToBreak) {
            ballob.setGoRightBall(colideToBreakAndMoveToRight);
        }
    }

    public void updateVelocityOnPaddleCollision() {
        double relation = (ballob.getxBall() - paddle.getCenterBreakX()) / ((double) paddle.getBreakWidth() / 2);

        if (Math.abs(relation) <= 0.3) {
            vX = Math.abs(relation);
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            vX = (Math.abs(relation) * 1.5) + (level / 3.500);
        } else {
            vX = (Math.abs(relation) * 2) + (level / 3.500);
        }
    }

    public void handleBlockCollisions() {
        if (colideToRightBlock) {
            ballob.setGoRightBall(true);
        }
        if (colideToLeftBlock) {
            ballob.setGoRightBall(false);
        }
        if (colideToTopBlock) {
            ballob.setGoDownBall(false);
        }
        if (colideToBottomBlock) {
            ballob.setGoDownBall(true);
        }
    }

    public void resetCollisionFlags() {
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;

        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    public void initchocobombModel() {
        for (BonusBlock choco : chocos) {
            choco.choco.setY(choco.getY());
        }

        for (PenaltyBlock bomb : bombs){
            bomb.getBomb().setY(bomb.getY());
        }
    }

    public void handlehitgoldblockModel() {
        goldTime = time;
        soundManager.playHitStarSound();
        System.out.println("gold ball");
        isGoldStauts = true;
    }

    public void handlehitheartblockModel() {
        heart++;
        soundManager.playCollectHeartSound();
    }

    public void checkhitcode(double hitCode) {
        if (hitCode == GameBlock.HIT_RIGHT) {
            colideToRightBlock = true;
            soundManager.playBlockHitSound();
        }
        if (hitCode == GameBlock.HIT_BOTTOM) {
            colideToBottomBlock = true;
            soundManager.playBlockHitSound();
        }
        if (hitCode == GameBlock.HIT_LEFT) {
            colideToLeftBlock = true;
            soundManager.playBlockHitSound();
        }
        if (hitCode == GameBlock.HIT_TOP) {
            colideToTopBlock = true;
            soundManager.playBlockHitSound();
        }
    }

    public void handleremovegoldModel() {
        isGoldStauts = false;
        goldTime = 0;
    }

    public boolean chocoHitsBreak(BonusBlock choco) {
        return choco.getY() >= paddle.getyBreak() && choco.getY() <= paddle.getyBreak() + paddle.getBreakHeight() &&
                choco.getX() >= paddle.getxBreak() && choco.getX() <= paddle.getxBreak() + paddle.getBreakWidth();
    }

    public void handleChocoHitModel(BonusBlock choco) {
        System.out.println("You Got it and +3 score for you");
        soundManager.playCollectBonusSound();
        choco.setTaken(true);
        score += 3;
    }

    public boolean bombHitsPaddle(PenaltyBlock bomb) {
        return bomb.getY() >= paddle.getyBreak() && bomb.getY() <= paddle.getyBreak() + paddle.getBreakHeight() &&
                bomb.getX() >= paddle.getxBreak() && bomb.getX() <= paddle.getxBreak() + paddle.getBreakWidth();
    }

    public void handleBombPaddleCollisionModel(PenaltyBlock bomb) {
        soundManager.playBombHitSound();
        bomb.setTaken(true);
    }

    public GameBall getBallob() {
        return ballob;
    }
    public GamePaddle getPaddle() {
        return paddle;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void inclevel() {
        this.level ++;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void incScore(int inc) {
        this.score += inc;
    }
    public int getHeart() {
        return heart;
    }
    public void setHeart(int heart) {
        this.heart = heart;
    }
    public void dreHeart() {
        this.heart--;
    }
    public ArrayList<GameBlock> getBlocks() {
        return blocks;
    }
    public ArrayList<PenaltyBlock> getBombs() {
        return bombs;
    }
    public ArrayList<BonusBlock> getChocos() {
        return chocos;
    }
    public void setColideToTopBlock(boolean colideToTopBlock) {
        this.colideToTopBlock = colideToTopBlock;
    }
    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }
    public void setColideToRightBlock(boolean colideToRightBlock) {
        this.colideToRightBlock = colideToRightBlock;
    }
    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }
    public void setColideToLeftBlock(boolean colideToLeftBlock) {
        this.colideToLeftBlock = colideToLeftBlock;
    }
    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }
    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }
    public void setColideToBottomBlock(boolean colideToBottomBlock) {
        this.colideToBottomBlock = colideToBottomBlock;
    }
    public void setvX(double vX) {
        this.vX = vX;
    }
    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }
    public void setGoldStauts(boolean goldStauts) {
        isGoldStauts = goldStauts;
    }
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public boolean isColideToTopBlock() {
        return colideToTopBlock;
    }
    public boolean isColideToRightWall() {
        return colideToRightWall;
    }
    public boolean isColideToRightBlock() {
        return colideToRightBlock;
    }
    public boolean isColideToLeftWall() {
        return colideToLeftWall;
    }
    public boolean isColideToLeftBlock() {
        return colideToLeftBlock;
    }
    public boolean isColideToBreakAndMoveToRight() {
        return colideToBreakAndMoveToRight;
    }
    public boolean isColideToBreak() {
        return colideToBreak;
    }
    public boolean isColideToBottomBlock() {
        return colideToBottomBlock;
    }
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }
    public boolean isGoldStauts() {
        return isGoldStauts;
    }
    public long getTime() {
        return time;
    }
    public long getGoldTime() {
        return goldTime;
    }
    public double getvX() {
        return vX;
    }
    public static int getLEFT() {
        return LEFT;
    }
    public static int getRIGHT() {
        return RIGHT;
    }
    public Color[] getColors() {
        return colors;
    }
}