import java.awt.Graphics;
import java.awt.Image;

public class TA extends GameObj {

    public static final int SIZE = 30;
    private int maxX;
    private int maxY;
    public static final int INIT_PX = GameCourt.COURT_WIDTH / 2;
    public static final int INIT_PY = GameCourt.COURT_HEIGHT / 2;
    public static final int INIT_VX = 2;
    public static final int INIT_VY = 0;
    private static String file = "files/TA.png";
    Image icon = loadImage(file);
    private static int[][] maze = GameCourt.MAZE;

    public TA(int initvX, int initvY) {
        super(INIT_VX, INIT_VY, INIT_PX, INIT_PY, SIZE, SIZE,
                GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
        this.maxX = GameCourt.COURT_WIDTH - SIZE;
        this.maxY = GameCourt.COURT_HEIGHT - SIZE;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, this.getPx(), this.getPy(), 
                this.getWidth(), this.getHeight(), null, null);

    }

    public void restrict(Direction d) {

        int[] coords = translate(2, 31);
        int x1 = coords[0];
        int y1 = coords[1];
        int x2 = coords[2];
        int y2 = coords[3];

        if (d == null) {
            return;
        }

        if ((collision(x1, y1, x2, y2, 1) || atBound())) { // 1 is target because looking for walls
            turn(this.getDirection(), coords, true);
        }
    }

    @Override
    public void move() {
        int[] coords = translate(2, 31); // 2, 31 provides buffer so TA can fit in halls

        restrict(this.getDirection());
        clip();
        turn(this.getDirection(), coords, false);
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());
    }

    private boolean atBound() {
        return this.getPx() == maxX + 3 || this.getPx() == -3 
                || this.getPy() == maxY + 3 || this.getPy() == -3;
    }

    protected void turn(Direction d, int[] coords, boolean isCollision) {
        int x1 = coords[0];
        int y1 = coords[1];
        int x2 = coords[2];
        int y2 = coords[3];

        if (d == null) {
            return;
        }
        if (d.equals(Direction.LEFT) || d.equals(Direction.RIGHT)) {
            if (pathAbove(x1, y1, x2, y2) && takePath(Direction.UP, isCollision)) {
                this.setVx(0);
                this.setVy(-3);

            } else if (pathBelow(x1, y1, x2, y2) && takePath(Direction.DOWN, isCollision)) {
                this.setVx(0);
                this.setVy(3);

            } else if (!(pathAbove(x1, y1, x2, y2)) 
                    && !(pathBelow(x1, y1, x2, y2)) && isCollision) { // no path //
                bounce(this.getDirection());
            }
        } else {
            if (pathRight(x1, y1, x2, y2) && takePath(Direction.RIGHT, isCollision)) {
                this.setVy(0);
                this.setVx(3);
            } else if (pathLeft(x1, y1, x2, y2) && takePath(Direction.LEFT, isCollision)) {
                this.setVy(0);
                this.setVx(-3);

            } else if (!(pathLeft(x1, y1, x2, y2)) 
                    && !(pathRight(x1, y1, x2, y2)) && isCollision) { // no path
                bounce(this.getDirection());
            }
        }

    }

    private boolean takePath(Direction d, boolean isCollision) {
        double prob = Math.random();
        if (isCollision) {
            return true;
        }
        return prob >= 0.5;
    }

    // checking both corners
    private boolean pathAbove(int x1, int y1, int x2, int y2) {
        if (y1 == 0) {
            return false;
        }

        return maze[y1 - 1][x1] == 0 && maze[y1 - 1][x2] == 0;
    }

    private boolean pathBelow(int x1, int y1, int x2, int y2) {
        if (y1 == maze.length - 1) {
            return false;
        }
        return maze[y1 + 1][x1] == 0 && maze[y1 + 1][x2] == 0;
    }

    private boolean pathLeft(int x1, int y1, int x2, int y2) {
        if (x1 == 0) {
            return false;
        }
        return maze[y1][x1 - 1] == 0 && maze[y2][x1 - 1] == 0;
    }

    private boolean pathRight(int x1, int y1, int x2, int y2) {
        if (x1 == maze[y1].length - 1) {
            return false;
        }
        return maze[y1][x1 + 1] == 0 && maze[y2][x1 + 1] == 0;
    }

    public void setPx(int px) {
        super.setPx(px);
    }

    public void setPy(int py) {
        super.setPy(py); 
    }

    public void setVx(int vx) {
        super.setVx(vx);
    }

    public void setVy(int vy) {
        super.setVy(vy);
    }

}
