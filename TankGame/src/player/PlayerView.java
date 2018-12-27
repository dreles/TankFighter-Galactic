package player;

import player.MiniMap;
import player.HealthBar;
import objects.*;
import tankgame.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import map.GameWorld;

/*
Takes the full map frame and splits it up into player views based 
on tank locations.
 */
public class PlayerView extends BasicObject {

  protected int windowDivider = 6;

  /*----------Objects----------*/
  protected ArrayList<TankObject> tanks;
  protected int Panel_WIDTH = TankGame.WIDTH;
  protected int Panel_HEIGHT = TankGame.HEIGHT - 20;
  private BufferedImage tankOneView, tankTwoView;
  private final int PlayerWindow_WIDTH;
  private HealthBar tankOneHealth, tankTwoHealth;
  private MiniMap miniMap;

  /*----------Initializer----------*/
  public PlayerView( ArrayList<TankObject> tanks ) {
    this.tanks = tanks;
    this.PlayerWindow_WIDTH = ( Panel_WIDTH / 2 ) - ( windowDivider / 2 );
  }

  /*----------Player View Creation----------*/
  public void createTwoPlayerViews( BufferedImage frame ) {

    //Tank Views
    tankOneView = createView( tanks.get( 0 ), frame, PlayerWindow_WIDTH );
    tankTwoView = createView( tanks.get( 1 ), frame, PlayerWindow_WIDTH );
    //Create final image
    BufferedImage newImage = new BufferedImage( Panel_WIDTH, Panel_HEIGHT, BufferedImage.TYPE_INT_ARGB );
    Graphics2D g2 = newImage.createGraphics();
    g2.setPaint( Color.BLACK );
    g2.fillRect( 0, 0, Panel_WIDTH, Panel_HEIGHT );

    //Combine views
    g2.drawImage( tankOneView, null, 0, 0 );
    g2.drawImage( tankTwoView, null, PlayerWindow_WIDTH + windowDivider, 0 );

    //Tank Health
    tankOneHealth = new HealthBar( tanks.get( 0 ), 10, Panel_HEIGHT - 30 );
    tankOneHealth.repaint( g2 );
    tankTwoHealth = new HealthBar( tanks.get( 1 ), ( Panel_WIDTH / 2 ) + 10, Panel_HEIGHT - 30 );
    tankTwoHealth.repaint( g2 );

    //MiniMap
    miniMap = new MiniMap( "middle", frame );
    miniMap.createMiniMap( frame );
    miniMap.repaint( g2 );

    g2.dispose();
    this.image = newImage;
  }

  public void createSinglePlayerView( BufferedImage frame ) {

    //Tank Views
    tankOneView = createView( tanks.get( 0 ), frame, Panel_WIDTH );

    //Create final image
    BufferedImage newImage = new BufferedImage( Panel_WIDTH, Panel_HEIGHT, BufferedImage.TYPE_INT_ARGB );
    Graphics2D g2 = newImage.createGraphics();
    g2.setPaint( Color.BLACK );
    g2.fillRect( 0, 0, Panel_WIDTH, Panel_HEIGHT );

    //Combine views
    g2.drawImage( tankOneView, null, 0, 0 );

    //Draw Tank Healh
    tankOneHealth = new HealthBar( tanks.get( 0 ), 230, Panel_HEIGHT - 30 );
    tankOneHealth.repaint( g2 );

    //MiniMap
    miniMap = new MiniMap( "left", frame );
    miniMap.createMiniMap( frame );
    miniMap.repaint( g2 );

    g2.dispose();
    this.image = newImage;
  }

  private BufferedImage createView( TankObject tank, BufferedImage frame, int WIDTH ) {
    int x, y;

    //Check x boundary
    if( tank.getX() <= WIDTH / 2 ) {
      x = 0;
    } else if( tank.getX() >= GameWorld.getMapWidth() - ( WIDTH / 2 ) ) {
      x = GameWorld.getMapWidth() - WIDTH;
    } else {
      x = tank.getX() - ( WIDTH / 2 );
    }

    //Check y boundary
    if( tank.getY() <= Panel_HEIGHT / 2 ) {
      y = 0;
    } else if( tank.getY() >= GameWorld.getMapHeight() - ( Panel_HEIGHT / 2 ) ) {
      y = GameWorld.getMapHeight() - Panel_HEIGHT;
    } else {
      y = tank.getY() - ( Panel_HEIGHT / 2 );
    }

    return frame.getSubimage( x, y, WIDTH, Panel_HEIGHT );
  }
}
