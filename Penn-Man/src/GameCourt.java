
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

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

    public static final int[][] MAZE = { { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 0, 0, 2, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0 },
            { 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 },
            { 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

    // the state of the game logic
    private static Man pennMan = new Man(COURT_WIDTH, COURT_HEIGHT);

    private static int r = 0;
    private static int b = 255;
    private static int gr = 0;

    private static TA ta1 = new TA(5 * TILE_SIZE, 4 * TILE_SIZE);
    private static TA ta2 = new TA(8 * TILE_SIZE, 4 * TILE_SIZE);
    private static TA ta3 = new TA(13 * TILE_SIZE, 10 * TILE_SIZE);
    private static TA ta4 = new TA(2 * TILE_SIZE, 10 * TILE_SIZE);
    private static ArrayList<TA> staff = new ArrayList<TA>();

    public static int P_SETS = 0;

    private boolean playing = false; // whether the game is running
    private JLabel status; // Current status text, i.e. "Running..."

    // Update interval for timer, in milliseconds
    private static final int INTERVAL = 30;

    private void initStaff() {
        staff.add(ta1);
        staff.add(ta2);
        staff.add(ta3);
        staff.add(ta4);
    }

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);
        initStaff();

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

                Direction keyPress = getDirFromKey(e);
                System.out.println("key: " + keyPress);

                if (keyPress == null) { // not pressing
                    return;
                }

                if (pennMan.isPath(keyPress)) {
                    pennMan.takePath(keyPress);
                } else {
                    Man.setLastPressed(keyPress);
                }

            }
        });

        this.status = status;

    }

    private Direction getDirFromKey(KeyEvent k) {
        switch (k.getKeyCode()) {
            case KeyEvent.VK_UP:
                return Direction.UP;
            case KeyEvent.VK_DOWN:
                return Direction.DOWN;
            case KeyEvent.VK_LEFT:
                return Direction.LEFT;
            case KeyEvent.VK_RIGHT:
                return Direction.RIGHT;
            default:
                return null;
        }
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

    private boolean lost() {
        return Man.getLives() == 0;
    }

    private boolean won() {
        return P_SETS == 0 || staff.size() == 0;
    }

    private void newLife() {
        pennMan.reset();
        resetStaff();
    }

    private void showOptions() {
        Icon caught = new ImageIcon(ImageController.caught());
        int answer = JOptionPane.showConfirmDialog(this,
                "You were caught by the TA! " + "Do you wish to continue", "CAUGHT",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, caught);
        if (answer == 0) {
            newLife();
        } else {
            loseGame();
        }
    }

    private void updateLives() {
        Man.setLives(Man.getLives() - 1);
        Game.updateLives();
    }

    private int[] stopMovement() {
        int[] oldSpeeds = { pennMan.getVx(), pennMan.getVy() };
        pennMan.setVy(0);
        pennMan.setVx(0);
        for (TA t : staff) {
            t.setVx(0);
            t.setVy(0);
        }
        return oldSpeeds;
    }

    private void startMovement(int[] oldSpeeds) {
        pennMan.setVx(oldSpeeds[0]);
        pennMan.setVy(oldSpeeds[1]);
    }

    private void loseGame() {
        stopMovement();
        Game.addRecord();
        ImageIcon icon = new ImageIcon(ImageController.scatter());
        JOptionPane.showMessageDialog(this, "You Lose.", "LOSE", JOptionPane.INFORMATION_MESSAGE,
                icon);
        Game.quit();
    }

    private void winGame() {
        ImageIcon icon = new ImageIcon(ImageController.win());
        JOptionPane.showMessageDialog(this, "Congratulations. You have won!", "WINNER",
                JOptionPane.INFORMATION_MESSAGE, icon);
        Game.quit();
    }

    private void resetStaff() {
        for (TA t : staff) {
            t.setPx(t.getInitPx());
            t.setPy(t.getInitPy());
            t.setChaser(true);
        }
    }

    private TA intersection() {
        for (TA t : staff) {
            if (pennMan.intersects(t)) {
                return t;
            }
        }
        return null;
    }

    private void killTA(TA intersected) {
        if (!(intersected.isDead() || intersected.expoloding())) {
            AudioController.explode();
        }
        intersected.blowUp();
        intersected = null;
    }

    private void cleanStaff() {
        Iterator<TA> cleaner = staff.iterator();
        while (cleaner.hasNext()) {
            TA t = cleaner.next();
            if (t.isDead()) {
                cleaner.remove();
            }
        }
    }

    private void colorShift() {
        int inc = 15;
        if (r + inc < 255) {
            r += inc;
        } else if (gr + inc < 255) {
            gr += inc;
        } else if (b + inc < 255) {
            b += inc;
        } else {
            r = 100;
            gr = 100;
            b = 100;
        }
    }

    private static void defaultColors() {
        r = 0;
        gr = 0;
        b = 255;
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            TA intersected = intersection();

            if (TA.isScatter()) {
                colorShift();
            } else {
                defaultColors();
            }

            if (intersected != null) {
                if (TA.isScatter()) {
                    killTA(intersected);
                } else {
                    updateLives();
                    if (lost()) {
                        loseGame();
                    } else {
                        AudioController.caught();
                        stopMovement();
                        showOptions();
                    }
                }

            }

            if (won()) {
                winGame();
            }

            pennMan.move();
            pennMan.restrict(pennMan.getDirection());
            pennMan.checkPath();
            pennMan.checkStamina();

            for (TA t : staff) {
                t.move();
                t.navigate(pennMan.getPx(), pennMan.getPy());
                t.restrict(t.getDirection());
            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
        pennMan.takeItem();
        pennMan.draw(g);
        cleanStaff();
        for (TA t : staff) {
            if (t.expoloding()) {
                t.drawExploding(g);
            } else {
                t.draw(g);
            }
        }
    }

    private void drawMaze(Graphics g) {
        for (int i = 0; i < COURT_HEIGHT; i += TILE_SIZE) {
            for (int j = 0; j < COURT_WIDTH; j += TILE_SIZE) {
                if (MAZE[i / TILE_SIZE][j / TILE_SIZE] == 0) {
                    drawPath(g, i, j);
                } else if (MAZE[i / TILE_SIZE][j / TILE_SIZE] == 1) {
                    drawWall(g, i, j);
                } else if (MAZE[i / TILE_SIZE][j / TILE_SIZE] == 2) {
                    drawCoffee(g, i, j);
                }

                else {
                    g.setColor(Color.black);
                    g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
                }

            }
        }
    }

    private static void drawPath(Graphics g, int i, int j) {
        g.setColor(Color.BLACK);
        g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.WHITE);
        g.fillOval(j + (TILE_SIZE / 2), i + (TILE_SIZE / 2), 3, 3);
        P_SETS += 1;
    }

    private static void drawWall(Graphics g, int i, int j) {
        g.setColor(new Color(r, gr, b));
        g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
    }

    private static void drawCoffee(Graphics g, int i, int j) {
        g.setColor(Color.BLACK);
        g.fillRect(j, i, TILE_SIZE, TILE_SIZE);
        g.drawImage(ImageController.coffee(), j, i, 32, 32, null, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
