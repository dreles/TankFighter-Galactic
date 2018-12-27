package tankgame;

import java.io.File;
import map.*;

/*
Written by: Andre Leslie & Joshua Lizak
Class: CSC 413 - 04
Prof: JRob
 */
public class TankGame {

  public static int WIDTH = 800; //800
  public static int HEIGHT = 600; //600
  public static String frameTitle = "Tank Game";
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
    return TankGame.game;
  }

  public static String getResourcesDir() {
    return RESOURCES_DIR;
  }
}
