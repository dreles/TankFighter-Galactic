package map;

import player.WinnerDisplay;
import player.PlayerView;
import animations.Explosion;
import controllers.*;
import objects.*;
import tankgame.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JPanel;
import sounds.GameSound;

/*
Houses the basic map for the game.
 */
public class GameWorld extends JPanel implements Runnable {

  private static final int MAP_WIDTH = 1280; //1280
  private static final int MAP_HEIGHT = 1280; //1280

  public enum GAME_STATE {
    MENU,
    LOCAL,
    HOST,
    CONNECT
  }

  /*----------Objects----------*/
  private final String BACKGROUND_FILEPATH = TankGame.getResourcesDir() + "background_tile.png";
  private Thread thread;
  protected BasicObject background;
  public GameClock clock = new GameClock();
  private BufferedImage initialFrame;
  private BufferedImage finalFrame;
  static ArrayList<TankObject> tanks;
  private static WallList wallList;
  private CollisionDetector collision;
  public static ArrayList<Explosion> explosions;
  private PlayerView playerView;
  private WinnerDisplay winDisplay;
  private final GameMenu gameMenu;
  private InputController inputController;
  private final GameSound sounds = new GameSound();
  public GAME_STATE STATE = GAME_STATE.MENU;


  /*----------Initialization----------*/
  public GameWorld() {
    inputController = new InputController();
    gameMenu = new GameMenu( inputController );
    sounds.menuMusic();
  }

  public void initialize() {
    this.setFocusable( true );
    setPreferredSize( new Dimension( MAP_WIDTH, MAP_HEIGHT ) );
    addKeyListener( inputController );
    initialFrame = ( BufferedImage ) createImage( MAP_WIDTH, MAP_HEIGHT );
    finalFrame = ( BufferedImage ) createImage( TankGame.WIDTH, TankGame.HEIGHT );
  }

  public void localInitializer() {
    sounds.stop();
    tanks = new ArrayList<>();
    explosions = new ArrayList<>();
    try {
      this.background = new BasicObject( BACKGROUND_FILEPATH, 0, 0 );
      wallList = new WallList();
      tanks.add( new TankObject( 500, 50, 180, "tankOne", clock, inputController ) );// (500, 50)
      tanks.add( new TankObject( 500, 1170, 0, "tankTwo", clock, inputController ) );// (500, 1170) 
      collision = new CollisionDetector( tanks, wallList, this );
      playerView = new PlayerView( tanks );
      winDisplay = new WinnerDisplay( tanks );
      inputController = new InputController( tanks );
      addKeyListener( inputController );
    } catch( IOException exception ) {
      System.err.println( "Local Map Initialization Failed." );
    }
    sounds.music();
  }

  public void hostInitializer() {
    sounds.stop();
    tanks = new ArrayList<>();
    explosions = new ArrayList<>();
    try {
      this.background = new BasicObject( BACKGROUND_FILEPATH, 0, 0 );
      wallList = new WallList();
      tanks.add( new TankObject( 500, 50, 180, "tankOne", clock, inputController ) );// (500, 50)
      tanks.add( new TankObject( 500, 1170, 0, "tankTwo", clock, inputController ) );// (500, 1170) 
      collision = new CollisionDetector( tanks, wallList, this );
      playerView = new PlayerView( tanks );
      winDisplay = new WinnerDisplay( tanks );
      inputController = new InputController( tanks );
      addKeyListener( inputController );
    } catch( IOException exception ) {
      System.err.println( "Host Map Initialization Failed." );
    }
    sounds.music();
  }

  public void connectInitializer() {
    sounds.stop();
    tanks = new ArrayList<>();
    explosions = new ArrayList<>();
    try {
      this.background = new BasicObject( BACKGROUND_FILEPATH, 0, 0 );
      wallList = new WallList();
      tanks.add( new TankObject( 500, 50, 180, "tankOne", clock, inputController ) );// (500, 50)
      tanks.add( new TankObject( 500, 1170, 0, "tankTwo", clock, inputController ) );// (500, 1170) 
      collision = new CollisionDetector( tanks, wallList, this );
      playerView = new PlayerView( tanks );
      winDisplay = new WinnerDisplay( tanks );
      inputController = new InputController( tanks );
      addKeyListener( inputController );
    } catch( IOException exception ) {
      System.err.println( "Connection Map Initialization Failed." );
    }
    sounds.music();
  }

  /*----------Drawing----------*/
  private void drawMap( Graphics2D graphics ) {
    //Print the background
    for( int x = 0; x < MAP_WIDTH; x += background.getWidth() ) {
      for( int y = 0; y < MAP_HEIGHT; y += background.getHeight() ) {
        background.setLocation( x, y );
        background.repaint( graphics );
      }
    }

    //Check for collision
    collision.TankMovementPossible();
    collision.bulletTankCollision();
    collision.bulletCollision();

    ListIterator<?> iterator;

    //Print the walls
    iterator = getWallIterator();
    while( iterator.hasNext() ) {
      WallObject wall = ( WallObject ) iterator.next();
      wall.repaint( graphics );
    }

    //Print the bullets
    iterator = getTankIterator();
    while( iterator.hasNext() ) {
      TankObject tank = ( TankObject ) iterator.next();
      ArrayList<BulletObject> tanksBullets = tank.getBullets();
      ListIterator bulletIterator = tanksBullets.listIterator();
      while( bulletIterator.hasNext() ) {
        BulletObject bullet = ( BulletObject ) bulletIterator.next();
        bullet.repaint( graphics );
        bullet.move();
      }
    }

    //Print the tanks
    iterator = getTankIterator();
    while( iterator.hasNext() ) {
      TankObject tank = ( TankObject ) iterator.next();
      tank.repaint( graphics );
    }

    //Print explosions
    iterator = getExplosionIterator();
    while( iterator.hasNext() ) {
      Explosion explosion = ( Explosion ) iterator.next();
      if( !explosion.isDone() ) {
        explosion.repaint( graphics );
      }
    }
  }

  private void drawFinalFrame( Graphics2D graphics ) {
    //Player Views
    if( STATE == GAME_STATE.LOCAL ) {
      playerView.createTwoPlayerViews( initialFrame );
    } else {
      playerView.createSinglePlayerView( initialFrame );
    }
    playerView.repaint( graphics );

    //Check for Winner
    if( tanks.get( 0 ).isDead() || tanks.get( 1 ).isDead() ) {
      winDisplay.repaint( graphics );
      removeKeyListener( inputController );
      ListIterator iterator = getTankIterator();
      while( iterator.hasNext() ) {
        TankObject tank = ( TankObject ) iterator.next();
        tank.setIsFiring( false );
        tank.setIsMovingBackward( false );
        tank.setIsMovingForward( false );
        tank.setIsTurningLeft( false );
        tank.setIsTurningRight( false );
      }
    }

    //Frame Number (for testing)
    Integer frame = clock.getFrame();
    graphics.setColor( Color.WHITE );
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 12 ) );
    graphics.drawString( frame.toString(), 3, 10 );
  }

  @Override
  public void paint( Graphics graphics ) {
    if( STATE == GAME_STATE.MENU ) {
      gameMenu.repaint( graphics );
    } else {
      clock.tick();
      Graphics2D gInitial = createGraphics2D( MAP_WIDTH, MAP_HEIGHT, initialFrame );
      drawMap( gInitial );
      gInitial.dispose();
      Graphics2D gFinal = createGraphics2D( TankGame.WIDTH, TankGame.HEIGHT, finalFrame );
      drawFinalFrame( gFinal );
      gFinal.dispose();
      graphics.drawImage( finalFrame, 0, 0, this );
    }
  }

  private Graphics2D createGraphics2D( int w, int h, BufferedImage image ) {
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    Graphics2D g2;
    if( image == null || image.getWidth() != w || image.getHeight() != h ) {
      image = ( BufferedImage ) createImage( w, h );
    }
    g2 = image.createGraphics();
    g2.setBackground( getBackground() );
    g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
    g2.clearRect( 0, 0, w, h );
    g2.setRenderingHints( rh );
    return g2;
  }

  /*----------Game Threads----------*/
  public void start() {
    thread = new Thread( this );
    thread.setPriority( Thread.MAX_PRIORITY );
    thread.start();
  }

  @Override
  public void run() {
    Thread me = Thread.currentThread();
    while( thread == me ) {
      this.requestFocusInWindow();
      repaint();
      try {
        Thread.sleep( 16 ); //pause to have a max of 60 fps
      } catch( InterruptedException e ) {
        break;
      }
    }
  }

  /*----------Getters & Setters----------*/
  public ArrayList<TankObject> getTanks() {
    return GameWorld.tanks;
  }

  public ListIterator<TankObject> getTankIterator() {
    return tanks.listIterator();
  }

  public ListIterator<WallObject> getWallIterator() {
    return wallList.listIterator();
  }

  public ListIterator<Explosion> getExplosionIterator() {
    return explosions.listIterator();
  }

  public static int getMapHeight() {
    return MAP_HEIGHT;
  }

  public static int getMapWidth() {
    return MAP_WIDTH;
  }

}
