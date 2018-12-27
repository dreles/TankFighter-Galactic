package objects;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tankgame.TankGame;

public class BulletObject extends BasicObject {

  protected final Dimension size = new Dimension( 30, 6 ); //set the size of the bullet

  /*----------Objects----------*/
  String BULLET_IMAGE = TankGame.getResourcesDir() + "bullet.png";
  protected Point direction;
  TankObject owner;
  protected int angle;
  protected Graphics2D g2d;

  /*----------Initialization----------*/
  public BulletObject( Point location, int angle, TankObject owner ) throws IOException {
    location.x -= size.width / 2;
    location.y -= size.height / 2;
    this.location = new Rectangle( location, size );
    this.angle = angle;
    this.owner = owner;
    this.image = ImageIO.read( new File( BULLET_IMAGE ) );
  }

  /*----------Bullet Controller----------*/
  public void move() {
    location.translate( ( ( int ) ( 10 * Math.cos( getAngle() ) ) ), ( ( int ) ( 10 * Math.sin( getAngle() ) ) ) );
  }

  /*----------Drawing----------*/
  @Override
  public void repaint( Graphics graphics ) {
    g2d = ( Graphics2D ) graphics;
    AffineTransform tx = AffineTransform.getRotateInstance( Math.toRadians( angle ), image.getWidth() / 2, image.getHeight() / 2 );
    AffineTransformOp op = new AffineTransformOp( tx, AffineTransformOp.TYPE_BILINEAR );
    g2d.drawImage( image, op, location.x, location.y );

  }

  /*----------Getters & Setters----------*/
  public TankObject getOwner() {
    return owner;
  }

  public double getAngle() {
    return Math.toRadians( angle );
  }
}
