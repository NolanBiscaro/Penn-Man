/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */

public class Game implements Runnable {
    
    private static String user = "";
    private static Map<String, Integer> records = new TreeMap<String, Integer>();
    private static final String FILENAME = "files/storedData";
    private boolean startGame = false;
    private boolean showLeaderboard = false;
    private static JLabel score = new JLabel(); 
    private static JLabel lives = new JLabel(); 

    public void run() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
            

        loadGame();
        
        final JFrame frame = new JFrame("PENN-MAN");
        final JFrame menu = new JFrame("Main Menu");
        final JFrame leaderboard = new JFrame("Leaderboard");

        frame.setLocation(500, 100);
        frame.setSize(500, 100);
        frame.setResizable(false);

        menu.setLocation(512, 100);
        menu.setResizable(false);
        menu.setPreferredSize(new Dimension(512, 512));
        menu.getContentPane().setBackground(new Color(0, 0, 102));

        // menu layout
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setSize(new Dimension(512, 100));

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Penn-Man");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        final JPanel lbScrollContainer = new JPanel();
        lbScrollContainer.setLayout(new BoxLayout(lbScrollContainer, BoxLayout.PAGE_AXIS));
        lbScrollContainer.setBackground(Color.BLACK);
        lbScrollContainer.setSize(new Dimension(512, 512));
        buildLeaderboard(lbScrollContainer);

        // Leaderboard
        final JScrollPane lb = new JScrollPane(lbScrollContainer);

        lb.setBackground(Color.black);
        lb.setPreferredSize(new Dimension(512, 512));
        lb.setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED, 
                        Color.RED, Color.RED, Color.BLACK, Color.BLACK));

        lb.setVisible(true);
        lb.add(new JPanel());
        leaderboard.add(lb, BorderLayout.CENTER);

        // Scoreboard
        final JPanel scoreboard = new JPanel(new GridLayout());
        scoreboard.setBackground(Color.BLACK);
        scoreboard.setBorder((BorderFactory.createLineBorder(Color.RED)));

        // score
        score = new JLabel("SCORE: 0", JLabel.LEFT);
        score.setSize(100, 100);
        score.setForeground(Color.RED);
        score.setFont(new Font("Serif", Font.BOLD, 25));
        scoreboard.add(score);

        // lives
        lives = new JLabel("LIVES: 3", JLabel.RIGHT);
        lives.setSize(100, 100);
        lives.setForeground(Color.RED);  
        lives.setFont(new Font("Serif", Font.BOLD, 25));
        scoreboard.add(lives);

        scoreboard.setPreferredSize(new Dimension(50, 70));

        frame.add(scoreboard, BorderLayout.NORTH);

        final JButton newGameButton = new JButton("NEW GAME");
        newGameButton.setPreferredSize(new Dimension(256, 100));
        newGameButton.setBackground(Color.BLACK);
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
        newGameButton.setOpaque(true);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user = JOptionPane.showInputDialog("Please enter a username");
                if (user != null) {
                    user = user.replaceAll(" ", "_");
                    court.reset();
                    frame.setVisible(true);
                }

            }
        });

        final JButton leaderboardButton = new JButton("LEADERBOARD");
        leaderboardButton.setPreferredSize(new Dimension(256, 100));
        leaderboardButton.setBackground(Color.BLACK);
        leaderboardButton.setForeground(Color.WHITE);
        leaderboardButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
        leaderboardButton.setOpaque(true);
        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leaderboard.setVisible(true);
            }
        });

        final JButton closeLeaderboardButton = new JButton("CLOSE");
        closeLeaderboardButton.setPreferredSize(new Dimension(256, 100));
        closeLeaderboardButton.setBackground(Color.BLACK);
        closeLeaderboardButton.setForeground(Color.WHITE);
        closeLeaderboardButton.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.RED));
        closeLeaderboardButton.setOpaque(true);
        closeLeaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                leaderboard.setVisible(false);
            }
        });

        JPanel quitContainer = new JPanel();
        quitContainer.setBackground(Color.BLACK);
        quitContainer.add(closeLeaderboardButton, BorderLayout.SOUTH);
        leaderboard.add(quitContainer, BorderLayout.SOUTH);

        menuPanel.add(newGameButton, BorderLayout.WEST);
        menuPanel.add(leaderboardButton, BorderLayout.EAST);
        menuPanel.setBackground(Color.BLACK);
        menu.add(menuPanel, BorderLayout.SOUTH);
        
        JLabel title = new JLabel("PennMan"); 
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setForeground(Color.RED);
        title.setHorizontalAlignment(JLabel.CENTER);
        menu.add(title);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(startGame);

        // Put menu on screen
        menu.pack();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);

        // put leaderboard on screen
        leaderboard.pack();
        leaderboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        leaderboard.setVisible(showLeaderboard);

        // Start game

    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements
     * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
     * this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    public static void updateScore(int s) {
        score.setText("SCORE: " + Integer.toString(s));
    }

    public static void updateLives() {
        lives.setText("LIVES: " + Man.getLives());
    }

    public static void quit() {
        saveGame();
        System.exit(0);
    }

    private void loadGame() {
        try {
            String line;
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                String username = data[0];
                String score = data[1];
                records.put(username, Integer.parseInt(score));
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found when reading");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error when reading");
            e.printStackTrace();
        }
    }

    private void buildLeaderboard(JPanel container) {
        for (Map.Entry<String, Integer> entry : sortByVal(records)) {

            JLabel record = new JLabel("Username: " + 
                    entry.getKey() + " Score: " + entry.getValue());
            record.setForeground(Color.WHITE);
            record.setBorder(BorderFactory.createLineBorder(Color.RED));
            record.setBackground(Color.black);
            record.setSize(new Dimension(300, 200));
            record.setVisible(true);
            record.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            container.add(record);

        }
    }

    public static void addRecord() {
        records.put(user, Man.getScore());
    }
  
    public static void setUser(String u) {
        user = u; 
    }
    
    public static Map<String, Integer> getRecords() {
        return records; 
    }
    
    public static JLabel getScore() {
        return score; 
    }
    
    public static JLabel getLives() {
        return lives; 
    }

    private static void saveGame() {
        String data = "\r\n" + user + " " + Man.getScore();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("error writing to file");
            e.printStackTrace();
        }
    }

    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> 
        sortByVal(Map<K, V> records) {
        SortedSet<Map.Entry<K, V>> sorted = 
                new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
                    @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int answ = e2.getValue().compareTo(e1.getValue());
                        return answ != 0 ? answ : 1;
                    }
                });
        sorted.addAll(records.entrySet());
        return sorted;
    }

}