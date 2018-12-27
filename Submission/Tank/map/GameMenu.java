package map;

import controllers.KeyObject;
import controllers.*;
import objects.*;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import tankgame.TankGame;

public class GameMenu extends BasicObject {

  /*----------Objects----------*/
  private enum MENU_STATE {
    FIRST,
    HOST,
    FIND
  }
  protected BasicObject background;
  private GameClock menuClock = new GameClock();
  private TankObject tank;
  int PANEL_WIDTH = TankGame.WIDTH;
  int PANEL_HEIGHT = TankGame.HEIGHT - 20;
  private InputController inputController;
  private String port = "";
  private String ipAddress = "";
  private int findPosition = 0;
  private int selection = 0;
  private MENU_STATE STATE = MENU_STATE.FIRST;

  /*----------Initialization---------*/
  public GameMenu( InputController input ) {
    this.inputController = input;
    try {
      this.background = new BasicObject( TankGame.getResourcesDir() + "background_tile.png", 0, 0 );
      tank = new TankObject( -300, PANEL_HEIGHT - ( PANEL_HEIGHT / 4 ), 0, "tankOne", menuClock, inputController );
    } catch( IOException exception ) {
      System.err.println( "Menu Initialization Failed." );
    }
    location.x = 0;
    location.y = 0;
  }

  /*----------Selection Updater----------*/
  private void firstSelection( Graphics graphics ) {
    ArrayList<KeyObject> keysTyped = inputController.getKeysTyped();
    ListIterator keyIterator = keysTyped.listIterator();
    while( keyIterator.hasNext() ) {
      KeyObject key = ( KeyObject ) keyIterator.next();
      if( key.pressed ) {
        switch( key.key.getKeyCode() ) {
          case ( KeyEvent.VK_UP ):
            if( selection > 0 ) {
              selection--;
            } else {
              selection = 2;
            }
            break;
          case ( KeyEvent.VK_DOWN ):
            if( selection < 2 ) {
              selection++;
            } else {
              selection = 0;
            }
            break;
          case ( KeyEvent.VK_ENTER ):
            switch( selection ) {
              case 0:
                TankGame.getGameWorld().STATE = GameWorld.GAME_STATE.LOCAL;
                TankGame.getGameWorld().localInitializer();
                break;
              case 1:
                STATE = MENU_STATE.HOST;
                break;
              case 2:
                STATE = MENU_STATE.FIND;
                break;
              default:
                break;
            }
            selection = 0;
            break;
        }
      }
      keyIterator.remove();
    }

    if( selection == 0 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Local Play", ( PANEL_WIDTH / 2 ) - 70, ( PANEL_HEIGHT / 2 ) - 30 );

    if( selection == 1 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Host Game", ( PANEL_WIDTH / 2 ) - 75, ( PANEL_HEIGHT / 2 ) + 40 );

    if( selection == 2 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Find Game", ( PANEL_WIDTH / 2 ) - 75, ( PANEL_HEIGHT / 2 ) + 110 );
  }

  private void hostSelection( Graphics graphics ) {
    ArrayList<KeyObject> keysTyped = inputController.getKeysTyped();
    ListIterator keyIterator = keysTyped.listIterator();
    while( keyIterator.hasNext() ) {
      KeyObject key = ( KeyObject ) keyIterator.next();
      if( key.pressed ) {
        switch( key.key.getKeyCode() ) {
          case ( KeyEvent.VK_1 ):
            port = port + "1";
            break;
          case ( KeyEvent.VK_2 ):
            port = port + "2";
            break;
          case ( KeyEvent.VK_3 ):
            port = port + "3";
            break;
          case ( KeyEvent.VK_4 ):
            port = port + "4";
            break;
          case ( KeyEvent.VK_5 ):
            port = port + "5";
            break;
          case ( KeyEvent.VK_6 ):
            port = port + "6";
            break;
          case ( KeyEvent.VK_7 ):
            port = port + "7";
            break;
          case ( KeyEvent.VK_8 ):
            port = port + "8";
            break;
          case ( KeyEvent.VK_9 ):
            port = port + "9";
            break;
          case ( KeyEvent.VK_0 ):
            port = port + "0";
            break;
          case ( KeyEvent.VK_ENTER ):
            TankGame.getGameWorld().STATE = GameWorld.GAME_STATE.HOST;
            TankGame.getGameWorld().hostInitializer();
        }
      }
      keyIterator.remove();
    }

    InetAddress localHost;
    try {
      localHost = InetAddress.getLocalHost();
      ipAddress = localHost.getHostAddress().trim();
    } catch( UnknownHostException ex ) {
      System.err.println( "Couldn't get IP address." );
      ipAddress = "";
    }
    graphics.setColor( Color.WHITE );
    graphics.drawString( "Your IP: " + ipAddress, ( PANEL_WIDTH / 2 ) - 150, ( PANEL_HEIGHT / 2 ) - 30 );
    graphics.drawString( "Enter PORT: " + port, ( PANEL_WIDTH / 2 ) - 150, ( PANEL_HEIGHT / 2 ) + 40 );
  }

  private void findSelection( Graphics graphics ) {
    ArrayList<KeyObject> keysTyped = inputController.getKeysTyped();
    ListIterator keyIterator = keysTyped.listIterator();

    if( findPosition == 0 ) {
      while( keyIterator.hasNext() ) {
        KeyObject key = ( KeyObject ) keyIterator.next();
        if( key.pressed ) {
          switch( key.key.getKeyCode() ) {
            case ( KeyEvent.VK_PERIOD ):
              ipAddress = ipAddress + ".";
              break;
            case ( KeyEvent.VK_1 ):
              ipAddress = ipAddress + "1";
              break;
            case ( KeyEvent.VK_2 ):
              ipAddress = ipAddress + "2";
              break;
            case ( KeyEvent.VK_3 ):
              ipAddress = ipAddress + "3";
              break;
            case ( KeyEvent.VK_4 ):
              ipAddress = ipAddress + "4";
              break;
            case ( KeyEvent.VK_5 ):
              ipAddress = ipAddress + "5";
              break;
            case ( KeyEvent.VK_6 ):
              ipAddress = ipAddress + "6";
              break;
            case ( KeyEvent.VK_7 ):
              ipAddress = ipAddress + "7";
              break;
            case ( KeyEvent.VK_8 ):
              ipAddress = ipAddress + "8";
              break;
            case ( KeyEvent.VK_9 ):
              ipAddress = ipAddress + "9";
              break;
            case ( KeyEvent.VK_0 ):
              ipAddress = ipAddress + "0";
              break;
            case ( KeyEvent.VK_ENTER ):
              findPosition++;
          }
        }
        keyIterator.remove();
      }
    } else {
      while( keyIterator.hasNext() ) {
        KeyObject key = ( KeyObject ) keyIterator.next();
        if( key.pressed ) {
          switch( key.key.getKeyCode() ) {
            case ( KeyEvent.VK_PERIOD ):
              port = port + ".";
              break;
            case ( KeyEvent.VK_1 ):
              port = port + "1";
              break;
            case ( KeyEvent.VK_2 ):
              port = port + "2";
              break;
            case ( KeyEvent.VK_3 ):
              port = port + "3";
              break;
            case ( KeyEvent.VK_4 ):
              port = port + "4";
              break;
            case ( KeyEvent.VK_5 ):
              port = port + "5";
              break;
            case ( KeyEvent.VK_6 ):
              port = port + "6";
              break;
            case ( KeyEvent.VK_7 ):
              port = port + "7";
              break;
            case ( KeyEvent.VK_8 ):
              port = port + "8";
              break;
            case ( KeyEvent.VK_9 ):
              port = port + "9";
              break;
            case ( KeyEvent.VK_0 ):
              port = port + "0";
              break;
            case ( KeyEvent.VK_ENTER ):
              TankGame.getGameWorld().STATE = GameWorld.GAME_STATE.CONNECT;
              TankGame.getGameWorld().connectInitializer();
          }
        }
        keyIterator.remove();
      }
    }

    if( findPosition == 0 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Enter IP: " + ipAddress, ( PANEL_WIDTH / 2 ) - 150, ( PANEL_HEIGHT / 2 ) - 30 );

    if( findPosition > 0 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Enter PORT: " + port, ( PANEL_WIDTH / 2 ) - 150, ( PANEL_HEIGHT / 2 ) + 40 );
  }

  /*----------Drawing----------*/
  @Override
  public void repaint( Graphics graphics ) {
    this.image = new BufferedImage( PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB );
    Graphics2D g2d = image.createGraphics();
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    g2d.setRenderingHints( rh );
    drawMenu( g2d );
    g2d.dispose();
    graphics.drawImage( image, location.x, location.y, observer );
  }

  private void drawMenu( Graphics graphics ) {
    //Print the background
    for( int x = 0; x < PANEL_WIDTH; x += background.getWidth() ) {
      for( int y = 0; y < PANEL_HEIGHT; y += background.getHeight() ) {
        background.setLocation( x, y );
        background.repaint( graphics );
      }
    }

    //Print the tank
    tank.repaint( graphics );
    if( tank.getX() < PANEL_WIDTH + 100 ) {
      tank.moveForwardSlowly();
    }

    //Print the title
    graphics.setColor( Color.WHITE );
    graphics.setFont( new Font( "TimesRoman", Font.BOLD, 80 ) );
    graphics.drawString( "Tank Game", ( PANEL_WIDTH / 2 ) - 200, ( PANEL_HEIGHT / 2 ) - 100 );

    //Print the Options
    graphics.setFont( new Font( "TimesRoman", Font.BOLD, 30 ) );
    switch( STATE ) {
      case FIRST:
        firstSelection( graphics );
        break;
      case HOST:
        hostSelection( graphics );
        break;
      case FIND:
        findSelection( graphics );
        break;
      default:
        break;
    }

    //Frame Number (for testing)
    Integer frameNum = menuClock.getFrame();
    graphics.setColor( Color.WHITE );
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 12 ) );
    graphics.drawString( frameNum.toString(), 3, 10 );
  }
}
