
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;

import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	public static final int[][] maze = { 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0 }, 
			{ 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
			{ 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0 }, 
			{ 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
			{ 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0 }, 
			{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
			{ 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 }, 
			{ 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
			{ 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 }, 
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

	// the state of the game logic
	public static Man pennMan = new Man();
	private TA ta1 = new TA(2, 0, false); 
	private TA ta2 = new TA(2, 0, false); 
	private TA ta3 = new TA(2, 0, false);
	private TA ta4 = new TA(2, 0, false); 
	private TA[] TAs  = { ta1, ta2, ta3, ta4 }; 
	

	public static boolean leftLock = false;
	public static boolean rightLock = false;
	public static boolean upLock = false;
	public static boolean downLock = false;

	public static final int maxScore = 141; 
	

	private boolean playing = false; // whether the game is running
	private JLabel status; // Current status text, i.e. "Running..."

	// Game constants
	public static final int COURT_WIDTH = 512;
	public static final int COURT_HEIGHT = 512;
	public static final int TILE_SIZE = 32;

	private static final int PENNMAN_VEL = 3;

	// Update interval for timer, in milliseconds
	public static final int INTERVAL = 35;

	public GameCourt(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.BLACK);

		// The timer is an object which triggers an action periodically with the given
		// INTERVAL. We
		// register an ActionListener with this timer, whose actionPerformed() method is
		// called each
		// time the timer triggers. We define a helper method called tick() that
		// actually does
		// everything that should be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area.
		// When this component has the keyboard focus, key events are handled by its key
		// listener.
		setFocusable(true);

		// This key listener allows the square to move as long as an arrow key is
		// pressed, by
		// changing the square's velocity accordingly. (The tick method below actually
		// moves the
		// square.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT && !(leftLock)) {
					pennMan.setVy(0);
					pennMan.setVx(-PENNMAN_VEL);

				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !(rightLock)) {
					pennMan.setVy(0);
					pennMan.setVx(PENNMAN_VEL);

				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && !(downLock)) {
					pennMan.setVy(PENNMAN_VEL);
					pennMan.setVx(0);
				} else if (e.getKeyCode() == KeyEvent.VK_UP && !(upLock)) {
					pennMan.setVy(-PENNMAN_VEL);
					pennMan.setVx(0);
				}
			}

		});

		this.status = status;
	}

	public static void resetLocks() {
		rightLock = false;
		leftLock = false;
		upLock = false;
		downLock = false;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		// square = new Square(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
		// poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
		// snitch = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);

		playing = true;
		status.setText("PENN-MAN");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			// advance the square and snitch in their current direction.
			// square.move();
			// snitch.move();
			
			if (isIntersection()) {
				updateLives(); 
			}
			 
			if (Man.score == maxScore) {
				winGame();
			}
			
			
			
			pennMan.move(); 
			ta1.move(); 
		 	ta2.move();
			ta3.move();
			ta4.move();
			
			// make the snitch bounce off walls...
			// snitch.bounce(snitch.hitWall());
			// ...and the mushroom
			// snitch.bounce(snitch.hitObj(poison));

			/*
			 * if (square.intersects(poison)) { playing = false;
			 * status.setText("You lose!"); } else if (square.intersects(snitch)) { playing
			 * = false; status.setText("You win!"); }
			 */

			// update the display
			repaint();
		}
	}
	
	private boolean isIntersection() {
		return pennMan.intersects(ta1)
		|| pennMan.intersects(ta2) 
		|| pennMan.intersects(ta3) 
		|| pennMan.intersects(ta4); 
	}
	
	private void updateLives() {
		Man.lives -= 1; 
		Game.updateLives();
		if (Man.lives <= 0) {
			loseGame();
		} else {
			stopMovement(); 
			int answer = JOptionPane.showConfirmDialog(null, "You were caught using stack overflow by the TA! "
					+ "Do you wish to continue, or do your accept failure?"); //yes = 0, no = 1, 2 = cancel
			if (answer == 0) {
				newLife();
			} else {
				Game.quit(); 
			}
		}
	}
	
	private void stopMovement() {
		pennMan.setVy(0);
		pennMan.setVx(0);
		for (TA t : TAs) {
			t.setVx(0);
			t.setVy(0);			
		}
	}
	
	private void loseGame() {
		stopMovement(); 
		Game.addRecord();
		String[] options = { "Quit"};
		JOptionPane.showMessageDialog(null, "You have failed the class");
		//int option = JOptionPane.showOptionDialog(null, "You have failed the class", "F", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		//if (options[option].equals("Leaderboard")) {
		//	Game.showLb();
		//} else {
			Game.quit();
		//}
	}
	
	private void winGame() {
		JOptionPane.showMessageDialog(null, "Congratualtions! You have passed the class");
		Game.quit();
	}
	
	private void newLife() {
		pennMan.setPx(Man.INIT_POS_X);
		pennMan.setPy(Man.INIT_POS_Y);
		pennMan.setVx(Man.INIT_VEL_X);
		pennMan.setVy(Man.INIT_VEL_Y);
		for (TA t : TAs) {
			t.setPx(TA.INIT_PX);
			t.setPy(TA.INIT_PY);
			t.setVx(TA.INIT_VX);
			t.setVy(TA.INIT_VY);
		}
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// square.draw(g);
		// poison.draw(g);
		// snitch.draw(g);
		pennMan.takePSet(); 
		drawMaze(g);
		pennMan.draw(g);
		ta1.draw(g);
		ta2.draw(g);
		ta3.draw(g);
		ta4.draw(g);
	}

	/*
	 * private void drawMaze(Graphics g) { for (int y = 0; y < COURT_HEIGHT; y +=
	 * TILE_SIZE) { for (int x = 0; x < COURT_WIDTH; x += TILE_SIZE) { if (maze[y /
	 * TILE_SIZE][x / TILE_SIZE] == 0) { drawWall(x, y, g); } else { drawPSet(x, y,
	 * g); } } } }
	 */
	private void drawMaze(Graphics g) {
		for (int i = 0; i < COURT_HEIGHT; i += TILE_SIZE) {
			for (int j = 0; j < COURT_WIDTH; j += TILE_SIZE) {
				if (maze[i / TILE_SIZE][j / TILE_SIZE] == 0) {
					g.setColor(Color.BLACK);
					g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
					g.setColor(Color.WHITE);
					g.fillOval(j + (TILE_SIZE / 2), i + (TILE_SIZE / 2), 3, 3);
				} else if (maze[i / TILE_SIZE][j / TILE_SIZE] == 1){
					g.setColor(Color.BLUE);
					g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
				} else {
					g.setColor(Color.black);
					g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
				}

			}
		}
	}


	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

	/*
	 * 
	 * public void drawCenterBlock(Graphics g) { int mid_w = COURT_WIDTH / 2; int
	 * mid_h = COURT_HEIGHT / 2;
	 * 
	 * int rm_w = COURT_WIDTH / 4; int rm_h = COURT_HEIGHT / 20;
	 * 
	 * int hall_w = 70; int post_w = COURT_WIDTH / 24; int post_h = COURT_HEIGHT /
	 * 5;
	 * 
	 * g.setColor(Color.blue);
	 * 
	 * //draw center block g.fillRect(mid_w - (rm_w / 2), mid_h, rm_w, rm_h);
	 * //middle g.fillRect(mid_w - (rm_w / 2), mid_h, post_w, post_h); //left
	 * g.fillRect(mid_w + (rm_w / 2) - post_w, mid_h, post_w, post_h); //right
	 * 
	 * //draw inner L-Brackets
	 * 
	 * //left g.fillRect(mid_w - (rm_w / 2) - hall_w, mid_h - hall_w, post_w, post_h
	 * + 2*hall_w); g.fillRect(mid_w - (rm_w / 2) - hall_w, mid_h - hall_w, 2*post_h
	 * - hall_w, post_w); //right g.fillRect(mid_w + (rm_w / 2) - post_w + hall_w,
	 * mid_h - hall_w, post_w, post_h + 2*hall_w); g.fillRect(mid_w - (rm_w / 2) +
	 * hall_w, mid_h + post_h - post_w + hall_w, 2*post_h - hall_w, post_w); //outer
	 * left g.fillRect(mid_w - (rm_w / 2) - 2*hall_w, mid_h - 2 * hall_w, post_w,
	 * post_h + 4*hall_w); g.fillRect(mid_w - (rm_w / 2) - 2*hall_w, mid_h -
	 * 2*hall_w, 2*post_h - hall_w, post_w);
	 */
}
