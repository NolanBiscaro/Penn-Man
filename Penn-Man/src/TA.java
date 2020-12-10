import java.awt.Color;
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
	public boolean chaser;
	Image icon = loadImage(file);
	private static int[][] maze = GameCourt.maze;

	public TA(int INIT_VX, int INIT_VY, boolean chaser) {
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

		int[] coords = translate(2, 31); // no buffer required since automated //2,31 sweeeeet spot
		int x1 = coords[0];
		int y1 = coords[1];
		int x2 = coords[2];
		int y2 = coords[3];

		if (d == null) {
			return;
		}

		if ((collision(x1, y1, x2, y2, 1) /* && inMiddle(x1, y1, x2, y2)) */) || atBound()) { // 1 is target because
																								// looking for walls
			// System.out.println("COLLISION");
			System.out.println("COLLISION");
			turn(this.getDirection(), coords, true);

			/*
			 * if (this.getPx() % 32 == 0 && this.getPy() % 32 == 0) { //in middle of TILE
			 * turn(this.getDirection(), true); }
			 */
		}
	}

	@Override
	protected void move() {
		int[] coords = translate(2, 31); // no buffer required since automated //2,31 sweeeeet spot
		
		restrict(this.getDirection());
		clip();
		turn(this.getDirection(), coords, false);
		this.px += this.vx;
		this.py += this.vy;
	}

	private boolean atBound() {
		return this.getPx() == maxX + 3 || this.getPx() == -3 || this.getPy() == maxY + 3 || this.getPy() == -3;
	}

	protected void turn(Direction d, int[] coords, boolean isCollision) {
		//int[] coords = translate(1, 31); // no buffer required since automated //2,31 sweeeeet spot
		int x1 = coords[0];
		int y1 = coords[1];
		int x2 = coords[2];
		int y2 = coords[3];

		int advance = 0;
		if (d == null) {
			return;
		}
		if (d.equals(Direction.LEFT) || d.equals(Direction.RIGHT)) {
			if (pathAbove(x1, y1, x2, y2) && takePath(Direction.UP, isCollision)) {
				System.out.println("PATH ABOVE");
				this.setVx(0);
				this.setVy(-3);
				// recenter();
				this.py -= advance;

			} else if (pathBelow(x1, y1, x2, y2) && takePath(Direction.DOWN, isCollision)) {
				System.out.println("PATH BELOW");
				this.setVx(0);
				this.setVy(3);
				// recenter();
				this.py += advance;
			} else if (!(pathAbove(x1, y1, x2, y2)) && !(pathBelow(x1, y1, x2, y2)) && isCollision) { // no path above
																										// or below
																										// reverse dir
																										// horiz.
				bounce(this.getDirection());
				System.out.println("No path ");
			}
		} else {
			if (pathRight(x1, y1, x2, y2) && takePath(Direction.RIGHT, isCollision)) {
				this.setVy(0);
				this.setVx(3);
				// recenter();
				this.px += advance;
				System.out.println("TURN RIGHT");
			} else if (pathLeft(x1, y1, x2, y2) && takePath(Direction.LEFT, isCollision)) {
				System.out.println("TURN LEFT");
				this.setVy(0);
				this.setVx(-3);
				// recenter();
				this.px -= advance;
			} else if (!(pathLeft(x1, y1, x2, y2)) && !(pathRight(x1, y1, x2, y2)) && isCollision) { // no path right or
																										// left then
																										// reverse dir
																										// vert
				System.out.println("No path ");
				bounce(this.getDirection());
				// recenter();
			}
		}

	}

	private boolean takePath(Direction d, boolean isCollision) { // d is the direction we are considering taking
		double prob = Math.random();
		if (isCollision) {
			return true;
		}
		if (chaser) {
			switch (d) {
			case UP:
				return GameCourt.pennMan.getPy() <= this.getPy(); // pennman is above us

			case DOWN:
				return GameCourt.pennMan.getPy() >= this.getPy(); // below us

			case RIGHT:
				return GameCourt.pennMan.getPx() >= this.getPx();

			case LEFT:
				return GameCourt.pennMan.getPx() <= this.getPx();

			default:
				return false;
			}
		}

		return prob >= 0.5;
	}

	// need to check that the path is above all 4 corners to ensure that the pennman
	// is
	// in the middle of the aisle.
	private boolean pathAbove(int x1, int y1, int x2, int y2) {
		if (y1 == 0) {
			return false;
		}

		return maze[y1 - 1][x1] == 0 && maze[y1 - 1][x2] == 0;// && maze[y2 - 1][x1] == 0 && maze[y2 - 1][x2] == 0;
	}

	private boolean pathBelow(int x1, int y1, int x2, int y2) {
		if (y1 == maze.length - 1) {
			return false;
		}
		return maze[y1 + 1][x1] == 0 && maze[y1 + 1][x2] == 0; // && maze[y2 + 1][x1] == 0 && maze[y2 + 1][x2] == 0;
	}

	private boolean pathLeft(int x1, int y1, int x2, int y2) {
		if (x1 == 0) {
			return false;
		}
		return maze[y1][x1 - 1] == 0 && maze[y2][x1 - 1] == 0; // && maze[y1][x2 - 1] == 0 && maze[y2][x2 - 1] == 0;
	}

	private boolean pathRight(int x1, int y1, int x2, int y2) {
		if (x1 == maze[y1].length - 1) {
			return false;
		}
		return maze[y1][x1 + 1] == 0 && maze[y2][x1 + 1] == 0; // && maze[y1][x2 + 1] == 0 && maze[y2][x2 + 1] == 0;
	}
	

	private void recenter() {
		int x1 = this.getPx();
		int y1 = this.getPy();
		int c_x1 = (int) Math.round(x1 / 32) * 32;
		int c_y1 = (int) Math.round(y1 / 32) * 32;
		this.setPx(c_x1);
		this.setPy(c_y1);

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

}
