package player;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import objects.BasicObject;
import tankgame.TankGame;

public class MiniMap extends BasicObject {

  private BufferedImage before, after;
  private final double scalar = 0.14;

  public MiniMap( String position, BufferedImage frame ) {
    if( position.equals( "left" ) ) {
      this.location.x = 20 ;
      this.location.y = TankGame.HEIGHT - ( int ) ( frame.getHeight() * scalar ) - 35;
      
    } else if( position.equals( "middle" ) ) {
      this.location.x = ( TankGame.WIDTH / 2 ) - ( int ) ( ( frame.getWidth() * scalar ) / 2 );
      this.location.y = TankGame.HEIGHT - ( int ) ( frame.getHeight() * scalar ) - 70;
    }
  }

  public void createMiniMap( BufferedImage frame ) {
    before = frame;
    after = new BufferedImage( before.getWidth(), before.getHeight(), BufferedImage.TYPE_INT_ARGB );
    AffineTransform at = new AffineTransform();
    at.scale( scalar, scalar );
    AffineTransformOp op = new AffineTransformOp( at, AffineTransformOp.TYPE_BILINEAR );
    this.image = op.filter( before, after );
  }
}
