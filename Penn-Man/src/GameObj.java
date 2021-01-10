
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

public abstract class GameObj {

    private int px;
    private int py;

    private int width;
    private int height;
    private static final int SIZE = 32;

    private int vx;
    private int vy;

    private int maxX;
    private int maxY;

    final static int[][] MAZE = GameCourt.MAZE;

    final static int COURT_WIDTH = GameCourt.COURT_WIDTH;
    final static int COURT_HEIGHT = GameCourt.COURT_HEIGHT;

    private static int baseSpeed;

    protected abstract void draw(Graphics g);

    public GameObj(int vx, int vy, int px, int py) {
        this.vx = vx;
        this.vy = vy;

        this.px = px;
        this.py = py;

        this.width = SIZE;
        this.height = SIZE;

        this.maxX = COURT_WIDTH - width;
        this.maxY = COURT_HEIGHT - height;

        baseSpeed = 2;
    }

    public void move() {
        this.px += this.vx;
        this.py += this.vy;
        this.clip();
    }

    protected void clip() { // keep gameObj within bounds of JFrame.
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    protected int[] translate() { // translates pixel coordinates into tile coordinates

        final int TILE_SIZE = GameCourt.TILE_SIZE;

        int x1 = this.getPx();
        int y1 = this.getPy();

        int x2 = this.getPx() + this.getWidth();
        int y2 = this.getPy() + this.getHeight();

        int x1Trans = Math.floorDiv(x1, TILE_SIZE);
        int y1Trans = Math.floorDiv(y1, TILE_SIZE);

        int x2Trans = Math.min(MAZE.length - 1, Math.floorDiv(x2, TILE_SIZE));
        int y2Trans = Math.min(MAZE.length - 1, Math.floorDiv(y2, TILE_SIZE));

        int centerTransX = Math.floorDiv(((x1 + x2) / 2), TILE_SIZE);
        int centerTransY = Math.floorDiv((y1 + y2) / 2, TILE_SIZE);

        int[] coords = { x1Trans, y1Trans, x2Trans, y2Trans, centerTransX, centerTransY };
        return coords;
    }

    protected Direction collision(int wall, Direction dirTravel) {
        int[] coords = translate();

        int x1 = coords[0];
        int y1 = coords[1];

        int x2 = coords[2];
        int y2 = coords[3];

        int cx = coords[4];
        int cy = coords[5];

        int topLeft = MAZE[y1][x1];
        int topRight = MAZE[y1][x2];

        int bottomLeft = MAZE[y2][x1];
        int bottomRight = MAZE[y2][x2];

        int topCenter = MAZE[y1][cx];
        int bottomCenter = MAZE[y2][cx];

        int rightCenter = MAZE[cy][x2];
        int leftCenter = MAZE[cy][x1];

        switch (dirTravel) {
            case UP:
                if ((topLeft == wall && topRight == wall) || topCenter == wall) {
                    return Direction.UP;
                }
                break;
            case DOWN:
                if ((bottomLeft == wall && bottomRight == wall) || bottomCenter == wall) {
                    return Direction.DOWN;
                }
                break;
            case RIGHT:
                if ((topRight == wall && bottomRight == wall) || rightCenter == wall) {
                    return Direction.RIGHT;
                }
                break;
            case LEFT:
                if ((topLeft == wall && bottomLeft == wall) || leftCenter == wall) {
                    return Direction.LEFT;
                }
                break;
            default:
                return Direction.PARALLEL;
        }
        return Direction.PARALLEL;
    }

    public void restrict(Direction dirTravel) { // dirTravel is the current direction of travel.
        int wall = 1;
        Direction dirCollision = collision(wall, dirTravel);

        if (dirCollision.equals(Direction.PARALLEL)) { // no collision
            return;
        }

        if (dirTravel.equals(Direction.UP) || dirTravel.equals(Direction.DOWN)) {
            if (dirCollision.equals(Direction.UP) || dirCollision.equals(Direction.DOWN)) {
                this.setVy(0);
                snapY();
            }
        } else { // moving horizontally since not vertically
            if (dirCollision.equals(Direction.RIGHT) || dirCollision.equals(Direction.LEFT)) {
                this.setVx(0);
                snapX();
            }
        }
    }

    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px && this.py + this.height >= that.py
                && that.px + that.width >= this.px && that.py + that.height >= this.py);
    }

    public Direction getDirection() {
        if (this.vx < 0) {
            return Direction.LEFT;
        } else if (vx > 0) {
            return Direction.RIGHT;
        } else if (vy < 0) {
            return Direction.UP;
        } else if (vy > 0) {
            return Direction.DOWN;
        }
        return Direction.PARALLEL; // not moving
    }

    public Direction getOppDir(Direction d) {
        switch (d) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case RIGHT:
                return Direction.LEFT;
            case LEFT:
                return Direction.RIGHT;
            default:
                return Direction.PARALLEL; // no opposite.
        }
    }

    protected boolean isSnappedX() {
        return this.getPx() % 32 == 0;
    }

    protected boolean isSnappedY() {
        return this.getPy() % 32 == 0;
    }

    protected void snapX() {
        double div = this.getPx() / 32.0;
        int closestMultX = (int) Math.max(32, 32 * Math.round(div));
        this.setPx(closestMultX);
    }

    protected void snapY() {
        double div = Math.round(this.getPy() / 32.0);
        int closestMultY = (int) Math.max(32, 32 * Math.round(div));
        this.setPy(closestMultY);
    }

    protected boolean pathAbove(int y1, int cx, int cy) {
        if (y1 == 0) {
            return false;
        }
        return isSnappedX() && MAZE[cy - 1][cx] != 1;
    }

    protected boolean pathBelow(int cx, int cy) {
        if (cy == MAZE.length - 1) {
            return false;
        }
        return isSnappedX() && MAZE[cy + 1][cx] != 1;
    }

    protected boolean pathLeft(int x1, int cx, int cy) {
        if (x1 == 0) {
            return false;
        }
        return isSnappedY() && MAZE[cy][cx - 1] != 1;
    }

    protected boolean pathRight(int y1, int cx, int cy) {
        if (cx == MAZE[y1].length - 1) {
            return false;
        }
        return isSnappedY() && MAZE[cy][cx + 1] != 1;
    }

    protected boolean isPath(Direction tryDir) {
        int[] coords = translate();

        int x1 = coords[0];
        int y1 = coords[1];

        int cx = coords[4];
        int cy = coords[5];

        if (tryDir == null) {
            return false;
        }

        switch (tryDir) {
            case UP:
                return pathAbove(y1, cx, cy);
            case DOWN:
                return pathBelow(cx, cy);
            case RIGHT:
                return pathRight(y1, cx, cy);
            case LEFT:
                return pathLeft(x1, cx, cy);
            default:
                return false;
        }
    }

    protected void takePath(Direction takeDir) {
        switch (takeDir) {
            case UP:
                takeAbove();
                break;
            case DOWN:
                takeBelow();
                break;
            case RIGHT:
                takeRight();
                break;
            case LEFT:
                takeLeft();
                break;
            default:
                return;
        }
    }

    protected void takeLeft() {
        setVy(0);
        setVx(-baseSpeed);
    }

    protected void takeRight() {
        setVy(0);
        setVx(baseSpeed);
    }

    protected void takeAbove() {
        setVx(0);
        setVy(-baseSpeed);
    }

    protected void takeBelow() {
        setVx(0);
        setVy(baseSpeed);

    }

    // getters + setters

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVx() {
        return this.vx;
    }

    public int getVy() {
        return this.vy;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public static int getBaseSpeed() {
        return baseSpeed;
    }

    public static void setBaseSpeed(int speed) {
        baseSpeed = speed;
    }

}