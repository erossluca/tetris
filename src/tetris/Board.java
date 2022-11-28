package tetris;

import javax.swing.*;
import tetris.Tetromino.Tetrominoes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class Board extends JPanel {

    private static final long serialVersionUID = 1L;

    private final int BOARD_WIDTH = 15;
    private final int BOARD_HEIGHT = 15;
    private final int SPEED = 10000;

    private Timer timer;
    private JLabel status;
    private Tetromino current_piece;
    private int current_x, current_y = 0;
    private Tetrominoes[] board;
    private boolean is_paused = false;
    private boolean fallen = false;
    private int removed_lines = 0;

    public static ArrayList<Scores> scores = new ArrayList<>(); //nemszabaaad

    private boolean gameOver = false;
    private Tetris parent;

    /**
     * Játéktábla konstruktora
     *
     * @param _parent Tetris objektum
     */
    public Board(Tetris _parent) {
        timer = new Timer(1000, new GameCycle() {
        });
        timer.start();
        parent = _parent;
        initBoard(_parent);
    }

    /**
     * Játéktábla inicializálása
     *
     * @param _parent Tetris objektum
     */
    public void initBoard(Tetris _parent) {
        setFocusable(true);
        status = _parent.getStatus();
        addKeyListener(new TetrisAdapter());
    }

    /**
     * Adott koordinátákon az alakzat visszaadása
     *
     * @param _x x koordináta
     * @param _y y koordináta
     * @return visszaadja egy Tetrominoes tömbben az alakzatot
     */
    public Tetrominoes shapePlace(int _x, int _y) {
        return board[(_y * BOARD_WIDTH) + _x];
    }

    /**
     * Meghatározza egy darab négyzet szélességét
     *
     * @return
     */
    private int squareWidth() {

        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**
     * meghatározza egy darab négyzet magasságát
     *
     * @return
     */
    private int squareHeight() {

        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     * Elindítja a játékot
     */
    public void startGame() {
        current_piece = new Tetromino();
        board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
        newPiece();

        timer = new Timer(SPEED, null); //???
        timer.start();
    }

    /**
     * Szünetelteti a játékot, de egyelőre csak elméletben TODO
     */
    public void pauseGame() {
        is_paused = !is_paused;
        if (is_paused)
            status.setText("paused");
        else
            status.setText(String.valueOf(removed_lines));

        repaint();
    }

    /**
     * Egy sorral lejjebb helyezi az alakzatot, ha a következő sorban még szabad a hely
     */
    private void oneLineDown() {
        if (!tryMove(current_piece, current_x, current_y - 1))
            pieceDropped();
    }

    /**
     * A lehető legalacsonyabb helyre teszi az alakzatot, ahol még nincs másik
     */
    private void dropDown() {
        int temp_y = current_y;

        while (temp_y > 0) {
            if (!tryMove(current_piece, current_x, temp_y - 1))
                break;

            temp_y--;
        }
        pieceDropped();
    }

    /**
     * Amikor leér az alakzat, a függvény megnézi, lett-e teli sor, amennyiben igen, törli azt
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = current_x + current_piece.getX(i);
            int y = current_y - current_piece.getY(i);
            board[(y * BOARD_WIDTH) + x] = current_piece.getShape();
        }

        removeLine();

        if (!fallen)
            newPiece();
    }

    /**
     * Törli a játéktábla teljes tartalmát
     */
    public void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoes.Empty;
        }
    }

    /**
     * Végignézi, vannak-e teli sorok, és ha igen, törli azokat
     * Eközben végig számolja a törölt sorokat, ez fogja adni a pontszámot
     */
    public void removeLine() {
        int no_full_lines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean full_line = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapePlace(j, i) == Tetrominoes.Empty) {
                    full_line = false;
                    break;
                }
            }

            if (full_line) {
                no_full_lines++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int l = 0; l < BOARD_WIDTH; l++) {
                        board[(k * BOARD_WIDTH) + l] = shapePlace(l, k + 1);
                    }
                }
            }
        }

        if (no_full_lines > 0) {
            removed_lines += no_full_lines;
            fallen = true;
            status.setText(String.valueOf(removed_lines));
            current_piece.setShape(Tetrominoes.Empty);

        }
    }

    /**
     * Létrehozza random generálva a következő alakzatot és elhelyzi a tábla tetején
     * Ezután megnézi, hogy az alakzattól betelt-e a tábla
     */
    public void newPiece() {
        current_piece.setRandomShape();
        current_x = BOARD_WIDTH / 2 + 1;
        current_y = BOARD_HEIGHT - 1 + current_piece.minY();

        if (!tryMove(current_piece, current_x, current_y)) {
            current_piece.setShape(Tetrominoes.Empty);
            timer.stop();
            gameOver = true;
            String message = "Game over. Score: " + removed_lines;
            status.setText(message);
        }
    }

    /**
     * Amennyiben lehet mozgatni az alakzatot a kívánt pozícióba, elforgatja
     *
     * @param _new   az aktuális alakzat
     * @param _new_x az alakzat új x koordinátája
     * @param _new_y az alakzat új y koordinátája
     * @return visszaadja, hogy sikerült-e elmozgatni az alakjatot
     */
    public boolean tryMove(Tetromino _new, int _new_x, int _new_y) {
        for (int i = 0; i < 4; i++) {
            int x = _new_x + _new.getX(i);
            int y = _new_y - _new.getY(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;

            if (shapePlace(x, y) != Tetrominoes.Empty)
                return false;
        }

        current_piece = _new;
        current_x = _new_x;
        current_y = _new_y;

        repaint();
        return true;
    }

    /**
     * Az alakzatokat rajzolja ki a táblára, ha vége a játéknak, bezárja az ablakot
     * Visszatér a főmenübe
     *
     * @param _g
     */
    private void draw(Graphics _g) {
        var size = getSize();
        int board_top = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoes piece = shapePlace(j, BOARD_HEIGHT - i - 1);

                if (piece != Tetrominoes.Empty)
                    drawSquare(_g, j * squareWidth(), board_top + i * squareHeight(), piece);
            }
        }

        if (current_piece.getShape() != Tetrominoes.Empty) {
            for (int i = 0; i < 4; i++) {
                int x = current_x + current_piece.getX(i);
                int y = current_y - current_piece.getY(i);

                drawSquare(_g, x * squareWidth(), board_top + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        current_piece.getShape());
            }
        }

        if(gameOver == true)
        {
            saveTopList();
            parent.dispose();
            new Menu();
        }
    }

    /**
     * Megrajzolja a játék táblát
     *
     * @param _g
     */
    public void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        draw(_g);
    }

    /**
     * Alakzatok színeit állítja
     * Kirajzolja az alakzatokat négyzetekből felépítve
     *
     * @param _g
     * @param _x     x koordináta
     * @param _y     y koordináta
     * @param _shape aktuális alakzat
     */
    private void drawSquare(Graphics _g, int _x, int _y, Tetrominoes _shape) {
        Color colors[] =
                {new Color(0, 0, 0),
                        new Color(204, 102, 102),
                        new Color(102, 204, 102),
                        new Color(102, 102, 204),
                        new Color(204, 204, 102),
                        new Color(204, 102, 204),
                        new Color(102, 204, 204),
                        new Color(218, 170, 0)
                };

        var color = colors[_shape.ordinal()];

        _g.setColor(color);
        _g.fillRect(_x + 1, _y + 1, squareWidth() - 2, squareHeight() - 2);

        _g.setColor(color.brighter());
        _g.drawLine(_x, _y + squareHeight() - 1, _x, _y);
        _g.drawLine(_x, _y, _x + squareWidth() - 1, _y);

        _g.setColor(color.darker());
        _g.drawLine(_x + 1, _y + squareHeight() - 1,
                _x + squareWidth() - 1, _y + squareHeight() - 1);
        _g.drawLine(_x + squareWidth() - 1, _y + squareHeight() - 1,
                _x + squareWidth() - 1, _y + 1);
    }


    private class GameCycle implements ActionListener {

        public void actionPerformed(ActionEvent e)
        {
            gameCycle();
        }

        /**
         * Frissíti és újrarajzolja a játékot
         */
        private void gameCycle() {
            if (is_paused == false) {
                update();
                repaint();
            }

        }

        /**
         * Frissíti a játéktáblát
         * Amikor leesett az alakzat, újat kér, addig pedig egy sorral folyamatosan lejjebb helyezi
         */
        private void update() {
            if (is_paused == true)
                return;

            if (fallen) {
                fallen = false;
                newPiece();
            } else
                oneLineDown();
        }
    }

    /**
     * Kimenti fájlba a ranglistát
    */
    public void saveTopList()
    {
        try
        {
            FileOutputStream fs = new FileOutputStream("topListTxt.txt");
            ObjectOutputStream ous = new ObjectOutputStream(fs);
            Scores newScore = new Scores(scores.size()+1, "teszt", removed_lines);
            scores.add(newScore);
            System.out.println(newScore.getPlace() + newScore.getName() + newScore.getScore());
            ous.writeObject(scores);
            ous.close();
            fs.close();
        }
        catch(IOException e){}
    }

    /**
     * A TetrisAdapter osztály a játék billentyűzetekkel való irányítására szolgál
     * Adott billentyűlenyomásra meghívja a mozgatáshoz tartozó függvényt
     */
    private class TetrisAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if(current_piece.getShape() == Tetrominoes.Empty)
                return;

            int key_code = e.getKeyCode();

            switch(key_code)
            {
                case KeyEvent.VK_ESCAPE -> pauseGame();
                case KeyEvent.VK_LEFT -> tryMove(current_piece, current_x - 1, current_y);
                case KeyEvent.VK_RIGHT -> tryMove(current_piece, current_x + 1, current_y);
                case KeyEvent.VK_UP -> tryMove(current_piece.rotateRight(), current_x, current_y);
                case KeyEvent.VK_DOWN -> tryMove(current_piece.rotateLeft(), current_x, current_y);
                case KeyEvent.VK_SPACE -> dropDown();
                case KeyEvent.VK_D -> oneLineDown();
            }
        }
    }
}