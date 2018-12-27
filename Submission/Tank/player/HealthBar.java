package player;

import java.awt.Color;
import java.awt.Graphics;
import objects.BasicObject;
import objects.BasicObject;
import objects.TankObject;
import objects.TankObject;

/*
Health bar for the individual tanks.
 */
public class HealthBar extends BasicObject {

  TankObject tank;
  protected int width = 200;
  protected int height = 15;

  public HealthBar( TankObject tank, int x, int y ) {
    this.tank = tank;
    this.location.x = x;
    this.location.y = y;
  }

  @Override
  public void repaint( Graphics graphics ) {
    width = tank.getHealth() * 2;
    if( tank.getHealth() >= 75 ) {
      graphics.setColor( Color.GREEN );
    } else if( tank.getHealth() >= 25 && tank.getHealth() < 74 ) {
      graphics.setColor( Color.YELLOW );
    } else{
      graphics.setColor( Color.RED );
    }
    graphics.fillRect( location.x, location.y, width, height );
    graphics.drawRect( location.x, location.y, width, height );
  }
}
