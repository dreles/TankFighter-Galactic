package objects;

import controllers.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import galacticmail.*;
import java.awt.event.KeyEvent;
import java.util.ListIterator;
import sounds.GameSound;

/**
 * Object for the player's ship.
 */
public class PlayerObject extends BasicObject implements Observer {

  protected final Dimension size = new Dimension( 48, 48 );

  /*----------Objects & Variables----------*/
  protected int angle;
  protected static String name = "";
  protected InputController inputController;
  protected int health;
  static protected int money;
  protected String SHIP_IMAGE = GalacticMail.getResourcesDir() + "ship.png";
  protected Graphics2D g2d;
  protected boolean isStopped;
  protected boolean isMoving;
  protected boolean isAlive;
  protected boolean moveForwardPossible;
  protected boolean isMovingForward;
  protected boolean isTurningLeft;
  protected boolean isTurningRight;
  protected boolean justLaunched;
  protected GameClock clock;

  /*----------Initialization----------*/
  public PlayerObject( int x, int y, int angle, GameClock clock, InputController inputController ) throws IOException {
    this.inputController = inputController;
    clock.addObserver( PlayerObject.this );
    this.clock = clock;
    location = new Rectangle( x, y, size.width, size.height );
    this.angle = angle;
    image = ImageIO.read( new File( SHIP_IMAGE ) );
    isStopped = true;
    isAlive = true;
    isMoving = true;
    moveForwardPossible = true;
    isMovingForward = false;
    isTurningLeft = false;
    isTurningRight = false;
  }

  /*----------Drawing----------*/
  @Override
  public void repaint( Graphics graphics ) {
    if( isAlive ) {
      g2d = ( Graphics2D ) graphics;
      AffineTransform tx = AffineTransform.getRotateInstance( Math.toRadians( angle ), image.getWidth() / 2, image.getHeight() / 2 );
      AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR );
      g2d.drawImage( image, op, location.x, location.y );
    }

  }

  /*----------Controllers----------*/
  public void moveForward() {
    if( !isStopped && isAlive ) {
      location.translate( ( ( int ) ( 3 * Math.cos( getAngle() ) ) ), ( ( int ) ( 3 * Math.sin( getAngle() ) ) ) );
    }
  }

  @Override
  public void update( Observable o, Object arg ) {
    updateMovement();
    if( clock.getFrame() % 5 == 0 ) {
      justLaunched = false;
    }
    if( !isStopped ) {
      moveForward();
    }
    if( isTurningLeft ) {
      changeAngle( -3 );
    }
    if( isTurningRight ) {
      changeAngle( 3 );
    }
    if( clock.getFrame() % 100 == 0 && isStopped ) {
      changeMoney( -10 );
    }
  }

  /*----------Movement----------*/
  public void updateMovement() {

    ListIterator keyIterator = inputController.getKeysTyped().listIterator();

    while( keyIterator.hasNext() ) {
      KeyObject keyPressed = ( KeyObject ) keyIterator.next();
      if( keyPressed.pressed ) {
        /* Key Pressed */
        switch( keyPressed.key.getKeyCode() ) {
          case ( KeyEvent.VK_SPACE ):
            if( isStopped ) {
              isStopped = false;
              GameSound sound = new GameSound();
              justLaunched = true;
              sound.launch();
            }
            break;
          case ( KeyEvent.VK_LEFT ):
            setIsTurningLeft( true );
            break;
          case ( KeyEvent.VK_RIGHT ):
            setIsTurningRight( true );
            break;
        }
      } else {
        /* Key Released */
        switch( keyPressed.key.getKeyCode() ) {
          case ( KeyEvent.VK_LEFT ):
            setIsTurningLeft( false );
            break;
          case ( KeyEvent.VK_RIGHT ):
            setIsTurningRight( false );
            break;
        }
      }
      keyIterator.remove();
    }
  }

  /*----------Getters & Setters----------*/
  public PlayerObject getInstance() {
    return ( PlayerObject ) this;
  }

  public void setIsStopped( boolean value ) {
    isStopped = value;
  }

  public double getAngle() {
    return Math.toRadians( angle );
  }

  public int getMoney() {
    return money;
  }

  public void changeMoney( int amount ) {
    money += amount;
  }

  public int getAngleDegrees() {
    return angle;
  }

  public void setAngle( int angle ) {
    this.angle = angle;
  }

  public void changeAngle( int angle ) {
    this.angle += angle;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void setIsAlive( boolean value ) {
    isAlive = value;
  }

  public boolean isStopped() {
    return isStopped;
  }

  public void setIsTurningLeft( boolean x ) {
    isTurningLeft = x;
  }

  public void setIsTurningRight( boolean x ) {
    isTurningRight = x;
  }

  public void setName( String name ) {
    PlayerObject.name = name;
  }

  public String getName() {
    return PlayerObject.name;
  }

  public boolean justLaunched() {
    return justLaunched;
  }
}
