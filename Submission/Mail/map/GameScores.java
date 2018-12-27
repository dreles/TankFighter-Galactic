package map;

import com.sun.glass.events.KeyEvent;
import controllers.*;
import galacticmail.GalacticMail;
import objects.BasicObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.reverseOrder;
import java.util.ListIterator;
import objects.ScoreObject;

/**
 * Prints high scores onto the screen.
 */
public class GameScores extends BasicObject {

  /*----------Objects----------*/
  protected BasicObject background;
  private InputController inputController;
  private String filePath = GalacticMail.getResourcesDir() + "scores.txt";
  private final int PANEL_WIDTH = GalacticMail.WIDTH;
  private final int PANEL_HEIGHT = GalacticMail.HEIGHT;
  private int selection = 0;
  ArrayList<ScoreObject> scoreList;

  /*----------Initialization---------*/
  public GameScores( InputController input ) {
    this.inputController = input;
    this.scoreList = new ArrayList<>();
    try {
      this.background = new BasicObject( GalacticMail.getResourcesDir() + "Background.png", 0, 0 );
    } catch( IOException exception ) {
      System.err.println( "Menu Initialization Failed." );
    }
    location.x = 0;
    location.y = 0;
  }

  /*----------File Reading----------*/
  public void readFile() {
    scoreList.clear();
    String line;
    try {
      FileReader fileReader = new FileReader( filePath );
      try( BufferedReader bufferedReader = new BufferedReader( fileReader ) ) {
        while( ( line = bufferedReader.readLine() ) != null ) {
          String[] tokenized = line.split( " " );
          ScoreObject score = new ScoreObject( tokenized[ 0 ], Integer.parseInt( tokenized[ 1 ] ) );
          scoreList.add( score );
        }
      }
    } catch( IndexOutOfBoundsException | IOException e ) {
      scoreList.clear();
      System.err.println( "File Error. Creating new one." );
      try {
        FileWriter fileWriter = new FileWriter( filePath );
        try( BufferedWriter bufferedWriter = new BufferedWriter( fileWriter ) ) {
          bufferedWriter.write( "Default 10" );
        }
      } catch( IOException e2 ) {
        System.err.println( "Error writing to High Scores file." );
      }
    }
    Collections.sort( scoreList, reverseOrder( ( ScoreObject o1, ScoreObject o2 ) -> o1.getScore().compareTo( o2.getScore() ) ) );
  }

  public void addScore( String name, int score ) {
    try {
      FileWriter fileWriter = new FileWriter( filePath, true );
      try( BufferedWriter bufferedWriter = new BufferedWriter( fileWriter ) ) {
        bufferedWriter.write( "\n" + name + " " + score );
      }
    } catch( IOException e ) {
      System.err.println( "Error writing to High Scores file." );
    }
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
    draw( g2d );
    g2d.dispose();
    graphics.drawImage( image, location.x, location.y, observer );
  }

  private void draw( Graphics graphics ) {
    background.repaint( graphics );
    graphics.setFont( new Font( "TimesRoman", Font.BOLD, 50 ) );
    graphics.drawString( "High Scores", ( PANEL_WIDTH / 2 ) - 120, 50 );
    printHighScores( graphics );
    drawSelection( graphics );
  }

  private void printHighScores( Graphics graphics ) {
    graphics.setFont( new Font( "TimesRoman", Font.PLAIN, 30 ) );
    int y = 100;
    int numScoresDisplayed = 0;
    ListIterator iterator = scoreList.listIterator();
    while( iterator.hasNext() && numScoresDisplayed < 5 ) {
      ScoreObject score = ( ScoreObject ) iterator.next();
      graphics.drawString( score.name + " " + score.score, ( PANEL_WIDTH / 2 ) - 60, y );
      y += 50;
    }
  }

  private void drawSelection( Graphics graphics ) {
    graphics.setFont( new Font( "TimesRoman", Font.BOLD, 30 ) );
    ArrayList<KeyObject> keysTyped = inputController.getKeysTyped();
    ListIterator keyIterator = keysTyped.listIterator();
    while( keyIterator.hasNext() ) {
      KeyObject key = ( KeyObject ) keyIterator.next();
      if( key.pressed ) {
        switch( key.key.getKeyCode() ) {
          case ( KeyEvent.VK_UP ):
            if( selection == 0 ) {
              selection = 1;
            } else {
              selection = 0;
            }
            break;
          case ( KeyEvent.VK_DOWN ):
            if( selection == 1 ) {
              selection = 0;
            } else {
              selection = 1;
            }
            break;
          case ( KeyEvent.VK_ENTER ):
            switch( selection ) {
              case 0:
                GalacticMail.getGameWorld().STATE = GameWorld.GAME_STATE.MENU;
                break;
              case 1:
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
    graphics.drawString( "Back to Menu", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 100 );

    if( selection == 1 ) {
      graphics.setColor( Color.RED );
    } else {
      graphics.setColor( Color.WHITE );
    }
    graphics.drawString( "Quit", ( PANEL_WIDTH / 2 ) - 75, PANEL_HEIGHT - 60 );
  }
}
