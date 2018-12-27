package map;

import animations.Explosion;
import controllers.*;
import objects.*;
import galacticmail.*;
import sounds.*;
import collision.*;
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
import java.util.Random;
import javax.swing.JPanel;

/**
 * Houses the basic map for the game.
 */
public class GameWorld extends JPanel implements Runnable {

  private static final int MAP_WIDTH = 640; //640
  private static final int MAP_HEIGHT = 480; //480

  public enum GAME_STATE {
    MENU, PLAY, SCORES
  }

  /*----------Objects & Controllers----------*/
  private final String BACKGROUND_FILEPATH = GalacticMail.getResourcesDir() + "Background.png";
  private Thread thread;
  protected BasicObject background;
  public GameClock clock = new GameClock();
  private BufferedImage frame;
  private PlayerObject player;
  private final GameMenu gameMenu;
  private final InputController inputController;
  private final GameSound sounds = new GameSound();
  public GAME_STATE STATE = GAME_STATE.MENU;
  private MoonObject moon;
  protected static ArrayList<MoonObject> moonList;
  protected static ArrayList<AsteroidObject> asteroidList;
  protected static ArrayList<Explosion> explosionList;
  protected Collision collision;
  protected int level = 1;
  protected int asteroidCount = 3;
  protected Explosion explosion;
  protected GameScores gameScores;


  /*----------Initialization----------*/
  public GameWorld() {
    inputController = new InputController();
    gameMenu = new GameMenu( inputController );
    gameScores = new GameScores( inputController );
    gameScores.readFile();
    sounds.music();
  }

  public void initialize() {
    this.setFocusable( true );
    setPreferredSize( new Dimension( MAP_WIDTH, MAP_HEIGHT ) );
    addKeyListener( inputController );
    frame = ( BufferedImage ) createImage( MAP_WIDTH, MAP_HEIGHT );
  }

  public void initializeLevel() {
    moonList = new ArrayList<>();
    asteroidList = new ArrayList<>();
    explosionList = new ArrayList<>();

    try {
      this.background = new BasicObject( BACKGROUND_FILEPATH, 0, 0 );
      this.player = new PlayerObject( MAP_WIDTH - 90, MAP_HEIGHT - 95, 220, clock, inputController );
      moonList.add( new MoonObject( 48, 80, "random" ) );
      moonList.add( new MoonObject( 80, 120 * 3, "random" ) );
      moonList.add( new MoonObject( 120 * 3, 60, "random" ) );
      moonList.add( new MoonObject( MAP_WIDTH - 100, MAP_HEIGHT - 100, "home" ) );

      for( int i = 0; i < asteroidCount; i++ ) {
        Random r = new Random();

        switch( r.nextInt( 3 ) ) {
          case 0:
            r = new Random();
            asteroidList.add( new AsteroidObject( -50, r.nextInt( MAP_HEIGHT ), clock ) );
            break;
          case 1:
            r = new Random();
            asteroidList.add( new AsteroidObject( r.nextInt( MAP_WIDTH ), - 50, clock ) );
            break;
          case 2:
            r = new Random();
            asteroidList.add( new AsteroidObject( MAP_WIDTH + 10, r.nextInt( MAP_HEIGHT ), clock ) );
            break;
          case 3:
            r = new Random();
            asteroidList.add( new AsteroidObject( r.nextInt( MAP_WIDTH ), MAP_HEIGHT + 10, clock ) );
            break;
        }

      }
    } catch( IOException exception ) {
      System.err.println( "Local Map Initialization Failed." );
    }
    collision = new Collision( player, asteroidList, moonList, clock );
  }

  public void initializeEndScores() {
    gameScores.addScore( player.getName(), player.getMoney() );
    gameScores.readFile();
  }

  /*----------Drawing----------*/
  private void drawMap( Graphics2D graphics ) {
    //Print the background
    background.repaint( graphics );

    if( collision.isOnMoon() && player.justLaunched() ) {
      collision.removeMoon();
    }

    ListIterator<?> iterator;

    //Print Moons/Bases
    iterator = moonList.listIterator();
    while( iterator.hasNext() ) {
      moon = ( MoonObject ) iterator.next();
      moon.repaint( graphics );
    }

    //Print player & update position
    player.repaint( graphics );
    if( player.getX() >= MAP_WIDTH + 10 ) {
      player.setX( -50 );
    } else if( player.getX() <= -50 ) {
      player.setX( MAP_WIDTH + 10 );
    }
    if( player.getY() >= MAP_HEIGHT + 10 ) {
      player.setY( -50 );
    } else if( player.getY() <= -50 ) {
      player.setY( MAP_HEIGHT + 10 );
    }

    //Print asteroids & update position
    iterator = asteroidList.listIterator();
    while( iterator.hasNext() ) {
      AsteroidObject asteroid = ( AsteroidObject ) iterator.next();
      asteroid.repaint( graphics );
      if( asteroid.getX() >= MAP_WIDTH + 10 ) {
        asteroid.setX( -50 );
      } else if( asteroid.getX() <= -50 ) {
        asteroid.setX( MAP_WIDTH + 10 );
      }
      if( asteroid.getY() >= MAP_HEIGHT + 10 ) {
        asteroid.setY( -50 );
      } else if( asteroid.getY() <= -50 ) {
        asteroid.setY( MAP_HEIGHT + 10 );
      }
    }

    //Print Player's Money
    Integer playerMoney = player.getMoney();
    graphics.setColor( Color.WHITE );
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 30 ) );
    graphics.drawString( "$" + playerMoney.toString(), 3, MAP_HEIGHT - 20 );

    //Print level 
    graphics.setColor( Color.GREEN );
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 30 ) );
    graphics.drawString( "level:" + Integer.toString( level ), MAP_WIDTH - 100, MAP_HEIGHT - 20 );

    //Frame Number (for testing)
    Integer frameNumber = clock.getFrame();
    graphics.setColor( Color.WHITE );
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 12 ) );
    graphics.drawString( frameNumber.toString(), 3, 10 );

    if( collision.asteroidCollision() ) {
      explosionList.add( new Explosion( player.getX(), player.getY(), this ) );
    }

    iterator = explosionList.listIterator();
    while( iterator.hasNext() ) {
      explosion = ( Explosion ) iterator.next();
      explosion.repaint( graphics );
    }

    changeLevel();
  }

  @Override
  public void paint( Graphics graphics ) {
    if( null == STATE ) {
      clock.tick();
      Graphics2D gInitial = createGraphics2D( MAP_WIDTH, MAP_HEIGHT, frame );
      drawMap( gInitial );
      gInitial.dispose();
      graphics.drawImage( frame, 0, 0, this );
    } else {
      switch( STATE ) {
        case MENU:
          gameMenu.repaint( graphics );
          break;
        case SCORES:
          gameScores.repaint( graphics );
          break;
        default:
          clock.tick();
          Graphics2D gInitial = createGraphics2D( MAP_WIDTH, MAP_HEIGHT, frame );
          drawMap( gInitial );
          gInitial.dispose();
          graphics.drawImage( frame, 0, 0, this );
          break;
      }
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
        Thread.sleep( 20 ); //pause to have a max of 60 fps
      } catch( InterruptedException e ) {
        break;
      }
    }
  }

  public void changeLevel() {
    if( moonList.size() == 2 && collision.moonCollision() ) {
      level++;
      asteroidCount++;
      initializeLevel();
    }
  }


  /*----------Getters & Setters----------*/
  public static int getMapHeight() {
    return MAP_HEIGHT;
  }

  public static int getMapWidth() {
    return MAP_WIDTH;
  }

  public void setPlayerName( String name ) {
    player.setName( name );
  }

  public void setLevel( int level ) {
    this.level = level;
  }
}
