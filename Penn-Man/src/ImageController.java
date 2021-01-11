import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageController {
    private static final String TA_FILE = "files/images/TA.png";
    private static final String SCATTER_FILE = "files/images/scatter.png";
    private static final String EXPLOSION_FILE = "files/images/boomboom.png";
    
    private static final Image TA = ImageController.loadImage(TA_FILE);
    private static final Image SCATTER = ImageController.loadImage(SCATTER_FILE);
    private static final Image EXPLOSION = ImageController.loadImage(EXPLOSION_FILE);
    
    private static final String OPEN_FILE = "files/images/man.png";
    private static final String CLOSE_FILE = "files/images/man_close.png";
    private static final Image OPEN = loadImage(OPEN_FILE);
    private static final Image CLOSE = loadImage(CLOSE_FILE);
    
    private static final String COFFEE_FILE = "files/images/coffee.png";
    private static final Image COFFEE = loadImage(COFFEE_FILE);
    
    private static final String CAUGHT_FILE = "files/images/caughtTA.png"; 
    private static final Image CAUGHT = loadImage(CAUGHT_FILE); 
    
    private static final String WIN_FILE = "files/images/winTA.png"; 
    private static final Image WIN = loadImage(WIN_FILE); 
    
    public static Image ta() {
        return TA;
    }
    
    public static Image scatter() {
        return SCATTER; 
    }
    
    public static Image explosion() {
        return EXPLOSION;
    }
    
    public static Image open() {
        return OPEN; 
    }
    
    public static Image close() {
        return CLOSE; 
    }
    
    public static Image coffee() {
        return COFFEE; 
    }
    
    public static Image caught() {
        return CAUGHT; 
    }
    
    public static Image win() {
        return WIN; 
    }
    
    protected static Image loadImage(String file) { // icon representing gameObj.
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("Error fetching image");
            e.printStackTrace();
        }
        return img;
    }

}
