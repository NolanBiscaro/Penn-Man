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
	private static final int TILE_SIZE = 32;
	
	String manOpen = "files/man.png";
	String manClose = "files/man_close.png";
	String chomp = "files/pennman_chomp.wav";
	
	Image open = loadImage(manOpen);
	Image close = loadImage(manClose); 
	
	Image icon = open; 
	
	Clip audioClip = initSoundFile(chomp);
	AudioInputStream stream = initStream(chomp);

	public static int score = 0;
	
	private int[][] maze = GameCourt.maze;

	public Man(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(icon, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null, null);
		g.setColor(Color.RED);
		icon = open; 
	}

	private static Image loadImage(String file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			System.out.println("Error fetching Man image");
			e.printStackTrace();
		}
		return img;
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
			icon = close; 
			audioClip.setFramePosition(11000);
			audioClip.start();
			
			System.out.println("play");
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
