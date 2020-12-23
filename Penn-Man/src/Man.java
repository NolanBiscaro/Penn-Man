import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Man extends GameObj {

    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_POS_X = 31;
    public static final int INIT_POS_Y = 3;
    private static final int SIZE = 30;
    protected static final int TILE_SIZE = 32;
    
    /*
     * Inital score and lives that the user begins with. 
     */
    private static int score = 0;
    private static int lives = 3;
    
    /*
     * the following booleans are used to lock the arrow keys.
     * Upon collision with one of the maze walls we can simply lock
     * the arrow keys so the user can't travel any further, and 
     * is restricted to the walls of the maze. 
     */
    boolean upLock = false;
    boolean downLock = false;
    boolean leftLock = false; 
    boolean rightLock = false; 

    /* 
     * manOpen corresponds to the avatar with its 
     * mouth open. Similar for manClose. Switching back
     * and forth between the two simulates eating.
     */
    String manOpen = "files/man.png";
    String manClose = "files/man_close.png";
    String chomp = "files/pennman_chomp.wav";

    Image open = loadImage(manOpen);
    Image close = loadImage(manClose);
    Image icon = open;

    Clip audioClip = initSoundFile(chomp);
    AudioInputStream stream = initStream(chomp);

    private static int[][] maze = GameCourt.MAZE;

    public Man(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtHeight,
                courtHeight);

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(icon, this.getPx(), this.getPy(),
                this.getWidth(), this.getHeight(), null, null);
        g.setColor(Color.RED);
        icon = open;
    }

    public void takePSet() {
        int x1 = this.getPx();
        int y1 = this.getPy();
        int x2 = (x1 + TILE_SIZE);
        int y2 = (y1 + TILE_SIZE);
        
        //get center position of tile
        int centerX = ((x1 + x2) / 2) / TILE_SIZE;
        int centerY = ((y1 + y2) / 2) / TILE_SIZE;

        //check for p-set
        if (maze[centerY][centerX] == 0) {
            maze[centerY][centerX] = -1;
            score += 1;
            Game.updateScore(score);
            icon = close;
            audioClip.setFramePosition(11000);
            audioClip.start();
        }
    }

    @Override
    public void restrict(Direction d) {
        int leftBuff = 3;
        int rightBuff = 30;
        int[] coords = translate(leftBuff, rightBuff);

        // top left
        int x1 = coords[0];
        int y1 = coords[1];

        // bottom right
        int x2 = coords[2];
        int y2 = coords[3];

        if (d == null) {
            return;
        }
        
        // 1 is target because looking for walls
        if (collision(x1, y1, x2, y2, 1)) { 
            System.out.println("colliding");
            switch (d) {
                case UP:
                    upLock = true; 
                    this.setPy(this.getPy() + 4);
                    this.setVy(0);
                    break;
    
                case DOWN:
                    downLock = true; 
                    this.setPy(this.getPy() - 4);
                    this.setVy(0);
                    break;
    
                case RIGHT:
                    rightLock = true;
                    this.setPx(this.getPx() - 4);
                    this.setVx(0);
                    break;
    
                case LEFT:
                    leftLock = true; 
                    this.setPx(this.getPx() + 4);
                    this.setVx(0);
                    break;
    
                default:
                    break;
            }

        } else {
            resetLocks();
        }

    }
    
    public void resetLocks() {
        upLock = false;
        downLock = false; 
        leftLock = false; 
        rightLock = false; 
    }
    
    public static int getScore() {
        return score; 
    }
    
    public static int getLives() {
        return lives; 
    }
    
    public static void setLives(int l) { 
        lives = l; 
    }
    
    public static void setScore(int s) {
        score = s;
    }

    private static AudioInputStream initStream(String file) {
        File audioFile = new File(file);
        AudioInputStream stream = null;
        try {
            stream = AudioSystem.getAudioInputStream(audioFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    private Clip initSoundFile(String file) {
        Clip audioClip = null;
        try {
            File audioFile = new File(file);
            stream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine((info));
            audioClip.open(stream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Error getting sound file");
            e.printStackTrace();
        }
        return audioClip;
    }

}
