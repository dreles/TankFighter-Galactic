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
import java.util.ArrayList;
import java.util.ListIterator;
import galacticmail.*;

/**
 * Menu used to initialize the game and set player's name.
 */
public class GameMenu extends BasicObject {

  private enum MENU_STATE {
    MAIN, NAME
  }

  /*----------Objects----------*/
  protected BasicObject background;
  protected BasicObject title;
  int PANEL_WIDTH = GalacticMail.WIDTH;
  int PANEL_HEIGHT = GalacticMail.HEIGHT;
  private InputController inputController;
  private int selection = 0;
  private MENU_STATE STATE = MENU_STATE.MAIN;
  protected String name = "";

  /*----------Initialization---------*/
  public GameMenu( InputController input ) {
    this.inputController = input;
    try {
      this.background = new BasicObject( GalacticMail.getResourcesDir() + "Background.png", 0, 0 );
      this.title = new BasicObject( GalacticMail.getResourcesDir() + "Title.png", 70, 30 );
    } catch( IOException exception ) {
      System.err.println( "Menu Initialization Failed." );
    }
    location.x = 0;
    location.y = 0;
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
    background.repaint( graphics );
    title.repaint( graphics );
    graphics.setFont( new Font( "TimesRoman", Font.BOLD, 30 ) );

    if( STATE == MENU_STATE.MAIN ) {
      firstSelection( graphics );
    } else if( STATE == MENU_STATE.NAME ) {
      secondSelection( graphics );
    }
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
            if( selection == 0 ) {
              selection = 2;
            } else {
              selection--;
            }
            break;
          case ( KeyEvent.VK_DOWN ):
            if( selection == 2 ) {
              selection = 0;
            } else {
              selection++;
            }
            break;
          case ( KeyEvent.VK_ENTER ):
            switch( selection ) {
              case 0:
                STATE = MENU_STATE.NAME;
                break;
              case 1:
                GalacticMail.getGameWorld().STATE = GameWorld.GAME_STATE.SCORES;
                break;
              case 2:
                System.exit( 0 );
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
    graphics.drawString( "Play Game", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 140 );

    if( selection == 1 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "High Scores", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 100 );

    if( selection == 2 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Quit", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 60 );
  }

  private void secondSelection( Graphics graphics ) {
    ArrayList<KeyObject> keysTyped = inputController.getKeysTyped();
    ListIterator keyIterator = keysTyped.listIterator();
    while( keyIterator.hasNext() ) {
      KeyObject key = ( KeyObject ) keyIterator.next();
      if( key.pressed ) {
        switch( key.key.getKeyCode() ) {
          case ( KeyEvent.VK_ENTER ):
            GalacticMail.getGameWorld().initializeLevel();
            GalacticMail.getGameWorld().STATE = GameWorld.GAME_STATE.PLAY;
            GalacticMail.getGameWorld().setPlayerName( name );
            STATE = MENU_STATE.MAIN;
            name = "";
            break;
          case ( KeyEvent.VK_SHIFT ):
            break;
          default:
            name += key.key.getKeyChar();
            break;
        }
      }
      keyIterator.remove();
    }
    graphics.setColor( Color.WHITE );
    graphics.drawString( "Enter Name: " + name, ( PANEL_WIDTH / 2 ) - 270, PANEL_HEIGHT - 140 );
    graphics.drawString( "High Scores", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 100 );
    graphics.drawString( "Quit", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 60 );
  }
}
