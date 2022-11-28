package tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame
{
    private JLabel status;

    /**
     * Tetris konstruktora
     */
    public Tetris()
    {
        initUI();
    }

    /**
     * Inicializálja a játék ablakát
     */
    public void initUI()
    {
        status = new JLabel(" 0");
        add(status, BorderLayout.SOUTH);

        Board board = new Board(this);
        add(board);
        board.startGame();

        setTitle("TETRIS");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Getter
     * @return visszaadja a status attribútum értékét
     */
    public JLabel getStatus()
    {
        return status;
    }

    /**
     * Elindítja a játékot egy új ablakban
     */
    public void startGame()
    {
        EventQueue.invokeLater(() -> {Tetris game = new Tetris(); game.setVisible(true);});
    }
}
