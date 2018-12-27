package objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
Standard base object from which more complex objects are built on top of.
 */
public class BasicObject {

  /*----------Objects----------*/
  protected Rectangle location = new Rectangle();
  protected BufferedImage image;
  protected ImageObserver observer;

  /*----------Initialization----------*/
  public BasicObject() {
  }

  public BasicObject( BufferedImage image, Point position ) {
    this.location.x = position.x;
    this.location.y = position.y;
    this.image = image;
  }

  public BasicObject( String resourceLocation, int x, int y ) throws IOException {
    image = ImageIO.read( new File( resourceLocation ) );
    this.location = new Rectangle( x, y, image.getWidth(), image.getHeight() );
  }

  /*----------Getters & Setters----------*/
  public void setLocation( int x, int y ) {
    this.location.setLocation( x, y );
  }

  public void setSize( int x, int y ) {
    this.location.setSize( x, y );
  }

  public Rectangle getLocation() {
    return this.location;
  }

  public void setX( int x ) {
    this.location.x = x;
  }

  public int getX() {
    return this.location.x;
  }

  public void changeX( int x ) {
    this.location.translate( x, 0 );
  }

  public void setY( int y ) {
    this.location.y = y;
  }

  public int getY() {
    return this.location.y;
  }

  public void changeY( int y ) {
    this.location.translate( 0, y );
  }

  public int getWidth() {
    return this.image.getWidth();
  }

  public int getHeight() {
    return this.image.getHeight();
  }

  public boolean collides( BasicObject object ) {
    return location.intersects( object.getLocation() );
  }

  public void repaint( Graphics graphics ) {
    graphics.drawImage( image, location.x, location.y, observer );
  }

}
