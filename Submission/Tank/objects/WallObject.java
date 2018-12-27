package objects;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tankgame.TankGame;

/*
Object for all walls whether destructible or indestructible.
 */
public class WallObject extends BasicObject {

  protected final Dimension size = new Dimension( 28, 28 );

  protected boolean destructible;
  protected final String INDESTRUCTIBLE_WALL_IMAGE = TankGame.getResourcesDir() + "wall_indestructible.png";
  protected final String WALL_IMAGE = TankGame.getResourcesDir() + "wall.png";

  public WallObject( boolean destructible ) throws IOException {
    location = new Rectangle( 0, 0, size.width, size.height );
    this.destructible = destructible;
    if( destructible ) {
      this.image = ImageIO.read( new File( WALL_IMAGE ) );
    } else {
      this.image = ImageIO.read( new File( INDESTRUCTIBLE_WALL_IMAGE ) );
    }
  }

  public WallObject( boolean destructible, int x, int y ) throws IOException {
    location = new Rectangle( x, y, size.width, size.height );
    this.destructible = destructible;
    if( destructible ) {
      this.image = ImageIO.read( new File( WALL_IMAGE ) );
    } else {
      this.image = ImageIO.read( new File( INDESTRUCTIBLE_WALL_IMAGE ) );
    }
  }

  public boolean isDestructible() {
    return this.destructible;
  }
}
