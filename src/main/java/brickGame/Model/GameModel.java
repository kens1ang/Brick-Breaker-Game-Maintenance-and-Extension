package brickGame.Model;

import brickGame.Model.GameElements.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;

import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
/**
 * Represents the game model in this game.
 * Manages the state of the game, including the ball, paddle, blocks, and bonuses.
 */
public class GameModel {
    private final GameBall ballob;
    private final GamePaddle paddle;
    private final SoundManager soundManager;
    private final LoadSave loadSave;
    private int  heart = 100;
    private int level = 0;
    private int score = 0;
    private long time = 0;
    private long goldTime = 0;
    public int destroyedBlockCount = 0;
    private int initialblockcount;
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
    /**
     * Constructs a new GameModel with default settings.
     * Initializes the instances of GamePaddle, GameBall, SoundManager, and loadSave.
     */
    public GameModel() {
        this.paddle = new GamePaddle();
        this.ballob = new GameBall();
        this.soundManager = new SoundManager();
        this.loadSave = new LoadSave();
    }
    /**
     * Initializes the position of the ball randomly and sets its starting position.
     */
    public void initBallPos() {
        randomballspawn();
        setballpos();
    }
    /**
     * Randomly determines the initial direction of the game ball.
     */
    private void randomballspawn() {
        Random rand = new Random();
        ballob.setGoRightBall(rand.nextBoolean());
        ballob.setGoDownBall(rand.nextBoolean());
    }
    /**
     * Sets the initial position of the game ball based on the scene's dimensions and level.
     */
    private void setballpos() {
        double horizontalCenter = sceneWidth / 2.0;
        double lowestBlockBottom = ((level + 1) * GameBlock.getHeight()) + GameBlock.getPaddingTop();
        double paddleTop = paddle.getyBreak();
        double verticalCenter = (lowestBlockBottom + paddleTop) / 2.0;
        ballob.setxBall(horizontalCenter);
        ballob.setyBall(verticalCenter);
    }
    /**
     * Gets the index of the specified color in the given array of colors.
     *
     * @param color  The color to find the index of.
     * @param colors The array of colors to search.
     * @return The index of the color in the array, or -1 if the color is not found.
     */
    public int getColorIndex(Color color, Color[] colors) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(color)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Initializes the game board by generating random blocks based on the current level.
     * Populates the 'blocks' list with instances of GameBlock.
     */
    public void initBoardModel() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < calculateBlockCount(); j++) {
                int r = random.nextInt(500);
                int type = determineBlockType(r);

                blocks.add(new GameBlock(j, i, colors[r % (colors.length)], type));
            }
        }
        initialblockcount = blocks.size();
    }
    /**
     * Calculates the number of blocks to be generated based on the current level.
     *
     * @return The calculated block count.
     */
    private int calculateBlockCount() {
        return (level == 18) ? 13 : level + 1;
    }
    /**
     * Determines the type of block to be generated based on a random number.
     * The type is influenced by the current level and the existence of heart blocks.
     *
     * @param randomNumber The randomly generated number.
     * @return The type of block (e.g., normal, choco, heart, star, bomb).
     */
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
    /**
     * Moves the paddle in the specified direction for a short duration.
     *
     * @param direction The direction to move the paddle (LEFT or RIGHT).
     */
    public void move(final int direction) {
        final int[] durationMillis = {4};

        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(durationMillis[0]),
                        event -> {
                            if (paddle.getxBreak() == (sceneWidth - paddle.getBreakWidth()) && direction == RIGHT) {
                                timeline.stop();
                                return;
                            }
                            if (paddle.getxBreak() == 0 && direction == LEFT) {
                                timeline.stop();
                                return;
                            }
                            if (direction == RIGHT) {
                                paddle.addxBreak();
                            } else {
                                paddle.minusxBreak();
                            }
                            paddle.setCenterBreakX(paddle.getxBreak() + paddle.getHalfBreakWidth());
                            durationMillis[0]++;
                        }
                )
        );

        timeline.setCycleCount(30);
        timeline.play();
    }
    /**
     * Updates the position of the game ball based on its current direction.
     */
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
    /**
     * Handles the collision with the right wall, reversing the ball's direction if necessary.
     */
    public void handlerightwallcolide() {
        if (ballob.getxBall() >= sceneWidth - ballob.getBallRadius()) {
            resetCollisionFlags();
            colideToRightWall = true;
            ballob.setGoRightBall(false);
        }
    }
    /**
     * Handles the collision with the left wall, reversing the ball's direction if necessary.
     */
    public void handleleftwallcolide() {
        if (ballob.getxBall() <= ballob.getBallRadius()) {
            resetCollisionFlags();
            colideToLeftWall = true;
            ballob.setGoRightBall(true);
        }
    }
    /**
     * Handles the collision with the ceiling, reversing the ball's vertical direction if necessary.
     */
    public void handleceilingcolide() {
        if (ballob.getyBall() <= ballob.getBallRadius()) {
            resetCollisionFlags();
            ballob.setGoDownBall(true);
        }
    }
    /**
     * Handles the collision with the floor, updating the game state accordingly.
     */
    public void handlefloorcolideModel() {
        resetCollisionFlags();
        if (!isGoldStauts) {
            soundManager.playMinusHeartSound();
        }
    }
    /**
     * Handles the collision with the floor, indicating game over and playing a sound.
     */
    public void handlefloorcolidegameover() {
        soundManager.playGameOverSound();
    }
    /**
     * Handles the collision between the ball and the paddle, updating the ball's direction and velocity.
     */
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
    /**
     * Updates the ball's velocity based on the collision with the paddle.
     */

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
    /**
     * Handles collisions between the ball and game blocks, updating the game state accordingly.
     */
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
    /**
     * Resets the collision flags for the ball and paddle interactions.
     */
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
    /**
     * Initializes the position of choco and bomb blocks to their initial state.
     */
    public void initchocobombModel() {
        for (BonusBlock choco : chocos) {
            choco.choco.setY(choco.getY());
        }

        for (PenaltyBlock bomb : bombs){
            bomb.getBomb().setY(bomb.getY());
        }
    }
    /**
     * Handles the special case of hitting a gold block, triggering a special status and sound.
     */
    public void handlehitgoldblockModel() {
        goldTime = time;
        soundManager.playHitStarSound();
        System.out.println("gold ball");
        isGoldStauts = true;
    }
    /**
     * Handles the case of hitting a heart block, increasing the player's heart count and playing a sound.
     */
    public void handlehitheartblockModel() {
        heart++;
        soundManager.playCollectHeartSound();
    }
    /**
     * Checks the hit code from block collisions and updates the game state accordingly.
     *
     * @param hitCode The hit code indicating the collision type.
     */
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
    /**
     * Handles the removal of the gold block status, resetting the special status and time.
     */
    public void handleremovegoldModel() {
        isGoldStauts = false;
        goldTime = 0;
    }
    /**
     * Checks if a bonus block (choco) hits the paddle.
     *
     * @param choco The bonus block to check.
     * @return True if the choco block hits the paddle, false otherwise.
     */
    public boolean chocoHitsBreak(BonusBlock choco) {
        return choco.getY() >= paddle.getyBreak() && choco.getY() <= paddle.getyBreak() + paddle.getBreakHeight() &&
                choco.getX() >= paddle.getxBreak() && choco.getX() <= paddle.getxBreak() + paddle.getBreakWidth();
    }
    /**
     * Handles the case of a bonus block (choco) hitting the paddle, updating the score and playing a sound.
     *
     * @param choco The bonus block that hit the paddle.
     */
    public void handleChocoHitModel(BonusBlock choco) {
        System.out.println("You Got it and +3 score for you");
        soundManager.playCollectBonusSound();
        choco.setTaken(true);
        score += 3;
    }
    /**
     * Checks if a bomb block hits the paddle.
     *
     * @param bomb The bomb block to check.
     * @return True if the bomb block hits the paddle, false otherwise.
     */
    public boolean bombHitsPaddle(PenaltyBlock bomb) {
        return bomb.getY() >= paddle.getyBreak() && bomb.getY() <= paddle.getyBreak() + paddle.getBreakHeight() &&
                bomb.getX() >= paddle.getxBreak() && bomb.getX() <= paddle.getxBreak() + paddle.getBreakWidth();
    }
    /**
     * Handles the case of a bomb block hitting the paddle, playing a sound and marking the bomb as taken.
     *
     * @param bomb The bomb block that hit the paddle.
     */
    public void handleBombPaddleCollisionModel(PenaltyBlock bomb) {
        soundManager.playBombHitSound();
        bomb.setTaken(true);
    }
    /**
     * Returns the instance of the GameBall in the game model.
     *
     * @return The GameBall instance.
     */
    public GameBall getBallob() {
        return ballob;
    }
    /**
     * Returns the instance of the GamePaddle in the game model.
     *
     * @return The GamePaddle instance.
     */
    public GamePaddle getPaddle() {
        return paddle;
    }
    /**
     * Returns the instance of the SoundManager in the game model.
     *
     * @return The SoundManager instance.
     */
    public SoundManager getSoundManager() {
        return soundManager;
    }
    /**
     * Returns the current level of the game.
     *
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }
    /**
     * Sets the level of the game to the specified value.
     *
     * @param level The new level value.
     */
    public void setLevel(int level) {
        this.level = level;
    }
    /**
     * Increments the current level of the game by 1.
     */
    public void inclevel() {
        this.level ++;
    }
    /**
     * Returns the current score in the game.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }
    /**
     * Sets the score in the game to the specified value.
     *
     * @param score The new score value.
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * Increments the current score in the game by the specified amount.
     *
     * @param inc The amount by which the score should be incremented.
     */
    public void incScore(int inc) {
        this.score += inc;
    }
    /**
     * Returns the current heart count in the game.
     *
     * @return The current heart count.
     */
    public int getHeart() {
        return heart;
    }
    /**
     * Sets the heart count in the game to the specified value.
     *
     * @param heart The new heart count value.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }
    /**
     * Decrements the current heart count in the game by 1.
     */
    public void dreHeart() {
        this.heart--;
    }
    /**
     * Returns the list of GameBlock instances representing the blocks in the game.
     *
     * @return The list of GameBlock instances.
     */
    public ArrayList<GameBlock> getBlocks() {
        return blocks;
    }
    /**
     * Returns the list of PenaltyBlock instances representing the bombs in the game.
     *
     * @return The list of PenaltyBlock instances.
     */
    public ArrayList<PenaltyBlock> getBombs() {
        return bombs;
    }
    /**
     * Returns the list of BonusBlock instances representing the choco blocks in the game.
     *
     * @return The list of BonusBlock instances.
     */
    public ArrayList<BonusBlock> getChocos() {
        return chocos;
    }
    /**
     * Sets the flag indicating a collision with the top block.
     *
     * @param colideToTopBlock The new value of the flag.
     */
    public void setColideToTopBlock(boolean colideToTopBlock) {
        this.colideToTopBlock = colideToTopBlock;
    }
    /**
     * Sets the flag indicating a collision with the right wall.
     *
     * @param colideToRightWall The new value of the flag.
     */
    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }
    /**
     * Sets the flag indicating a collision with a block on the right side.
     *
     * @param colideToRightBlock The new value of the flag.
     */
    public void setColideToRightBlock(boolean colideToRightBlock) {
        this.colideToRightBlock = colideToRightBlock;
    }
    /**
     * Sets the flag indicating a collision with the left wall.
     *
     * @param colideToLeftWall The new value of the flag.
     */
    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }
    /**
     * Sets the flag indicating a collision with a block on the left side.
     *
     * @param colideToLeftBlock The new value of the flag.
     */
    public void setColideToLeftBlock(boolean colideToLeftBlock) {
        this.colideToLeftBlock = colideToLeftBlock;
    }
    /**
     * Sets the flag indicating a collision with the paddle and movement to the right.
     *
     * @param colideToBreakAndMoveToRight The new value of the flag.
     */
    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }
    /**
     * Sets the flag indicating a collision with the paddle.
     *
     * @param colideToBreak The new value of the flag.
     */
    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }
    /**
     * Sets the flag indicating a collision with a block at the bottom.
     *
     * @param colideToBottomBlock The new value of the flag.
     */
    public void setColideToBottomBlock(boolean colideToBottomBlock) {
        this.colideToBottomBlock = colideToBottomBlock;
    }
    /**
     * Sets the horizontal velocity of the game ball.
     *
     * @param vX The new horizontal velocity.
     */
    public void setvX(double vX) {
        this.vX = vX;
    }
    /**
     * Sets the flag indicating the existence of heart blocks in the game.
     *
     * @param existHeartBlock The new value of the flag.
     */
    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }
    /**
     * Sets the flag indicating the gold status in the game.
     *
     * @param goldStauts The new value of the flag.
     */
    public void setGoldStauts(boolean goldStauts) {
        isGoldStauts = goldStauts;
    }
    /**
     * Sets the time when the gold status was activated in the game.
     *
     * @param goldTime The new gold time value.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }
    /**
     * Sets the current time in the game.
     *
     * @param time The new time value.
     */
    public void setTime(long time) {
        this.time = time;
    }
    /**
     * Checks if there is a collision with the top block.
     *
     * @return True if there is a collision with the top block, false otherwise.
     */
    public boolean isColideToTopBlock() {
        return colideToTopBlock;
    }
    /**
     * Checks if there is a collision with the right wall.
     *
     * @return True if there is a collision with the right wall, false otherwise.
     */
    public boolean isColideToRightWall() {
        return colideToRightWall;
    }
    /**
     * Checks if there is a collision with a block on the right side.
     *
     * @return True if there is a collision with a block on the right side, false otherwise.
     */
    public boolean isColideToRightBlock() {
        return colideToRightBlock;
    }
    /**
     * Checks if there is a collision with the left wall.
     *
     * @return True if there is a collision with the left wall, false otherwise.
     */
    public boolean isColideToLeftWall() {
        return colideToLeftWall;
    }
    /**
     * Checks if there is a collision with a block on the left side.
     *
     * @return True if there is a collision with a block on the left side, false otherwise.
     */
    public boolean isColideToLeftBlock() {
        return colideToLeftBlock;
    }
    /**
     * Checks if there is a collision with the paddle and movement to the right.
     *
     * @return True if there is a collision with the paddle and movement to the right, false otherwise.
     */
    public boolean isColideToBreakAndMoveToRight() {
        return colideToBreakAndMoveToRight;
    }
    /**
     * Checks if there is a collision with the paddle.
     *
     * @return True if there is a collision with the paddle, false otherwise.
     */
    public boolean isColideToBreak() {
        return colideToBreak;
    }
    /**
     * Checks if there is a collision with a block at the bottom.
     *
     * @return True if there is a collision with a block at the bottom, false otherwise.
     */
    public boolean isColideToBottomBlock() {
        return colideToBottomBlock;
    }
    /**
     * Checks if there is an existence of heart blocks in the game.
     *
     * @return True if there is an existence of heart blocks, false otherwise.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }
    /**
     * Checks if the gold status is activated in the game.
     *
     * @return True if the gold status is activated, false otherwise.
     */
    public boolean isGoldStauts() {
        return isGoldStauts;
    }
    /**
     * Returns the current time in the game.
     *
     * @return The current time.
     */
    public long getTime() {
        return time;
    }
    /**
     * Returns the time when the gold status was activated in the game.
     *
     * @return The gold time.
     */
    public long getGoldTime() {
        return goldTime;
    }
    /**
     * Returns the horizontal velocity of the game ball.
     *
     * @return The horizontal velocity.
     */
    public double getvX() {
        return vX;
    }
    /**
     * Returns the constant representing the left direction.
     *
     * @return The constant representing the left direction.
     */
    public static int getLEFT() {
        return LEFT;
    }
    /**
     * Returns the constant representing the right direction.
     *
     * @return The constant representing the right direction.
     */
    public static int getRIGHT() {
        return RIGHT;
    }
    /**
     * Returns the array of colors used in the game.
     *
     * @return The array of colors.
     */
    public Color[] getColors() {
        return colors;
    }
    /**
     * Returns the initial block count in the game.
     *
     * @return The initial block count.
     */
    public int getInitialblockcount() {
        return initialblockcount;
    }
    /**
     * Sets the initial block count in the game to the specified value.
     *
     * @param initialblockcount The new initial block count value.
     */
    public void setInitialblockcount(int initialblockcount) {
        this.initialblockcount = initialblockcount;
    }
    /**
     * Gets the count of destroyed blocks in the game.
     *
     * @return The count of destroyed blocks.
     */
    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }
    /**
     * Sets the count of destroyed blocks in the game.
     *
     * @param destroyedBlockCount The count of destroyed blocks to set.
     */
    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }
    /**
     * Increments the count of destroyed blocks in the game.
     */
    public void incDestroyedBlockCount(){this.destroyedBlockCount++;}
    /**
     * Gets the instance of the LoadSave class associated with the game.
     *
     * @return The LoadSave instance.
     */
    public LoadSave getLoadSave() {
        return loadSave;
    }
}