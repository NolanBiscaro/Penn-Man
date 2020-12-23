
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

import javax.imageio.ImageIO;

public abstract class GameObj {

    private int px;
    private int py;

    private int width;
    private int height;

    private int vx;
    private int vy;

    private int maxX;
    private int maxY;
    
    protected final static int BASE_SPEED = 3; 
    
    final static int[][] MAZE = GameCourt.MAZE; 
    
    protected abstract void draw(Graphics g);
    protected abstract void restrict(Direction d);
    

    public GameObj(int vx, int vy, int px, int py, int width, int height,
            int courtWidth, int courtHeight) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    protected void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    // translates pixel coordinates into tile coordinate
    protected int[] translate(int leftBuff, int rightBuff) {

        final int TILE_SIZE = GameCourt.TILE_SIZE;

        int x1 = this.getPx();
        int y1 = this.getPy();

        int x1Trans = Math.max(0, (int) Math.floor((x1 + leftBuff) / TILE_SIZE));
        int y1Trans = Math.max(0, (int) Math.floor((y1 + leftBuff) / TILE_SIZE));

        int x2Trans = Math.min(15, (int) Math.floor(x1 + rightBuff) / TILE_SIZE);
        int y2Trans = Math.min(15, (int) Math.floor(y1 + rightBuff) / TILE_SIZE);

        int[] coords = { x1Trans, y1Trans, x2Trans, y2Trans };
        return coords;
    }

    protected boolean collision(int x1, int y1, int x2, int y2, int target) {
        return MAZE[y1][x1] == target || MAZE[y2][x2] == target 
                || MAZE[y2][x1] == target || MAZE[y1][x2] == target;
    }

    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px 
                && this.py + this.height >= that.py && that.px + that.width >= this.px
                && that.py + that.height >= this.py);
    }

    public void bounce(Direction d) {
        if (d == null) {
            return;
        }

        switch (d) {
            case UP:
                this.vy = Math.abs(this.vy);
                break;
            case DOWN:
                this.vy = -Math.abs(this.vy);
                break;
            case LEFT:
                this.vx = Math.abs(this.vx);
                break;
            case RIGHT:
                this.vx = -Math.abs(this.vx);
                break;
            default:
                break;
        }
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
        return null; // not moving
    }

    protected static Image loadImage(String file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("Error fetching Man image");
            e.printStackTrace();
        }
        return img;
    }

    public void move() {
        this.px += this.vx;
        this.py += this.vy;
        this.clip();
    }
    
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
        //clip();
    }

    public void setPy(int py) {
        this.py = py;
       // clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

   

}