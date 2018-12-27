package objects;

import animations.SpriteLoader;
import controllers.GameClock;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Object for random flying Asteroids.
 */
public class AsteroidObject extends BasicObject implements Observer {

  protected final Dimension size = new Dimension( 40, 40 );

  private final int travelPatternX;
  private final int travelPatternY;
  protected Graphics2D g2d;
  private final SpriteLoader spriteLoader;
  private int angle;
  private final int[] vectors = { -2, -1, 1, 2 };

  public AsteroidObject( int x, int y, GameClock clock ) {
    clock.addObserver( AsteroidObject.this );
    this.location = new Rectangle( x, y, size.width, size.height );
    Random r = new Random();
    travelPatternX = vectors[ r.nextInt( 3 ) ];
    travelPatternY = vectors[ r.nextInt( 3 ) ];
    spriteLoader = new SpriteLoader( "../resources/Asteroid_strip180.png" );
    image = spriteLoader.getSprite( 48, 48, 1 )[ 0 ];
  }

  @Override
  public void repaint( Graphics graphics ) {
    g2d = ( Graphics2D ) graphics;
    double rotationRequired = Math.toRadians( angle );
    double locationX = image.getWidth() / 2;
    double locationY = image.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance( rotationRequired, locationX, locationY );
    AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR );
    g2d.drawImage( op.filter( image, null ), location.x, location.y, null );
  }

  @Override
  public void update( Observable o, Object arg ) {
    location.x += travelPatternX;
    location.y += travelPatternY;
    turnAsteroid( 1 );
  }

  public void turnAsteroid( int angle ) {
    this.angle += angle;
  }
}
