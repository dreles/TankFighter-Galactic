package animations;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
Loads,access, and grabs sprites from sprite sheet file
 */
public class SpriteLoader {

  private BufferedImage SpriteSheet;
  private BufferedImage[] sprites;

  public SpriteLoader( String SpriteSheetLocation ) {

    try {
      this.SpriteSheet = ImageIO.read( this.getClass().getResource( SpriteSheetLocation ) );
    } catch( IOException ex ) {
      System.err.print( "Sprite Sheet not loaded" );
    }
  }

  public BufferedImage[] getSprite( int width, int height, int amountOfSprites ) {
    sprites = new BufferedImage[ amountOfSprites ];
    for( int i = 0; i < amountOfSprites; i++ ) {
      sprites[ i ] = SpriteSheet.getSubimage( ( ( i + 1 ) * 32 ) - 32, ( 1 * 16 ) - 16, width, height );
    }
    return sprites;
  }
}
