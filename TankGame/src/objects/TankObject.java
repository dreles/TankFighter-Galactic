package objects;

import controllers.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import sounds.GameSound;
import tankgame.TankGame;

/*
  Object for the tanks.
 */
public class TankObject extends BasicObject implements Observer {

  protected final Dimension size = new Dimension( 55, 55 );

  /*----------Objects----------*/
  public String name;
  protected int angle;
  protected int health;
  protected ArrayList<BulletObject> bullets = new ArrayList<>();
  protected String TANK_IMAGE_BLUE = TankGame.getResourcesDir() + "Tank_Blue.png";
  protected String TANK_IMAGE_RED = TankGame.getResourcesDir() + "Tank_Red.png";
  protected Graphics2D g2d;
  protected boolean moveForwardPossible;
  protected boolean moveBackwardPossible;
  protected boolean isMovingForward;
  protected boolean isMovingBackward;
  protected boolean isTurningLeft;
  protected boolean isTurningRight;
  protected boolean isFiring;
  protected GameClock clock;
  private final GameSound sounds;
  private InputController inputController;

  /*----------Initialization----------*/
  public TankObject( int x, int y, int angle, String name, GameClock clock, InputController input ) throws IOException {
    this.inputController = input;
    this.sounds = new GameSound();
    this.name = name;
    clock.addObserver( TankObject.this );
    this.clock = clock;
    location = new Rectangle( x, y, size.width, size.height );
    health = 100;
    this.angle = angle;
    if( name.equals( "tankOne" ) ) {
      image = ImageIO.read( new File( TANK_IMAGE_BLUE ) );
    } else if( name.equals( "tankTwo" ) ) {
      image = ImageIO.read( new File( TANK_IMAGE_RED ) );
    }
    moveForwardPossible = true;
    moveBackwardPossible = true;
    isMovingForward = false;
    isMovingBackward = false;
    isTurningLeft = false;
    isTurningRight = false;
    isFiring = false;
  }

  //Copies given tank with position one step forward/back.
  public TankObject( TankObject tank, String direction ) {
    this.sounds = new GameSound();
    if( direction.equals( "forward" ) ) {
      this.location.x = tank.location.x + ( ( int ) ( 10 * Math.cos( tank.getAngle() ) ) );
      this.location.y = tank.location.y + ( ( int ) ( 10 * Math.sin( tank.getAngle() ) ) );
    } else if( direction.equals( "backward" ) ) {
      this.location.x = tank.location.x - ( ( int ) ( 10 * Math.cos( tank.getAngle() ) ) );
      this.location.y = tank.location.y - ( ( int ) ( 10 * Math.sin( tank.getAngle() ) ) );
    }
    location.width = 58;
    location.height = 58;
    angle = tank.getAngleDegrees();
  }

  /*----------Drawing----------*/
  @Override
  public void repaint( Graphics graphics ) {
    g2d = ( Graphics2D ) graphics;
    AffineTransform tx = AffineTransform.getRotateInstance( Math.toRadians( angle ), image.getWidth() / 2, image.getHeight() / 2 );
    AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR );
    g2d.drawImage( image, op, location.x, location.y );
  }

  /*----------Controllers----------*/
  public void decreaseHealth() {
    this.health -= 10;
  }

  public void fire( ArrayList<BulletObject> bullets ) throws IOException {
    Point bulletLocation = new Point( ( location.width / 2 ) + ( location.x ), ( location.height / 2 ) + ( location.y ) );
    BulletObject bullet = new BulletObject( bulletLocation, this.angle, this );
    sounds.fire();
    bullets.add( bullet );
  }

  public void moveForward() {
    if( moveForwardPossible ) {
      location.translate( ( ( int ) ( 7 * Math.cos( getAngle() ) ) ), ( ( int ) ( 7 * Math.sin( getAngle() ) ) ) );
    }
  }

  public void moveForwardSlowly() {
    if( moveForwardPossible ) {
      location.translate( ( ( int ) ( 2 * Math.cos( getAngle() ) ) ), ( ( int ) ( 2 * Math.sin( getAngle() ) ) ) );
    }
  }

  public void moveBackward() {
    if( moveBackwardPossible ) {
      location.translate( ( ( int ) ( -5 * Math.cos( getAngle() ) ) ), ( ( int ) ( -5 * Math.sin( getAngle() ) ) ) );
    }
  }

  @Override
  public void update( Observable o, Object arg ) {
    updateInput();
    if( isMovingForward ) {
      moveForward();
    }
    if( isMovingBackward ) {
      moveBackward();
    }
    if( isTurningLeft ) {
      changeAngle( -5 );
    }
    if( isTurningRight ) {
      changeAngle( 5 );
    }
    if( isFiring ) {
      if( clock.getFrame() % 10 == 0 ) {
        try {
          fire( bullets );
        } catch( IOException ex ) {
        }
      }
    }
  }

  private void updateInput() {

    ListIterator keyIterator = inputController.getKeysTyped().listIterator();

    while( keyIterator.hasNext() ) {
      KeyObject keyPressed = ( KeyObject ) keyIterator.next();
      if( keyPressed.pressed ) {
        /* Key Pressed */
        if( name.equals( "tankTwo" ) ) {
          switch( keyPressed.key.getKeyCode() ) {
            case ( KeyEvent.VK_UP ):
              setIsMovingForward( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_DOWN ):
              setIsMovingBackward( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_LEFT ):
              setIsTurningLeft( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_RIGHT ):
              setIsTurningRight( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_ENTER ):
              setIsFiring( true );
              keyIterator.remove();
              break;
          }
        } else if( name.equals( "tankOne" ) ) {
          switch( keyPressed.key.getKeyCode() ) {
            case ( KeyEvent.VK_W ):
              setIsMovingForward( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_S ):
              setIsMovingBackward( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_A ):
              setIsTurningLeft( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_D ):
              setIsTurningRight( true );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_SPACE ):
              setIsFiring( true );
              keyIterator.remove();
              break;
          }
        }

      } else {
        /* Key Released */
        if( name.equals( "tankTwo" ) ) {
          switch( keyPressed.key.getKeyCode() ) {
            case ( KeyEvent.VK_UP ):
              setIsMovingForward( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_DOWN ):
              setIsMovingBackward( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_LEFT ):
              setIsTurningLeft( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_RIGHT ):
              setIsTurningRight( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_ENTER ):
              setIsFiring( false );
              keyIterator.remove();
              break;

          }

        } else if( name.equals( "tankOne" ) ) {
          switch( keyPressed.key.getKeyCode() ) {
            case ( KeyEvent.VK_W ):
              setIsMovingForward( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_S ):
              setIsMovingBackward( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_A ):
              setIsTurningLeft( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_D ):
              setIsTurningRight( false );
              keyIterator.remove();
              break;
            case ( KeyEvent.VK_SPACE ):
              setIsFiring( false );
              keyIterator.remove();
              break;
          }
        }
      }
    }
  }

  /*----------Getters & Setters----------*/
  public TankObject getInstance() {
    return ( TankObject ) this;
  }

  public String getName() {
    return this.name;
  }

  public double getAngle() {
    return Math.toRadians( angle );
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

  public boolean isDead() {
    return health <= 0;
  }

  public int getHealth() {
    return health;
  }

  public boolean isForwardPossible() {
    return moveForwardPossible;
  }

  public boolean isBackwardPossible() {
    return moveBackwardPossible;
  }

  public void setForwardPossible( boolean x ) {
    moveForwardPossible = x;
  }

  public void setBackwardPossible( boolean x ) {
    moveBackwardPossible = x;
  }

  public void setIsMovingForward( boolean x ) {
    isMovingForward = x;
  }

  public void setIsMovingBackward( boolean x ) {
    isMovingBackward = x;
  }

  public void setIsTurningLeft( boolean x ) {
    isTurningLeft = x;
  }

  public void setIsTurningRight( boolean x ) {
    isTurningRight = x;
  }

  public void setIsFiring( boolean x ) {
    isFiring = x;
  }

  public ArrayList<BulletObject> getBullets() {
    return this.bullets;
  }
}
