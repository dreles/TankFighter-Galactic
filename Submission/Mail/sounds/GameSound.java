package sounds;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import galacticmail.*;

/*
Sound class for playing all music and sound effects.
 */
public class GameSound {

  File musicFile, explosionFile, launchFile, bonusFile;
  Clip clip;
  AudioInputStream stream;
  AudioFormat format;
  DataLine.Info info;
  long pauseTime;

  public GameSound() {
    musicFile = new File(GalacticMail.getResourcesDir() + "Music.wav");
    explosionFile = new File(GalacticMail.getResourcesDir() + "Explosion.wav");
    launchFile = new File(GalacticMail.getResourcesDir() + "Launch.wav" );
    bonusFile = new File(GalacticMail.getResourcesDir() + "Bonus.wav" );
  }

  private void play( File soundsFile ) {
    try {
      stream = AudioSystem.getAudioInputStream( soundsFile );
      format = stream.getFormat();
      info = new DataLine.Info( Clip.class, format );
      clip = ( Clip ) AudioSystem.getLine( info );
      clip.open( stream );
      clip.start();
    } catch( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
      System.err.println( "Audio not loaded properly." );
    }
  }

  public void music() {
    play( musicFile );
  }

  public void explosion() {
    play( explosionFile );
  }

  public void launch() {
    play( launchFile );
  }

  public void bonus() {
    play( bonusFile );
  }

  public void stop() {
    clip.stop();
  }
}
