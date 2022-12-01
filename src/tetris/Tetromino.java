package tetris;

import java.util.Random;

public class Tetromino {
    protected enum Tetrominoes {
        EmptyShape, Sshape, Zshape, Tshape, Lshape, Jshape, Ishape, Oshape
    }

    private Tetrominoes piece = Tetrominoes.EmptyShape;
    private final int[][] coordinates;

    /**
     * Tetromino konstruktora
     * Létrehozzuk az alakzatunk koordinátáit
     */
    public Tetromino() {
        coordinates = new int[4][2];
        setRandomShape();
    }

    /**
     * Setter
     * Alakzatok alapkoordinátáit állítjuk be
     * @param _shape a tetrominókat tároló enumeráció
     */
    public void setShape(Tetrominoes _shape) {
        int[][][] coordinate_table = new int[][][]
                {
                        {{0,0}, {0,0}, {0,0}, {0,0}}, //EmptyPiece
                        {{0,-1}, {0,0}, {-1,0}, {-1,1}}, //SPiece
                        {{0,-1}, {0,0}, {1,0}, {1,1}}, //ZPiece
                        {{0,-1}, {0,0}, {-1,0}, {1,0}}, //TPiece
                        {{1,-1}, {0,-1}, {0,0},{0,1}}, //LPiece
                        {{-1,-1}, {0,-1}, {0,0}, {0,1}}, //JPiece
                        {{0,-1}, {0,0}, {0,1}, {0,2}}, //IPiece
                        {{1,0}, {0,0}, {0,1}, {1,1}}, //OPiece
                };

        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(coordinate_table[piece.ordinal()], 0, coordinates, 0, 4);
        }
        piece = _shape;
    }

    /**
     * Setter
     * @param _index megmondja, hányadik pont koordinátáját kell állítani
     * @param _x a beállítandó érték
     */
    private void setX(int _index, int _x) {
        coordinates[_index][0] = _x;
    }

    /**
     * Setter
     * @param _index megmondja, hányadik pont koordinátáját kell állítani
     * @param _y a beállítandó érték
     */
    private void setY(int _index, int _y) {
        coordinates[_index][1] = _y;
    }

    /**
     * Getter
     * @param _index megadja, hogy hányadik pont x koordinátáját adja vissza
     * @return visszaadja a kért ponthoz tartozó x koordinátát
     */
    public int getX(int _index) {
        return coordinates[_index][0];
    }

    /**
     * Getter
     * @param _index megadja, hogy hányadik pont y koordinátáját adja vissza
     * @return visszaadja a kért ponthoz tartozó y koordinátát
     */
    public int getY(int _index) {
        return coordinates[_index][1];
    }

    /**
     * Getter
     * @return visszaad egy tetrominót
     */
    public Tetrominoes getShape() {
        return piece;
    }

    /**
     * Véletlenszerűen beállítja a következő alakzatot
     */
    public void setRandomShape()
    {
        var rand = new Random();
        int rand_num = Math.abs(rand.nextInt()) % 7 + 1;

        Tetrominoes[] values = Tetrominoes.values();
        while(values[rand_num] == Tetrominoes.EmptyShape)
            rand_num = Math.abs(rand.nextInt()) % 7 + 1;
        setShape(values[rand_num]);
    }

    /**
     * @return Adott alakzat legkisebb y koordinátáját adja vissza
     */
    public int minY()
    {
        int min = coordinates[0][1];

        for(int i = 0; i < 2; i++)
        {
            min = Math.min(min, coordinates[i][1]);
        }

        return min;
    }

    /**
     * Balra 90 fokban forgatja az alakzatot
     * @return az elforgatott alakzat
     */
    public Tetromino rotateLeft()
    {
        Tetromino result = new Tetromino();
        result.piece = piece;

        for(int i = 0; i < 4; i++)
        {
            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }

        return result;
    }

    /**
     * Jobbra 90 fokban elforgatja az alakzatot
     * @return az elforgatott alakzat
     */
    public Tetromino rotateRight()
    {
        Tetromino result = new Tetromino();
        result.piece = piece;

        for(int i = 0; i < 4; i++)
        {
            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }

        return result;
    }
}
