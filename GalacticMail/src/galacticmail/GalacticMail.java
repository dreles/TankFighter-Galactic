package galacticmail;

import java.io.File;
import map.*;

/**
 * Written by: Andre Leslie & Joshua Lizak Class: CSC 413 - 04 Prof: JRob
 */
public class GalacticMail {

  public static int WIDTH = 640; //640
  public static int HEIGHT = 500; //480+20 (compensate for titlebar)
  public static String frameTitle = "Galactic Mail";

  private static final String RESOURCES_DIR = System.getProperty( "user.dir" ) + File.separator + "resources" + File.separator;
  static GameWorld game;
  static GameFrame frame;

  public static void main( String[] args ) {
    game = new GameWorld();
    frame = new GameFrame( game );
    frame.initialize( frameTitle, WIDTH, HEIGHT );
    game.initialize();
    game.start();
  }

  public static GameWorld getGameWorld() {
    return GalacticMail.game;
  }

  public static String getResourcesDir() {
    return RESOURCES_DIR;
  }
}
