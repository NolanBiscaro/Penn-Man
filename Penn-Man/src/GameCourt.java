
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

import java.awt.event.*;
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
    
 // Game constants
    public static final int COURT_WIDTH = 512;
    public static final int COURT_HEIGHT = 512;
    public static final int TILE_SIZE = 32;

    public static final int[][] MAZE = { 
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
            { 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, 
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

    // the state of the game logic
    private static Man pennMan = new Man(COURT_WIDTH, COURT_HEIGHT);
    private static TA ta1 = new TA(2, 0);
    private static TA ta2 = new TA(2, 0);
    private static TA ta3 = new TA(2, 0);
    private static TA ta4 = new TA(2, 0);
    private static TA ta5 = new TA(2, 0);
    private static TA ta6 = new TA(2, 0);
    private static TA[] taS = { ta1, ta2, ta3, ta4, ta5, ta6 };

    public static final int MAX_SCORE = 141;

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."

    

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
                if (e.getKeyCode() == KeyEvent.VK_LEFT && !(pennMan.leftLock)) {
                    pennMan.setVy(0);
                    pennMan.setVx(-Man.BASE_SPEED);

                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !(pennMan.rightLock)) {
                    pennMan.setVy(0);
                    pennMan.setVx(Man.BASE_SPEED);

                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !(pennMan.downLock)) {
                    pennMan.setVy(Man.BASE_SPEED);
                    pennMan.setVx(0);
                } else if (e.getKeyCode() == KeyEvent.VK_UP && !(pennMan.upLock)) {
                    pennMan.setVy(-Man.BASE_SPEED);
                    pennMan.setVx(0);
                }
            }

        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {

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

            if (isIntersection()) {
                updateLives();
            }

            if (Man.getScore() == MAX_SCORE) {
                winGame();
            }

            pennMan.move();
            pennMan.restrict(pennMan.getDirection());
            ta1.move();
            ta2.move();
            ta3.move();
            ta4.move();
            ta5.move();
            ta6.move();

            // update the display
            repaint();
        }
    }

    private boolean isIntersection() {

        return pennMan.intersects(ta1) || pennMan.intersects(ta2) 
                || pennMan.intersects(ta3) || pennMan.intersects(ta4)
                || pennMan.intersects(ta5) || pennMan.intersects(ta6);
    }

    private void updateLives() {
        Man.setLives(Man.getLives() - 1);
        Game.updateLives();
        if (Man.getLives() <= 0) {
            loseGame();
        } else {
            stopMovement();
            int answer = JOptionPane.showConfirmDialog(null, 
                    "You were caught using stack overflow by the TA! "
                    + "Do you wish to continue, or do your accept failure?");
            if (answer == 0) {
                newLife();
            } else {
                Game.quit();
            }
        }
    }

    public void stopMovement() {
        pennMan.setVy(0);
        pennMan.setVx(0);
        for (TA t : taS) {
            t.setVx(0);
            t.setVy(0);
        }
    }

    private void loseGame() {
        stopMovement();
        Game.addRecord();
        JOptionPane.showMessageDialog(null, "You have failed the class");
        Game.quit();
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
        for (TA t : taS) {
            t.setPx(TA.INIT_PX);
            t.setPy(TA.INIT_PY);
            t.setVx(TA.INIT_VX);
            t.setVy(TA.INIT_VY);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        pennMan.takePSet();
        drawMaze(g);
        pennMan.draw(g);
        ta1.draw(g);
        ta2.draw(g);
        ta3.draw(g);
        ta4.draw(g);
        ta5.draw(g);
        ta6.draw(g);

    }
    
    public static TA[] getTas() {
        return taS; 
    }

    private void drawMaze(Graphics g) {
        for (int i = 0; i < COURT_HEIGHT; i += TILE_SIZE) {
            for (int j = 0; j < COURT_WIDTH; j += TILE_SIZE) {
                if (MAZE[i / TILE_SIZE][j / TILE_SIZE] == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillOval(j + (TILE_SIZE / 2), i + (TILE_SIZE / 2), 3, 3);
                } else if (MAZE[i / TILE_SIZE][j / TILE_SIZE] == 1) {
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
}
