package player;

import objects.*;
import tankgame.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class WinnerDisplay extends BasicObject {

  private final ArrayList<TankObject> tanks;
  private final int WIDTH = 500;
  private final int HEIGHT = 200;
  private final int x, y;

  public WinnerDisplay( ArrayList<TankObject> tanks ) {
    this.tanks = tanks;
    x = ( TankGame.WIDTH / 2 ) - ( WIDTH / 2 );
    y = ( TankGame.HEIGHT / 2 ) - ( HEIGHT / 2 );
  }

  @Override
  public void repaint( Graphics graphics ) {
    Graphics2D g2d = ( Graphics2D ) graphics;
    RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
    g2d.setRenderingHints( rh );
    //Draw rectangle
    g2d.setColor( Color.BLACK );
    g2d.fillRect( x, y, WIDTH, HEIGHT );
    g2d.drawRect( x, y, WIDTH, HEIGHT );

    //Draw Text
    g2d.setColor( Color.WHITE );
    g2d.setFont( new Font( "TimesRoman", Font.PLAIN, 50 ) );
    if( tanks.get( 0 ).isDead() ) {
      g2d.drawString( "RED Tank Wins!!", x + 60, y + 100 );
    } else {
      g2d.drawString( "BLUE Tank Wins!!", x + 50, y + 100 );
    }
  }
}
