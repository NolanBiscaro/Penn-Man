import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Man extends GameObj {

    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private static final int INIT_POS_X = 0;
    private static final int INIT_POS_Y = 0;
    private static final int SIZE = 30;
    private static final int TILE_SIZE = 32; 
    
    public int p_sets = 0; 
    private int[][] maze = GameCourt.maze; 
    
    int c_x = ((2 * this.getWidth() + this.getWidth()) / 2) / 32; 
    int c_y = ((2 * this.getHeight() + this.getHeight()) / 2) / 32; 

    public Man(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

    }

    @Override
    public void draw(Graphics g) {
        Image icon = loadImage();
        g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null, null); 
    }

    private static Image loadImage() {
        BufferedImage img = null;   
        try {
            img = ImageIO.read(new File("files/man.png"));
        } catch (IOException e) {
            System.out.println("Error fetching Man image");
            e.printStackTrace();
        }
        return img;
    }
    
    public boolean takePSet(Graphics g) {
		if (maze[c_x][c_y] == 0) {
			System.out.println("found a pset");
			maze[c_x][c_y] = -1; 
			g.setColor(Color.black);
			g.fillRect(c_x * TILE_SIZE, c_y * TILE_SIZE, TILE_SIZE, TILE_SIZE); 
			
		}
		return false; 
	}
    

}
