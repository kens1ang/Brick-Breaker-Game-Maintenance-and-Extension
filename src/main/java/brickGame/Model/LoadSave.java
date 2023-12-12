package brickGame.Model;

import brickGame.Main;
import brickGame.Model.GameElements.BlockSerializable;
import brickGame.Model.GameElements.GameBlock;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This class represents the functionality for loading and saving game data.
 */
public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean          isGoldStauts;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean          colideToBreak;
    public boolean          colideToBreakAndMoveToRight;
    public boolean          colideToRightWall;
    public boolean          colideToLeftWall;
    public boolean          colideToRightBlock;
    public boolean          colideToBottomBlock;
    public boolean          colideToLeftBlock;
    public boolean          colideToTopBlock;
    public int              level;
    public int              score;
    public int              heart;
    public int              destroyedBlockCount;
    public double           xBall;
    public double           yBall;
    public double           xBreak;
    public double           yBreak;
    public double           centerBreakX;
    public long             time;
    public long             goldTime;
    public double           vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir = "C:/save/";
    /**
     * Reads the saved game data from the file.
     */
    public void read() {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(savePath));

            // Read various game state parameters from the input stream
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStauts = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            colideToBreak = inputStream.readBoolean();
            colideToBreakAndMoveToRight = inputStream.readBoolean();
            colideToRightWall = inputStream.readBoolean();
            colideToLeftWall = inputStream.readBoolean();
            colideToRightBlock = inputStream.readBoolean();
            colideToBottomBlock = inputStream.readBoolean();
            colideToLeftBlock = inputStream.readBoolean();
            colideToTopBlock = inputStream.readBoolean();

            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "IOException occurred in ArrayList<BlockSerializable>)", e);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException occurred in read", e);
        }
    }

    /**
     * Saves the current state of the game, including the level, score, ball position, paddle position, and block information.
     * This method runs in a separate thread to avoid blocking the main application.
     *
     * @param model The GameModel containing the current game state.
     */
    public void savegamedata(GameModel model) {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {

                outputStream.writeInt(model.getLevel());
                outputStream.writeInt(model.getScore());
                outputStream.writeInt(model.getHeart());
                outputStream.writeInt(model.getDestroyedBlockCount());
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
                for (GameBlock block : model.getBlocks()) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    int colorIndex = model.getColorIndex(block.getColor(), model.getColors());
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type, colorIndex));
                }

                outputStream.writeObject(blockSerializables);

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "IOException occurred in saveGame", e);
            }
        }).start();
    }

    /**
     * Loads a saved game state, including the level, score, ball position, paddle position, and block information.
     * Initializes the game with the loaded state.
     *
     * @param model The GameModel to load the game state into.
     */
    public void loadgamedata(GameModel model) {

        read();
        model.setExistHeartBlock(isExistHeartBlock);
        model.setGoldStauts(isGoldStauts);
        model.getBallob().setGoDownBall(goDownBall);
        model.getBallob().setGoRightBall(goRightBall);
        model.setColideToBreak(colideToBreak);
        model.setColideToBreakAndMoveToRight(colideToBreakAndMoveToRight);
        model.setColideToRightWall(colideToRightWall);
        model.setColideToLeftWall(colideToLeftWall);
        model.setColideToRightBlock(colideToRightBlock);
        model.setColideToBottomBlock(colideToBottomBlock);
        model.setColideToLeftBlock(colideToLeftBlock);
        model.setColideToTopBlock(colideToTopBlock);
        model.setLevel(level);
        model.setScore(score);
        model.setHeart(heart);
        model.setDestroyedBlockCount(0);
        model.getBallob().setxBall(xBall);
        model.getBallob().setyBall(yBall);
        model.getPaddle().setxBreak(xBreak);
        model.getPaddle().setyBreak(yBreak);
        model.getPaddle().setCenterBreakX(centerBreakX);
        model.setTime(time);
        model.setGoldTime(goldTime);
        model.setvX(vX);
        model.getBlocks().clear();
        model.getChocos().clear();
        model.getBombs().clear();

        for (BlockSerializable ser : blocks) {
            int colorIndex = ser.colorIndex;
            model.getBlocks().add(new GameBlock(ser.row, ser.j, model.getColors()[colorIndex], ser.type));
        }
        model.setInitialblockcount(blocks.size());
    }
}
