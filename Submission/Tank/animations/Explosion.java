package animations;

import controllers.GameClock;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import map.GameWorld;
import objects.BasicObject;

/*
Animation for explosion sprites.
 */
public class Explosion extends BasicObject implements Observer {

  private final int x;
  private final int y;
  static BufferedImage[] sprites = new BufferedImage[ 6 ];
  static boolean init = false;
  protected GameClock clock;
  private int currentImageNumber;
  private boolean done;
  GameWorld world;

  public Explosion( int x, int y, GameWorld world ) {
    this.x = x;
    this.y = y;
    this.clock = world.clock;
    currentImageNumber = 0;
    clock.addObserver( this );
    done = false;

    //if first time loading explosion fill static array with images
    if( !init ) {
      SpriteLoader ss = new SpriteLoader( "../resources/Explosion_small_strip6.png" );
      sprites = ss.getSprite( 32, 30, 6 );
    }
  }

  @Override
  public void repaint( Graphics graphics ) {
    graphics.drawImage( sprites[ currentImageNumber ], x, y - 10, null );
  }

  @Override
  public void update( Observable o, Object arg ) {
    if( clock.getFrame() % 2 == 0 ) {
      currentImageNumber++;
      if( currentImageNumber == 6 ) {
        done = true;
        clock.deleteObserver( this );
      }
    }
  }

  public boolean isDone() {
    return done;
  }
}
