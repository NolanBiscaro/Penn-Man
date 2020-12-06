
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

    // the state of the game logic
    private Man pennMan = new Man(COURT_WIDTH, COURT_HEIGHT); 
    
    
    public static final int[][] maze = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0 }, { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0 }, { 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 }, { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 }, };

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 512;
    public static final int COURT_HEIGHT = 512;
    public static final int TILE_SIZE = 32;;
    
    private static final int PENNMAN_X_VEL = 4; 

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.RED));
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
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    pennMan.setVy(0);
                    pennMan.setVx(-PENNMAN_X_VEL);
                    
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    pennMan.setVy(0);
                    pennMan.setVx(PENNMAN_X_VEL);
                    
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    pennMan.setVy(PENNMAN_X_VEL);
                    pennMan.setVx(0);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    pennMan.setVy(-PENNMAN_X_VEL);
                    pennMan.setVx(0);
                }
            }

            public void keyReleased(KeyEvent e) {
                // pennMan.setVx(0);
                // pennMan.setVy(0);
            }
        });

        this.status = status;
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
            pennMan.move();

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // square.draw(g);
        // poison.draw(g);
        // snitch.draw(g);       
        drawMaze(g);
        pennMan.draw(g);
        
    }
/*
    private void drawMaze(Graphics g) {
        for (int y = 0; y < COURT_HEIGHT; y += TILE_SIZE) {
            for (int x = 0; x < COURT_WIDTH; x += TILE_SIZE) {
                if (maze[y / TILE_SIZE][x / TILE_SIZE] == 0) {
                    drawWall(x, y, g);
                } else {
                    drawPSet(x, y, g);
                }
            }
        }
    }*/ 
    private void drawMaze(Graphics g) {
        for (int i = 0; i < COURT_HEIGHT; i+= TILE_SIZE) {
            for (int j = 0; j < COURT_WIDTH; j+= TILE_SIZE) {
                if (maze[i / TILE_SIZE][j / TILE_SIZE] == 0) {
                    g.setColor(Color.green);
                    g.drawLine(j, i, j, i + TILE_SIZE);
                } else {
                    g.setColor(Color.red);
                    g.drawLine(j, i, j, i + TILE_SIZE);
                }
                
            }
        }
    }

    private static void drawWall(int x, int y, Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.fillRect(x + (TILE_SIZE / 2), y + (TILE_SIZE / 2), 5, 5);
    }

    private static void drawPSet(int x, int y, Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
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
