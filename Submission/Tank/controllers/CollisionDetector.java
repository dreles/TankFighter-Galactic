package controllers;

import animations.Explosion;
import java.util.ArrayList;
import java.util.ListIterator;
import map.WallList;
import objects.*;
import map.GameWorld;
import sounds.GameSound;

/*
Checks for collision between Tanks, Walls, and Bullets.
 */
public class CollisionDetector {

  private final ArrayList<TankObject> tanks;
  private final WallList wallList;
  private final GameWorld world;

  public CollisionDetector( ArrayList<TankObject> tanks, WallList wallList, GameWorld world ) {
    this.tanks = tanks;
    this.wallList = wallList;
    this.world = world;
  }

  public void TankMovementPossible() {
    ListIterator<TankObject> tankIterator = getTankIterator();

    /* Tank on Wall Collision */
    while( tankIterator.hasNext() ) {

      TankObject tempTank = tankIterator.next();
      TankObject tank = new TankObject( tempTank, "forward" );

      if( wallList.tankCollides( tank ) ) {
        tempTank.setForwardPossible( false );
      } else {
        tempTank.setForwardPossible( true );
      }

      tank = new TankObject( tempTank, "backward" );
      if( wallList.tankCollides( tank ) ) {
        tempTank.setBackwardPossible( false );
      } else {
        tempTank.setBackwardPossible( true );
      }

      tempTank = null;
      tank = null;
    }

    /* Tank on Tank Collision */
    TankObject tank1 = tanks.get( 0 );
    TankObject tank2 = tanks.get( 1 );
    TankObject tempTank;

    if( tank1.isForwardPossible() ) {
      tempTank = new TankObject( tank1, "forward" );
      if( tempTank.collides( tank2 ) ) {
        tank1.setForwardPossible( false );
      } else {
        tank1.setForwardPossible( true );
      }
    }

    if( tank2.isForwardPossible() ) {
      tempTank = new TankObject( tank2, "forward" );
      if( tempTank.collides( tank1 ) ) {
        tank2.setForwardPossible( false );
      } else {
        tank2.setForwardPossible( true );
      }
    }

    if( tank1.isBackwardPossible() ) {
      tempTank = new TankObject( tank1, "backward" );
      if( tempTank.collides( tank2 ) ) {
        tank1.setBackwardPossible( false );
      } else {
        tank1.setBackwardPossible( true );
      }
    }

    if( tank2.isBackwardPossible() ) {
      tempTank = new TankObject( tank2, "backward" );
      if( tempTank.collides( tank1 ) ) {
        tank2.setBackwardPossible( false );
      } else {
        tank2.setBackwardPossible( true );
      }
    }
  }

  public void bulletCollision() {
    ListIterator<TankObject> tankIterator = getTankIterator();
    while( tankIterator.hasNext() ) {
      TankObject tank = ( TankObject ) tankIterator.next();
      ArrayList<BulletObject> tanksBullets = tank.getBullets();
      ListIterator bulletIterator = tanksBullets.listIterator();
      while( bulletIterator.hasNext() ) {
        BulletObject bullet = ( BulletObject ) bulletIterator.next();
        if( wallList.bulletCollides( bullet ) ) {
          GameSound sound = new GameSound();
          sound.wallHit();
          Explosion explosion = new Explosion( bullet.getX(), bullet.getY(), world );
          GameWorld.explosions.add( explosion );
          bulletIterator.remove();

        }
      }
    }
  }

  public void bulletTankCollision() {
    TankObject tankOne = tanks.get( 0 );
    TankObject tankTwo = tanks.get( 1 );

    /* TankOne Bullets hit TankTwo */
    ListIterator<BulletObject> tankOneBulletsIterator = tankOne.getBullets().listIterator();
    while( tankOneBulletsIterator.hasNext() ) {
      BulletObject bullet = tankOneBulletsIterator.next();
      if( bullet.collides( tankTwo ) ) {
        GameSound sound = new GameSound();
        sound.tankHit();
        GameWorld.explosions.add( new Explosion( bullet.getX(), bullet.getY(), world ) );
        tankTwo.decreaseHealth();
        tankOneBulletsIterator.remove();
        if( tankTwo.isDead() ) {
          GameSound soundtank = new GameSound();
          soundtank.tankExplosion();
        }
      }
    }

    /* TankTwo Bullets hit TankOne */
    ListIterator<BulletObject> tankTwoBulletsIterator = tankTwo.getBullets().listIterator();
    while( tankTwoBulletsIterator.hasNext() ) {
      BulletObject bullet = tankTwoBulletsIterator.next();
      if( bullet.collides( tankOne ) ) {
        GameSound sound = new GameSound();
        sound.tankHit();
        GameWorld.explosions.add( new Explosion( bullet.getX(), bullet.getY(), world ) );
        tankOne.decreaseHealth();
        tankTwoBulletsIterator.remove();
        if( tankOne.isDead() ) {
          GameSound soundtank = new GameSound();
          soundtank.tankExplosion();
        }
      }
    }
  }

  public void bulletWallCollision() {
    ListIterator<TankObject> tankIterator = getTankIterator();
    while( tankIterator.hasNext() ) {
      TankObject tank = ( TankObject ) tankIterator.next();
      ArrayList<BulletObject> tanksBullets = tank.getBullets();
      ListIterator bulletIterator = tanksBullets.listIterator();
      while( bulletIterator.hasNext() ) {
        BulletObject bullet = ( BulletObject ) bulletIterator.next();
        if( wallList.bulletCollides( bullet ) ) {
          Explosion explosion = new Explosion( bullet.getX(), bullet.getY(), world );
          GameSound sounds = new GameSound();
          sounds.wallHit();
          bulletIterator.remove();
        }
      }
    }
  }

  public ListIterator<TankObject> getTankIterator() {
    return tanks.listIterator();
  }
}
