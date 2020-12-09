import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class TA extends GameObj {

	public static final int SIZE = 30;
	private int maxX;
	private int maxY;
	// private static final int INIT_PX = 0;
	// private static final int INIT_PY = 0;
	private static String file = "files/TA.png";
	public boolean chaser;
	Image icon = loadImage(file);
	private static int[][] maze = GameCourt.maze;

	public TA(int INIT_PX, int INIT_PY, int INIT_VX, int INIT_VY, boolean chaser) {
		super(INIT_VX, INIT_VY, INIT_PX, INIT_PY, SIZE, SIZE, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);
		this.chaser = chaser; 
		this.maxX = GameCourt.COURT_WIDTH - SIZE;
		this.maxY = GameCourt.COURT_HEIGHT - SIZE;
	}

	@Override
	public void draw(Graphics g) {
		if (this.chaser) {
			g.setColor(Color.RED);
			g.fillRect(this.getPx(), this.getPy(), SIZE, SIZE);
		}
		g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null, null);

	}
	
	protected void restrict(Direction d) {
		int[] coords = translate();

		// top left
		int x1 = coords[0];
		int y1 = coords[1];

		// bottom right
		int x2 = coords[2];
		int y2 = coords[3];

		if (d == null) {
			return;
		}
		if (collision(x1, y1, x2, y2, 1) || this.getPx() == maxX) { // 1 is target because looking for walls			
			if (this.vx <= 0 && this.getPx() == 0) {
				bounce(Direction.LEFT); 
				
			}
			turn(this.getDirection(), true);
			
			/*
			if (this.getPx() % 32 == 0 && this.getPy() % 32 == 0) { //in middle of TILE
				turn(this.getDirection(), true);
			} */ 
		}
	}

	@Override
	protected void move() {
		this.px += this.vx;
		this.py += this.vy;

		clip();		
		if (this.getPx() % 32 == 0 && this.getPy() % 32 == 0) { //in middle of TILE
			turn(this.getDirection(), false);
		} 
		restrict(this.getDirection());
		
	}

	protected void turn(Direction d, boolean isCollision) {
		int[] coords = translate();
		int x = coords[0];
		int y = coords[1];
		if (d == null) {
			return;
		}
		if (d.equals(Direction.LEFT) || d.equals(Direction.RIGHT)) {
			if (pathAbove(x, y) && takePath(Direction.UP, isCollision)) {
				this.setVx(0);
				this.setVy(-3);
			} else if (pathBelow(x, y) && takePath(Direction.DOWN, isCollision)) {
				this.setVx(0);
				this.setVy(3);
			}
		} else {
			if (pathRight(x, y) && takePath(Direction.RIGHT, isCollision)) {
				this.setVy(0);
				this.setVx(3);
			} else if (pathLeft(x, y) && takePath(Direction.LEFT, isCollision)) {
				this.setVy(0);
				this.setVx(-3);
			}
		}

	}

	private boolean takePath(Direction d, boolean isCollision) { // d is the direction we are considering taking
		if (isCollision) {
			return true; 
		}
		double prob = Math.random();
		if (chaser) {
			switch (d) {
			case UP:
				return GameCourt.pennMan.getPy() < this.getPy(); // pennman is above us

			case DOWN:
				return GameCourt.pennMan.getPy() > this.getPy(); // below us

			case RIGHT:
				return GameCourt.pennMan.getPx() > this.getPx();

			case LEFT:
				return GameCourt.pennMan.getPx() < this.getPx();

			default:
				return false;
			}
		}

		return prob >= 0.2;
	}

	private boolean pathAbove(int x, int y) {
		if (y == 0) {
			return false;
		}
		return (maze[y - 1][x] == 0);
	}

	private boolean pathBelow(int x, int y) {
		if (y == maze.length - 1) {
			return false;
		}
		return (maze[y + 1][x] == 0);
	}

	private boolean pathLeft(int x, int y) {
		if (x == 0) {
			return false;
		}
		return (maze[y][x - 1] == 0);
	}

	private boolean pathRight(int x, int y) {
		if (x == maze[y].length - 1) {
			return false;
		}
		return (maze[y][x + 1] == 0);
	}

}
