package brickGame.Model.GameElements;

import java.io.Serializable;

/**
 * Represents a serializable block in the game.
 * This class is used for serialization purposes.
 */
public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    public final int type;
    public final int colorIndex;
    /**
     * Constructs a serializable game block with specified row, column, type, and color index.
     *
     * @param row       The row of the block.
     * @param j         The column of the block.
     * @param type      The type of the block.
     * @param colorIndex The index of the color in the colors array.
     */
    public BlockSerializable(int row , int j , int type, int colorIndex) {
        this.row = row;
        this.j = j;
        this.type = type;
        this.colorIndex = colorIndex;
    }
}
