package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Menu extends JFrame
{
    private static final long serialVersionUID = 1L;

    private final String gameDescription = "howToPlay.txt";
    private final String topListTxt = "topListTxt.txt";

    private static JPanel titlePanel;
    private static JPanel buttonPanel;

    private static JPanel topListPanel;
    private static JPanel topListTextPanel;
    private static JPanel topListButtonPanel;
    private static JPanel manualPanel;
    private static JPanel manualPanelText;
    private static JPanel manualButtonPanel;

    private Tetris game = new Tetris();

    private JTextArea descText;
    private JTextArea currScore;
    private JTextArea emptyScoreTxt;

    private JButton startButton = new JButton("Játék indítása");
    private JButton manualButton = new JButton("Játék menete");
    private JButton topListButton = new JButton("Ranglista");
    private JButton exitButton = new JButton("Kilépés");
    private JButton backToMainMenuButton = new JButton("Vissza a menübe");

    /**
     * A főmenü konstruktora
     * Hozzáad egy TETRIS feliratot megjelenítő panelt, valamint egy, a gombokat tartalmazó panelt
     */
    public Menu()
    {
        getContentPane().setBackground(Color.pink);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        setVisible(true);
        setSize(450, 550);

        titlePanel = new JPanel();
        JLabel titlePanelLabel = new JLabel("TETRIS");
        titlePanel.setBackground(Color.pink);
        titlePanel.setBounds(50, 100, 350, 100);
        titlePanelLabel.setForeground(Color.DARK_GRAY);
        titlePanelLabel.setFont(new Font("Arial", Font.BOLD, 40));

        titlePanel.add(titlePanelLabel);

        add(titlePanel);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.pink);
        buttonPanel.setBounds(125, 200, 200, 220);
        buttonPanel.add(startButton);
        buttonPanel.add(manualButton);
        buttonPanel.add(topListButton);
        buttonPanel.add(exitButton);

        add(buttonPanel);

        startButton.addActionListener(new startButtonActionListener());
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.LIGHT_GRAY);
        startButton.setFont(new Font("Arial", Font.PLAIN, 25));

        manualButton.addActionListener(new manualActionListener());
        manualButton.setBackground(Color.DARK_GRAY);
        manualButton.setForeground(Color.LIGHT_GRAY);
        manualButton.setFont(new Font("Arial", Font.PLAIN, 25));

        topListButton.addActionListener(new topListActionListener());
        topListButton.setBackground(Color.DARK_GRAY);
        topListButton.setForeground(Color.LIGHT_GRAY);
        topListButton.setFont(new Font("Arial", Font.PLAIN, 25));

        exitButton.addActionListener(new exitActionListener());
        exitButton.setBackground(Color.DARK_GRAY);
        exitButton.setForeground(Color.LIGHT_GRAY);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 25));

        topListReading();
    }

    /**
     * Elkészíti a "Játék indítása" ponthoz szükséges handler-t
     */
    final class startButtonActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(false);
            buttonPanel.setVisible(false);
            game = new Tetris();
            game.startGame();
        }
    }

    /**
     * Elkészíti a "Játék menete" gombhoz tartozó handler-t
     */
    final class manualActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(false);
            buttonPanel.setVisible(false);

            createManualWindow();

            manualPanel.setVisible(true);
        }
    }

    /**
     * Megjeleníti a "Játék menete" gombhoz tartozó képernyőt
     * Hozzádad 3 panelt, az első a "Játék menete" szöveget tartalmazza
     * A második a felhasználói kézikönyvet írja ki
     * A harmadik pedig a "Vissza a menübe" gombot tartalmazza
     */
    public void createManualWindow()
    {
        getContentPane().setBackground(Color.pink);
        setLayout(null);
        manualPanel = new JPanel();
        manualPanel.setBounds(50,30,340,30);
        manualPanel.setBackground(Color.pink);
        JLabel howToPlay = new JLabel("Játék menete");
        howToPlay.setForeground(Color.DARK_GRAY);
        howToPlay.setFont(new Font("Arial", Font.BOLD, 25));
        manualPanel.add(howToPlay);

        manualPanelText = new JPanel();
        manualPanelText.setBounds(10,70,400,350);

        descText = new JTextArea(loadManual(gameDescription));
        descText.setEditable(false);

        manualPanelText.setBackground(Color.pink);
        descText.setBackground(Color.pink);
        descText.setForeground(Color.DARK_GRAY);
        manualPanelText.add(descText);

        manualButtonPanel = new JPanel();
        manualButtonPanel.setBounds(125,430,200,40);
        manualButtonPanel.setBackground(Color.pink);
        backToMainMenuButton.addActionListener(new backToMainMenuFromManual());
        backToMainMenuButton.setBackground(Color.pink);
        backToMainMenuButton.setForeground(Color.DARK_GRAY);

        manualButtonPanel.add(backToMainMenuButton);
        add(manualPanel);
        add(manualPanelText);
        add(manualButtonPanel);

    }

    /**
     * Elkészíti a "Játék menete" képernyőhöz tartozó "Vissza a menübe" gomb handler-ét
     */
    final class backToMainMenuFromManual implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(true);
            buttonPanel.setVisible(true);

            manualPanel.setVisible(false);
            manualButtonPanel.setVisible(false);
            manualPanelText.setVisible(false);
        }
    }

    /**
     * Elkészíti a "Ranglista" ponthoz tartozó képernyőn lévő "Vissza a menübe" gomb handler-ét
     */
    final class backToMainMenuFromTopList implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(true);
            buttonPanel.setVisible(true);

            topListPanel.setVisible(false);
            topListTextPanel.setVisible(false);
            topListButtonPanel.setVisible(false);
        }
    }

    /**
     * Beolvassa fájlból a felhasználói kézikönyvet
     * @param fileName a beolvasandó fájl neve
     * @return visszatér a beolvasott fájllal
     */
    public String loadManual(String fileName)
    {
        try
        {
            String howToPlay = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            return howToPlay;
        }
        catch(FileNotFoundException e) {return "A fájl nem található";}
    }

    /**
     * Elkészíti a "Ranglista" gombhoz tartozó képernyőt
     * Hozzáad 3 panelt, az első a "Ranglista" feliratot jeleníti meg
     * A második magát a ranglistát tartalmazza
     * A harmadik pedig a "Vissza a menübe" gombot
     * @param scores átadja azt a listát, amibe beolvasásra kerültek a ranglista bejegyzései
     */
    public void createTopListWindow(ArrayList<Scores> scores)
    {
        getContentPane().setBackground(Color.pink);
        setLayout(null);
        topListPanel = new JPanel();
        topListPanel.setBounds(60, 80, 300, 100);
        topListPanel.setBackground(Color.pink);

        JLabel topListLabel = new JLabel("Ranglista");
        topListLabel.setForeground(Color.DARK_GRAY);
        topListLabel.setFont(new Font("Arial", Font.BOLD, 35));
        topListPanel.add(topListLabel);

        topListTextPanel = new JPanel();
        topListTextPanel.setBounds(10,150,400,350);
        topListTextPanel.setBackground(Color.pink);

        if(scores != null)
        {
            for(int i = 0; i < scores.size(); i++)
            {
                int place = scores.get(i).getPlace();
                String name = scores.get(i).getName();
                int score = scores.get(i).getScore();

                currScore = new JTextArea(place + " " + name + " " + score);
                currScore.setBackground(Color.pink);
                currScore.setForeground(Color.DARK_GRAY);
                topListTextPanel.add(currScore);
            }
        }
        else
        {
            emptyScoreTxt = new JTextArea("A ranglista üres");
            emptyScoreTxt.setBackground(Color.pink);
            emptyScoreTxt.setForeground(Color.DARK_GRAY);
            topListTextPanel.add(emptyScoreTxt);
        }

        topListButtonPanel = new JPanel();
        topListButtonPanel.setBounds(125,430,200,40);
        topListButtonPanel.setBackground(Color.pink);
        backToMainMenuButton.addActionListener(new backToMainMenuFromTopList());
        backToMainMenuButton.setBackground(Color.pink);
        backToMainMenuButton.setForeground(Color.DARK_GRAY);

        topListButtonPanel.add(backToMainMenuButton);
        add(topListPanel);
        add(topListTextPanel);
        add(topListButtonPanel);
    }

    /**
     * A "Ranglista" menüponthoz tartozó handler-t valósítja meg
     */
    final class topListActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(false);
            buttonPanel.setVisible(false);

            topListReading();
            createTopListWindow(Board.scores);

            topListPanel.setVisible(true);
            topListTextPanel.setVisible(true);
            topListButtonPanel.setVisible(true);

        }
    }

    /**
     * Beolvassa fájlból a ranglistát
    */
    public void topListReading()
    {
        try
        {
            FileInputStream fs = new FileInputStream("topListTxt.txt");
            ObjectInputStream ois = new ObjectInputStream(fs);
            Board.scores = (ArrayList) ois.readObject();
            Collections.sort(Board.scores, Scores.compareByScores());
            ois.close();
            fs.close();
        }
        catch(FileNotFoundException e){}
        catch(IOException | ClassNotFoundException e){}

    }

    /**
     * Elkészíti a "Kilépés" gombhoz tartozó handler-t
     */
    final class exitActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
}
