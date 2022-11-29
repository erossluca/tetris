package tetris;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TetrominoTest
{
    Tetromino piece;

    @Before
    public void setup()
    {
        piece = new Tetromino();
    }

    @Test
    public void rotateRightTest()
    {
        int originalX = piece.getX(0);
        int originalY = piece.getY(0);

        piece.rotateRight();

        int newX = piece.getX(0);
        int newY = piece.getY(0);

        Assert.assertEquals(-originalY, newX);
        Assert.assertEquals(originalX, newY);
    }

    @Test
    public void rotateLeftTest()
    {
        int originalX = piece.getX(0);
        int originalY = piece.getY(0);

        piece.rotateLeft();

        int newX = piece.getX(0);
        int newY = piece.getY(0);

        Assert.assertEquals(originalY, newX);
        Assert.assertEquals(-originalX, newY);
    }
}
