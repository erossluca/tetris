package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu extends JFrame
{

    private static final long serialVersionUID = 1L;

    private final String gameDescription = "howToPlay.txt";
    private String howToPlayTxt;
    private final String topListTxt = "topListTxt.txt";

    private static JPanel titlePanel;
    private static JPanel buttonPanel;

    private static JPanel topListPanel;
    private static JPanel topListTextPanel;
    private static JPanel topListButtonPanel;

    private static JPanel manualPanel;
    private static JPanel manualPanelText;
    private static JPanel manualButtonPanel;

    private final JButton startButton = new JButton("Játék indítása");
    private final JButton manualButton = new JButton("Játék menete");
    private final JButton topListButton = new JButton("Ranglista");
    private final JButton exitButton = new JButton("Kilépés");
    private final JButton backToMainMenuButton = new JButton("Vissza a menübe");

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

        topListReading(topListTxt);
    }

    /**
     * Elkészíti a "Játék indítása" ponthoz szükséges handler-t
     * Bekéri a játékos nevét, amennyiben nem lesz megadva név
     * vagy a játékos a Cancel gombot választotta, visszatér a
     * főmenübe
     */
    final class startButtonActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            titlePanel.setVisible(false);
            buttonPanel.setVisible(false);

            String name = JOptionPane.showInputDialog("Enter your name:");
            if((name != null) && name.length() > 0)
            {
            Scores newPlayer = new Scores(Board.getScoreList().size()+1, name, 0);
            Board.getScoreList().add(newPlayer);

            Tetris game = new Tetris();
            game.startGame();
            dispose();
            }
            else
            {
                titlePanel.setVisible(true);
                buttonPanel.setVisible(true);
            }
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

            try {
                createManualWindow();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

            manualPanel.setVisible(true);
        }
    }

    /**
     * Megjeleníti a "Játék menete" gombhoz tartozó képernyőt
     * Hozzádad 3 panelt, az első a "Játék menete" szöveget tartalmazza
     * A második a felhasználói kézikönyvet írja ki
     * A harmadik pedig a "Vissza a menübe" gombot tartalmazza
     */
    public void createManualWindow() throws FileNotFoundException
    {
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

        loadManual(gameDescription);
        JTextArea descText = new JTextArea(howToPlayTxt);
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

        manualButtonPanel.repaint();
    }

    /**
     * Elkészíti a "Játék menete" képernyőhöz tartozó "Vissza a menübe" gomb handler-ét
     */
    static final class backToMainMenuFromManual implements ActionListener
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
    static final class backToMainMenuFromTopList implements ActionListener
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
     */
    public void loadManual(String fileName) throws FileNotFoundException
    {
        howToPlayTxt = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
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
        setLayout(null);

        topListPanel = new JPanel();
        topListPanel.setBounds(60, 80, 300, 50);
        topListPanel.setBackground(Color.pink);

        JLabel topListLabel = new JLabel("Ranglista");
        topListLabel.setForeground(Color.DARK_GRAY);
        topListLabel.setFont(new Font("Arial", Font.BOLD, 35));
        topListPanel.add(topListLabel);

        topListTextPanel = new JPanel();
        topListTextPanel.setBounds(200,150,400,200);
        topListTextPanel.setBackground(Color.pink);


        if(!scores.isEmpty())
        {
            int no_of_loops = Math.min(scores.size(), 3);
            for(int i = 0; i < no_of_loops ; i++)
            {
                int place = scores.get(i).getPlace();
                String name = scores.get(i).getName();
                int score = scores.get(i).getScore();

                String scoreToShow = place + " " + name + " " + score + "\n";

                JTextArea currScore = new JTextArea(scoreToShow);
                currScore.setBackground(Color.pink);
                currScore.setForeground(Color.DARK_GRAY);
                topListTextPanel.add(currScore);

                BoxLayout layout = new BoxLayout(topListTextPanel, BoxLayout.Y_AXIS);
                topListTextPanel.setLayout(layout);
            }
        }
        else
        {
            JTextArea emptyScoreTxt = new JTextArea("A ranglista üres");
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

        topListButtonPanel.repaint();
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

            createTopListWindow(Board.getScoreList());

            topListPanel.setVisible(true);
        }
    }

    /**
     * Beolvassa fájlból a ranglistát
     * Amennyiben a fájl még nem létezik, létrehozza azt
    */
    public void topListReading(String _file)
    {
        try
        {
            File file = new File(_file);

            if (!file.exists())
                file.createNewFile();

            if (file.length() != 0) {

                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fs);
                Board.setScoreList((ArrayList) ois.readObject());
                Board.getScoreList().sort(Scores.compareByScores());
                ois.close();
                fs.close();
            }
        } catch (IOException | ClassNotFoundException fileNotFoundException) {fileNotFoundException.printStackTrace();}
    }


    /**
     * Elkészíti a "Kilépés" gombhoz tartozó handler-t
     */
    static final class exitActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }
}
