import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Man extends GameObj {

	private static final int INIT_VEL_X = 0;
	private static final int INIT_VEL_Y = 0;
	private static final int INIT_POS_X = 0;
	private static final int INIT_POS_Y = 0;
	private static final int SIZE = 30;
	protected static final int TILE_SIZE = 32;
	
	String manOpen = "files/man.png";
	String manClose = "files/man_close.png";
	String chomp = "files/pennman_chomp.wav";
	
	Image open = loadImage(manOpen);
	Image close = loadImage(manClose); 
	
	Image icon = open; 
	
	Clip audioClip = initSoundFile(chomp);
	AudioInputStream stream = initStream(chomp);

	public static int score = 0;
	
	private static int[][] maze = GameCourt.maze;

	public Man() {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, GameCourt.COURT_WIDTH, GameCourt.COURT_HEIGHT);

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null, null);
		g.setColor(Color.RED);
		icon = open; 
	}

	

	public void takePSet() {
		int x1 = this.getPx();
		int y1 = this.getPy();
		int x2 = (x1 + TILE_SIZE);
		int y2 = (y1 + TILE_SIZE);

		int c_x = ((x1 + x2) / 2) / TILE_SIZE;
		int c_y = ((y1 + y2) / 2) / TILE_SIZE;

		if (maze[c_y][c_x] == 0) {
			maze[c_y][c_x] = -1;
			score += 1; 
			Game.updateScore(score);
			icon = close; 
			audioClip.setFramePosition(11000);
			audioClip.start();
			
			System.out.println("play");
		}
	}
	
	@Override
	protected void move() {
		this.px += this.vx;
		this.py += this.vy;
		clip();
		restrict(this.getDirection());
	}
	
	//@Override
	protected void restrict(Direction d) {
		int leftBuff = 6; 
		int rightBuff = 26; 
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

		if (collision(x1, y1, x2, y2, 1)) { // 1 is target because looking for walls
			switch (d) {
			case UP:
				GameCourt.upLock = true;
				this.py += 4;
				this.setVy(0);
				break;

			case DOWN:
				GameCourt.downLock = true;
				this.py -= 4;
				this.setVy(0);
				break;

			case RIGHT:
				GameCourt.rightLock = true;
				this.px -= 4;
				this.setVx(0);
				break;

			case LEFT:
				GameCourt.leftLock = true;
				this.px += 4;
				this.setVx(0);
				break;

			default:
				break;
			}

		} else

		{
			GameCourt.resetLocks();
		}

	}

	private static AudioInputStream initStream(String file) {
		File audioFile = new File(file);
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(audioFile);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
