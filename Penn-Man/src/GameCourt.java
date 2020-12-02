
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
    public int[][] maze =    {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                {1,0,1,0,1,1,1,1,1,1,1,0,1,0,1,0},
                                {1,0,1,0,0,0,0,0,0,0,1,0,1,0,1,0},
                                {1,0,1,0,1,1,1,1,1,0,0,0,1,0,1,0},
                                {1,0,1,0,1,0,0,0,0,0,1,0,1,0,1,0},
                                {1,0,1,0,1,0,0,0,0,0,1,0,0,0,1,0},
                                {1,0,0,0,1,0,0,0,0,0,1,0,1,0,1,0},
                                {1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,0},
                                {1,0,1,0,0,0,0,0,0,0,0,0,1,0,1,0},
                                {1,0,1,1,1,1,1,1,1,0,1,1,1,0,1,0},
                                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},
                                {0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0},
                                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                {1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,0},
                                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                                {1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
                                }; 
     

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 512;
    public static final int COURT_HEIGHT = 512;
    public static final int TILE_SIZE = 32;
    ;

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
                    //
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    //
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // square.setVy(SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    // square.setVy(-SQUARE_VELOCITY);
                }
            }

            public void keyReleased(KeyEvent e) {
                // square.setVx(0);
                // square.setVy(0);
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

            // make the snitch bounce off walls...
            // snitch.bounce(snitch.hitWall());
            // ...and the mushroom
            // snitch.bounce(snitch.hitObj(poison));

            // check for the game end conditions
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
    }
    
    private void drawMaze(Graphics g) {
        int mid = TILE_SIZE / 2;
        for (int y = 0; y < COURT_HEIGHT; y+=TILE_SIZE) {
            for (int x = 0; x < COURT_WIDTH; x+=TILE_SIZE) {
                if (maze[y / TILE_SIZE][x  / TILE_SIZE] == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.WHITE);
                    g.fillRect(x + mid, y + mid, 5, 5);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    
    /*

    public void drawCenterBlock(Graphics g) {
        int mid_w = COURT_WIDTH / 2; 
        int mid_h = COURT_HEIGHT / 2;
        
        int rm_w = COURT_WIDTH / 4; 
        int rm_h = COURT_HEIGHT / 20; 
        
        int hall_w = 70; 
        int post_w = COURT_WIDTH / 24;
        int post_h = COURT_HEIGHT / 5; 
       
        g.setColor(Color.blue);
        
        //draw center block
        g.fillRect(mid_w - (rm_w / 2), mid_h, rm_w, rm_h); //middle
        g.fillRect(mid_w - (rm_w / 2), mid_h, post_w, post_h); //left
        g.fillRect(mid_w + (rm_w / 2) - post_w, mid_h, post_w, post_h); //right
        
        //draw inner L-Brackets
        
        //left
        g.fillRect(mid_w - (rm_w / 2) - hall_w, mid_h - hall_w, post_w, post_h + 2*hall_w); 
        g.fillRect(mid_w - (rm_w / 2) - hall_w, mid_h - hall_w, 2*post_h - hall_w, post_w);
        //right
        g.fillRect(mid_w + (rm_w / 2) - post_w + hall_w, mid_h - hall_w, post_w, post_h + 2*hall_w);
        g.fillRect(mid_w - (rm_w / 2) + hall_w, mid_h + post_h - post_w + hall_w, 2*post_h - hall_w, post_w);
        //outer left
        g.fillRect(mid_w - (rm_w / 2) - 2*hall_w, mid_h - 2 * hall_w, post_w, post_h + 4*hall_w); 
        g.fillRect(mid_w - (rm_w / 2) - 2*hall_w, mid_h - 2*hall_w, 2*post_h - hall_w, post_w);
        */
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

