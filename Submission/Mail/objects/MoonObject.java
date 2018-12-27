package objects;

import animations.SpriteLoader;
import galacticmail.GalacticMail;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Object for moons player will land on. Type 'random' for moon to land on. Type
 * 'home' for moon to launch from.
 */
public class MoonObject extends BasicObject {

  private final Dimension size = new Dimension( 10, 10 );

  private final SpriteLoader spriteLoader = new SpriteLoader( "../resources/Bases_strip8.png" );
  private final BufferedImage[] baseList = spriteLoader.getSprite( 64, 64, 8 );

  public MoonObject( int x, int y, String type ) {
    switch( type ) {
      case "random":
        Random rand = new Random();

        this.location = new Rectangle(x, y, size.width + 5, size.height + 5);

        this.image = baseList[ rand.nextInt( baseList.length - 1 ) ];
        break;
      case "home":
        this.location = new Rectangle( x, y, 0, 0 );
        try {
          this.image = ImageIO.read( new File( GalacticMail.getResourcesDir() + "Moon.png" ) );
        } catch( IOException e ) {
          System.err.println( "Error loading home moon." );
        }
        break;
    }

  }
}
