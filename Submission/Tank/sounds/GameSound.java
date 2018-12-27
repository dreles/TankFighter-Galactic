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
import tankgame.TankGame;

/*
Sound class for playing all music and sound effects.
 */
public class GameSound {

  File soundFile;
  Clip clip;
  AudioInputStream stream;
  AudioFormat format;
  DataLine.Info info;
  long pauseTime;

  public GameSound() {
  }

  private void play( String soundsFile ) {
    soundFile = new File( soundsFile );
    try {
      stream = AudioSystem.getAudioInputStream( soundFile );
      format = stream.getFormat();
      info = new DataLine.Info( Clip.class, format );
      clip = ( Clip ) AudioSystem.getLine( info );
      clip.open( stream );
      clip.start();
    } catch( IOException | LineUnavailableException | UnsupportedAudioFileException e ) {
      System.err.println( "Audio not loaded properly." );
    }
  }

  public void menuMusic() {
    play( TankGame.getResourcesDir() + "menuMusic.wav" );
  }

  public void music() {
    play( TankGame.getResourcesDir() + "music.wav" );
  }

  public void wallHit() {
    play( TankGame.getResourcesDir() + "WallHit.wav" );
  }

  public void fire() {
    play( TankGame.getResourcesDir() + "fire.wav" );
  }

  public void tankHit() {
    play( TankGame.getResourcesDir() + "TankHit.wav" );
  }

  public void tankExplosion() {
    play( TankGame.getResourcesDir() + "TankExplosion.wav" );
  }

  public void stop() {
    clip.stop();
  }
}
