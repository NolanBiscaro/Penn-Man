import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioController {
    
    private static final String CHOMP = "files/audio/pennman_chomp.wav";
    private static final String CAFFEINATE = "files/audio/caffeinate.wav"; 
    private static final String EXPLODE = "files/audio/explosion.wav"; 
    private static final String CAUGHT = "files/audio/death.wav"; 

    private static AudioInputStream chompStream = initStream(CHOMP);
    private static Clip chomp = initAudioClip(chompStream);
    
    private static AudioInputStream caffeinateStream = initStream(CAFFEINATE); 
    private static Clip caffeinate = initAudioClip(caffeinateStream); 
    
    private static AudioInputStream caughtStream = initStream(CAUGHT); 
    private static Clip death = initAudioClip(caughtStream); 
    
    private static AudioInputStream explodeStream = initStream(EXPLODE); 
    private static Clip explode = initAudioClip(explodeStream); 

    public static void chomp() { // plays sound when user takes PSet.
        chomp.setFramePosition(13000);
        chomp.start();
    }
    
    public static void caffeinate() {
        caffeinate.setFramePosition(0);
        caffeinate.start();
    }
    
    public static void decaffeinate() {
        caffeinate.stop();
        try {
            caffeinateStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void explode() {
        explode.setFramePosition(0);
        explode.start();
    }
    
    public static void caught() {
        death.setFramePosition(0);
        death.start();
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

    private static Clip initAudioClip(AudioInputStream stream) {
        Clip audioClip = null;
        try {
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine((info));
            audioClip.open(stream);
        } catch (IOException | LineUnavailableException e) {
            System.out.println("Error getting sound file");
            e.printStackTrace();
        }
        return audioClip;
    }

}
