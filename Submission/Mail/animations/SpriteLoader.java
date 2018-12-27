package animations;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Loads,access, and grabs sprites from sprite sheet file
 */
public class SpriteLoader {

  private BufferedImage SpriteSheet;
  private BufferedImage[] sprites;

  public SpriteLoader( String spriteSheetLocation ) {
    try {
      this.SpriteSheet = ImageIO.read( this.getClass().getResource( spriteSheetLocation ) );
    } catch( IOException ex ) {
      System.err.print( "Sprite Sheet not loaded" );
    }
  }

  //Loads sprites 
  public BufferedImage[] getSprite( int width, int height, int amountOfSprites ) {
    sprites = new BufferedImage[ amountOfSprites ];
    for( int i = 0; i < amountOfSprites; i++ ) {
      sprites[ i ] = SpriteSheet.getSubimage( ( ( i + 1 ) * width ) - width, ( 1 * 16 ) - 16, width, height );
    }
    return sprites;
  }
}
