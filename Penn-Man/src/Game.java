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
	
	public static JLabel score = new JLabel(); 
	public static JLabel lives = new JLabel(); 
	public static String user = ""; 
	public static Map <String, Integer> records = new TreeMap<String, Integer>();
	private static final String filename = "files/storedData";
	private boolean startGame = false; 
	private boolean showLeaderboard = false; 
	public JList data = new JList(); 
	public static final JFrame lb = new JFrame("Leaderboard");
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your gam
    	loadGame(); 
    	
    	
        final JFrame frame = new JFrame("PENN-MAN");
        final JFrame menu = new JFrame("Main Menu");
         
        
       
        frame.setLocation(500, 100);
        frame.setResizable(false); 
        
        menu.setLocation(500, 100);
        menu.setResizable(false);
        menu.setPreferredSize(new Dimension(512, 300));
        
        //menu layout
        JPanel menuPanel = new JPanel(); 
        
        lb.setLocation(500, 100);
        lb.setResizable(false);
        lb.setPreferredSize(new Dimension(512, 240));

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
        buildLeaderboard(lbScrollContainer); 
        
        
      //Leaderboard
        final JScrollPane leaderboard = new JScrollPane(lbScrollContainer); 
        
        leaderboard.setBackground(Color.black);
        leaderboard.setPreferredSize(new Dimension(206, 206));
        leaderboard.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.RED, Color.BLACK, Color.BLACK));
       
        leaderboard.setVisible(true);
        leaderboard.add(new JPanel());
        lb.add(leaderboard, BorderLayout.CENTER);         
        //records
        
        

        //Scoreboard
        final JPanel scoreboard = new JPanel();
        scoreboard.setBackground(Color.BLACK);
        scoreboard.setBorder((BorderFactory.createLineBorder(Color.RED)));
        
       
        //score
        score = new JLabel("SCORE: 0", JLabel.LEFT); 
       // scoreboard.setLayout(new FlowLayout());
        score.setForeground(Color.WHITE);
        scoreboard.add(score);
        
        //lives
        lives = new JLabel("LIVES: 3", JLabel.RIGHT); 
        lives.setForeground(Color.WHITE);
        scoreboard.add(lives);
        
        scoreboard.setPreferredSize(new Dimension(50, 70));
        
        
        frame.add(scoreboard, BorderLayout.NORTH);
        

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        /*final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);*/ 
        //menuPanel.setLayout(new BorderLayout());
        final JButton newGameButton = new JButton("NEW GAME"); 
        newGameButton.setPreferredSize(new Dimension(200, 100));
        newGameButton.setForeground(Color.blue);
        newGameButton.setBackground(Color.red);
        newGameButton.setOpaque(true);
        newGameButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		user = JOptionPane.showInputDialog("Please enter a username");
        		if (user != null) {       			 
            		user = user.replaceAll(" ", "");
            		court.reset();
            		frame.setVisible(true);   			
        		} 
        		
        		
        	}
        });
        
        final JButton leaderboardButton = new JButton("LEADERBOARD"); 
        leaderboardButton.setPreferredSize(new Dimension(200, 100));
        leaderboardButton.setForeground(Color.blue);
        leaderboardButton.setBackground(Color.RED);
        leaderboardButton.setOpaque(true);
        leaderboardButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		lb.setVisible(true);
        	}
        });
        
        final JButton closeLeaderboardButton = new JButton("CLOSE"); 
        closeLeaderboardButton.setPreferredSize(new Dimension(200, 100));
        closeLeaderboardButton.setForeground(Color.RED);
        closeLeaderboardButton.setOpaque(true);
        closeLeaderboardButton.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent e) {
        		lb.setVisible(false);
        	}
        });
        
        JPanel quitContainer = new JPanel(); 
        quitContainer.add(closeLeaderboardButton, BorderLayout.SOUTH);
        lb.add(quitContainer, BorderLayout.SOUTH); 
        
        menuPanel.add(newGameButton, BorderLayout.SOUTH); 
        menuPanel.add(leaderboardButton, BorderLayout.PAGE_END); 
        menuPanel.setPreferredSize(new Dimension(512, 512));
        menuPanel.setBackground(Color.BLACK);
        menu.add(menuPanel); 
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(startGame);
        
        //Put menu on screen
        menu.pack();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
        
        //put leaderboard on screen
        lb.pack();
        lb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lb.setVisible(showLeaderboard);

        // Start game
        
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
    
    public static void updateScore(int s) {
    	 score.setText("SCORE: " + Integer.toString(s));
    }
    
    public static void updateLives() {
    	lives.setText("LIVES: " + Man.lives);
    }
    
    public static void quit() {
    	saveGame();  
    	System.exit(0);
    }
    
    private void loadGame() {
    	try {
    		String line; 
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			while((line = reader.readLine()) != null) {
				String[] data = line.split(" "); 
				String username = data[0]; 
				String score = data[1]; 
				records.put(username, Integer.parseInt(score));
			}
			
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
    		
    		JLabel record = new JLabel("Username: " + entry.getKey() + " Score: " + entry.getValue());
    		record.setForeground(Color.RED);
    		record.setBorder(BorderFactory.createDashedBorder(Color.RED));
    		record.setForeground(Color.black);
    		record.setPreferredSize(new Dimension(300, 200));
    		record.setVisible(true);
    		record.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    		container.add(record);
    		
    	}
    }
    
    public static void addRecord() {
    	records.put(user, Man.score); 
    }
    
    public static void showLb() {
    	lb.setVisible(true);
    	
    }
    
   
    private static void saveGame() {
    	String data = "\r\n" + user + " " + Man.score; 
    	try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true)); //true for append mode
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			System.out.println("error writing to file");
			e.printStackTrace();
		}
    }
    
    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> sortByVal(Map<K,V> records) {
        SortedSet<Map.Entry<K,V>> sorted = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int answ = e2.getValue().compareTo(e1.getValue());
                    return answ != 0 ? answ : 1;
                }
            }
        );
        sorted.addAll(records.entrySet()); 
        return sorted; 
    }
    
}