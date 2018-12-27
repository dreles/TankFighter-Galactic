package map;

import objects.*;

import java.util.ArrayList;
import java.io.IOException;
import java.awt.Graphics2D;
import java.util.ListIterator;

/*
  List of all indestructible and destructible walls.
 */
public final class WallList {

  /*----------Objects----------*/
  protected ArrayList< WallObject> walls;
  protected WallObject indestructibleWall;
  protected WallObject destructibleWall;

  /*----------Initialization----------*/
  public WallList() throws IOException {
    this.walls = new ArrayList<>();
    indestructibleWall = new WallObject( false );
    destructibleWall = new WallObject( true );
    addWalls();
  }

  public ArrayList<WallObject> getWallList() {
    return walls;
  }

  public ListIterator<WallObject> listIterator() {
    return walls.listIterator( 0 );
  }

  /*----------Drawing----------*/
  public void repaint( Graphics2D graphics ) {
    for( int i = 0; i < walls.size(); i++ ) {
      WallObject temp = walls.get( i );
      temp.repaint( graphics );
    }
  }

  /*----------Wall Collision----------*/
  public boolean bulletCollides( BulletObject bullet ) {
    ListIterator<WallObject> wallIterator = listIterator();
    while( wallIterator.hasNext() ) {
      WallObject tempWall = wallIterator.next();
      if( tempWall.collides( bullet ) ) {
        if( tempWall.isDestructible() ) {
          wallIterator.remove();
        }
        return true;
      }
    }
    return false;
  }

  public boolean tankCollides( TankObject tank ) {
    ListIterator<WallObject> wallIterator = listIterator();
    while( wallIterator.hasNext() ) {
      WallObject tempWall = wallIterator.next();
      if( tempWall.collides( tank ) ) {
        return true;
      }
    }
    return false;
  }

  /*----------Wall Placing----------*/
  public void addWalls() throws IOException {
    try {
      /*INDESTRUCTIBLE WALLS*/
      //perimeter
      for( int x = 0; x <= 39; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ), 0 ) );
      }
      for( int x = 1; x <= 40; x++ ) {
        walls.add( new WallObject( false, 0, ( x * indestructibleWall.getWidth() ) ) );
      }
      for( int x = 1; x <= 40; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                ( GameWorld.getMapHeight() - indestructibleWall.getHeight() ) ) );
      }
      for( int x = 1; x <= 40; x++ ) {
        walls.add( new WallObject( false, ( GameWorld.getMapWidth() - indestructibleWall.getHeight() ),
                ( x * indestructibleWall.getWidth() ) ) );
      }
      //top starting point
      for( int x = 1; x <= 3; x++ ) {
        walls.add( new WallObject( false, ( 19 * indestructibleWall.getWidth() ),
                ( x * indestructibleWall.getWidth() ) ) );
      }
      for( int x = 15; x <= 19; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                ( 4 * indestructibleWall.getWidth() ) ) );
      }
      //bottom starting point
      for( int x = 5; x >= 1; x-- ) {
        walls.add( new WallObject( false, ( 14 * indestructibleWall.getWidth() ),
                GameWorld.getMapHeight() - ( x * indestructibleWall.getWidth() ) ) );
      }
      for( int x = 14; x <= 19; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                GameWorld.getMapHeight() - ( 5 * indestructibleWall.getWidth() ) ) );
      }
      //left horiz wall
      for( int x = 1; x <= 7; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                ( 19 * indestructibleWall.getWidth() ) ) );
      }
      //right horiz wall
      for( int x = 1; x <= 9; x++ ) {
        walls.add( new WallObject( false, GameWorld.getMapWidth() - ( x * indestructibleWall.getWidth() ),
                ( 22 * indestructibleWall.getWidth() ) ) );
      }
      //middle structure left horiz wall
      for( int x = 4; x <= 15; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                GameWorld.getMapHeight() - ( 9 * indestructibleWall.getWidth() ) ) );
      }
      //middle structure right horiz wall
      for( int x = 20; x <= 34; x++ ) {
        walls.add( new WallObject( false, ( x * indestructibleWall.getWidth() ),
                ( 8 * indestructibleWall.getWidth() ) ) );
      }
      //middle structure left vert wall
      for( int x = 23; x <= 26; x++ ) {
        walls.add( new WallObject( false, ( 16 * indestructibleWall.getWidth() ),
                ( x * indestructibleWall.getWidth() ) ) );
      }
      //middle structure right vert wall
      for( int x = 13; x <= 18; x++ ) {
        walls.add( new WallObject( false, ( 19 * indestructibleWall.getWidth() ),
                ( x * indestructibleWall.getWidth() ) ) );
      }

      /*DESTRUCTIBLE WALLS*/
      //1st col of destructible walls
      for( int x = 18; x <= 30; x++ ) {
        walls.add( new WallObject( true, ( 15 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      //2nd col of destructible walls
      for( int x = 18; x <= 22; x++ ) {
        walls.add( new WallObject( true, ( 16 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      for( int x = 27; x <= 31; x++ ) {
        walls.add( new WallObject( true, ( 16 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      //3rd col of destructible walls
      for( int x = 18; x <= 19; x++ ) {
        walls.add( new WallObject( true, ( 17 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      for( int x = 22; x <= 23; x++ ) {
        walls.add( new WallObject( true, ( 17 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      //4th col of destructible walls
      for( int x = 8; x <= 19; x++ ) {
        walls.add( new WallObject( true, ( 18 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      for( int x = 22; x <= 23; x++ ) {
        walls.add( new WallObject( true, ( 18 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      //5th col of destructible walls
      for( int x = 8; x <= 12; x++ ) {
        walls.add( new WallObject( true, ( 19 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      for( int x = 19; x <= 23; x++ ) {
        walls.add( new WallObject( true, ( 19 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
      //6th col of destructible walls
      for( int x = 9; x <= 23; x++ ) {
        walls.add( new WallObject( true, ( 20 * destructibleWall.getWidth() ), ( x * destructibleWall.getWidth() ) ) );
      }
    } catch( IOException ex ) {
      System.err.println( "Error creating walls." );
    }
  }
}
