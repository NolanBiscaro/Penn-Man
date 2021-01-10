import java.awt.Graphics;
import java.awt.Image;
import java.util.Date;

public class Man extends GameObj {

    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_POS_X = 32;
    public static final int INIT_POS_Y = 0;
    protected static final int TILE_SIZE = 32;
    private static final int NON_EMPTY = 0;
    private static final int EMPTY = -1;
    private static final int COFFEE = 2;

    private static Direction lastPressed;

    private static long scatterStart;
    
    private static Image icon;

    // Initial score and lives that the user begins with.
    private static int score = 0;
    private static int lives = 3;

    private static int[][] maze = GameCourt.MAZE;

    public Man(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y);

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null,
                null);
        icon = ImageController.open();
    }

    public void checkPath() {
        if (isPath(lastPressed)) {
            takePath(lastPressed);
            lastPressed = null;
        }
    }

    private boolean isPSet(int cy, int cx) {
        return MAZE[cy][cx] == NON_EMPTY;
    }

    private boolean isCoffee(int cy, int cx) {
        return MAZE[cy][cx] == COFFEE;
    }

    public void takeItem() {
        int[] coords = translate();

        int cx = coords[4];
        int cy = coords[5];

        if (isPSet(cy, cx)) {
            maze[cy][cx] = EMPTY; // -1 indicates an empty tile.
            score += 1;
            Game.updateScore(score);
            icon = ImageController.close();
            AudioController.chomp();
        } else if (isCoffee(cy, cx)) {
            maze[cy][cx] = EMPTY;
            startScatter();
            AudioController.caffeinate();
        }
    }
    
    protected void stopScatter() {
        this.setVx(this.getVx() / 2);
        this.setVy(this.getVy() / 2);
        System.out.println("here");
        setBaseSpeed(2);
        TA.scatter = false;
        AudioController.decaffeinate();
    }
    
    public void checkStamina() {
        if (!(TA.scatter)) {
            return;
        } else {
            if (tired()) {
                stopScatter();
            }
        }
    }
    
    protected static boolean tired() {
        return new Date().getTime() - scatterStart > 5000;
    }

    protected void startScatter() {
        this.setVx(this.getVx() * 2);
        this.setVy(this.getVy() * 2);
        scatterStart = System.currentTimeMillis();
        TA.scatter = true;
        setBaseSpeed(4); 
    }

    public void reset() {
        setPx(INIT_POS_X);
        setPy(INIT_POS_Y);
        setVx(INIT_VEL_X);
        setVy(INIT_VEL_Y);
    }

    // getters + setters

    public static int getScore() {
        return score;
    }

    public static void setScore(int s) {
        score = s;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int l) {
        lives = l;
    }

    protected static void setLastPressed(Direction d) {
        lastPressed = d;
    }

    protected static Direction getLastPressed() {
        return lastPressed;
    }

}
