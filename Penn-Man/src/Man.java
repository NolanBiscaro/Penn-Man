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

    public Man(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

    }

    @Override
    public void draw(Graphics g) {
        Image icon = loadImage();
        g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null, null); 
        //g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight())
        
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

}
