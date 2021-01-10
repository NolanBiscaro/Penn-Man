import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Date;

public class TA extends GameObj {

    private int initPx;
    private int initPy;
    public static final int INIT_VX = getBaseSpeed();
    public static final int INIT_VY = 0;
    private int dynamicSize = 32;

    private boolean lostVert, lostHoriz;

    private boolean isChaser;
    private boolean explode;
    private boolean isDead;
    public static boolean scatter;

    public TA(int initPx, int initPy) {
        super(INIT_VX, INIT_VY, initPx, initPy);
        this.initPx = initPx;
        this.initPy = initPy;
        lostVert = false;
        lostHoriz = false;
        scatter = false;
        isChaser = true;
        explode = false;
        isDead = false;
    }

    @Override
    public void draw(Graphics g) {
        if (scatter) {
            drawScattered(g);
        } else {
            drawTA(g);
        }
    }

    public void drawTA(Graphics g) {
        g.drawImage(ImageController.ta(), this.getPx(), this.getPy(), this.getWidth(),
                this.getHeight(), null, null);
    }

    private void drawScattered(Graphics g) {
        g.drawImage(ImageController.scatter(), this.getPx(), this.getPy(), this.getWidth(),
                this.getHeight(), null, null);
    }

    public void drawExploding(Graphics g) {
        if (dynamicSize <= 150) {
            g.drawImage(ImageController.explosion(), this.getPx(), this.getPy(), dynamicSize,
                    dynamicSize, null, null);
            dynamicSize += 6;
            Man.setScore(Man.getScore() + 1);
        } else {
            this.kill();
        }
    }

    private void tryPath(Direction dRel) {
        Direction toMan = getOppDir(dRel);
        if (dRel.equals(Direction.UP) || dRel.equals(Direction.DOWN)) {
            if (isPath(toMan)) {
                takePath(toMan);
                lostVert = false;
            } else {
                lostVert = true;
            }
        } else {
            if (isPath(toMan)) {
                takePath(toMan);
                lostHoriz = false;
            } else {
                lostHoriz = true;
            }
        }

    }

    private void takeHorizontalPath() {
        boolean left = isPath(Direction.LEFT);
        boolean right = isPath(Direction.RIGHT);

        if (!(left) && !(right)) {
            return;
        } else if (!(right) && left) {
            takeLeft();
        } else if (!(left) && right) {
            takeRight();
        } else {
            randomHorizontal();
        }
    }

    private void randomHorizontal() {
        double p = Math.random();
        if (p > 0.3) {
            takeRight();
        } else {
            takeLeft();
        }
    }

    private void takeVerticalPath() {

        boolean above = isPath(Direction.UP);
        boolean below = isPath(Direction.DOWN);

        if (!(above) && !(below)) {
            return;
        } else if (!(above) && below) {
            takeBelow();
        } else if (!(below) && above) {
            takeAbove();
        } else {
            randomVertical();
        }
    }

    private void randomVertical() {
        double p = Math.random();
        if (p > 0.3) {
            takeAbove();
        } else {
            takeBelow();
        }
    }

    private Direction relativeX(int thatX) {

        if (this.getPx() == thatX) { // parallel
            return Direction.PARALLEL;
        }
        if (this.getPx() > thatX) {
            return Direction.RIGHT;
        }
        return Direction.LEFT;
    }

    private Direction relativeY(int thatY) {

        if (this.getPy() == thatY) { // parallel
            return Direction.PARALLEL;
        }
        if (this.getPy() < thatY) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    private void updateStatus(Direction dirTravel, int thatX, int thatY) {
        if (dirTravel == null) {
            return;
        }
        Direction ofManY = this.getOppDir(relativeY(thatY));
        Direction ofManX = this.getOppDir(relativeX(thatX));
        if (dirTravel.equals(ofManY) || dirTravel.equals(ofManX)) {
            isChaser = true;
        }
    }

    private void wander(Direction dirTravel, int thatX, int thatY) {
        boolean vertDir = dirTravel.equals(Direction.UP) || dirTravel.equals(Direction.DOWN);
        if (vertDir) {
            takeHorizontalPath();
        } else if (!(vertDir)) {
            takeVerticalPath();
        } else {
            takeRandomPath();
        }
        updateStatus(dirTravel, thatX, thatY);
    }

    private void takeRandomPath() {
        double p = Math.random();
        if (p > 0.5) {
            takeVerticalPath();
        } else {
            takeHorizontalPath();
        }
    }

    protected void navigate(int thatX, int thatY) { // navigates chaser towards Man
        Direction dirTravel = this.getDirection();
        boolean parallelX = relativeX(thatX).equals(Direction.PARALLEL);
        boolean parallelY = relativeY(thatY).equals(Direction.PARALLEL);

        if (scatter) {
            wander(dirTravel, thatX, thatY);
        } else if (isChaser) {
            if (parallelY) {
                isChaser = false;
                return;
            }
            tryPath(relativeY(thatY));
            if (parallelX) {
                isChaser = false;
                return;
            }
            tryPath(relativeX(thatX));

            if (lostHoriz && lostVert) {
                isChaser = false;
            }
        } else {
            wander(dirTravel, thatX, thatY);
        }
    }

    public int getInitPx() {
        return this.initPx;
    }

    public int getInitPy() {
        return this.initPy;
    }

    public void setChaser(boolean isChaser) {
        this.isChaser = isChaser;
    }

    public static boolean isScatter() {
        return scatter;
    }

    public static void scatter() {

    }

    public boolean isDead() {
        return this.isDead;
    }

    public void kill() {
        this.explode = false;
        this.isDead = true;
    }

    public void blowUp() {
        this.explode = true;
    }

    public boolean expoloding() {
        return this.explode;
    }

}
