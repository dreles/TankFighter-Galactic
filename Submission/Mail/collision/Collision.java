package collision;

import objects.*;
import controllers.GameClock;
import sounds.GameSound;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Checks for collision between asteroids and the player.
 */
public class Collision implements Observer {

  ArrayList<AsteroidObject> asteroidList;
  public ArrayList<MoonObject> moonList;
  PlayerObject player;
  GameSound sound = new GameSound();
  GameClock clock;
  GameEnder end;

  public static MoonObject currentMoon;
  static boolean isOnMoon = false;

  public Collision( PlayerObject player, ArrayList<AsteroidObject> asteroids, ArrayList<MoonObject> moons, GameClock clock ) {
    clock.addObserver( Collision.this );
    this.clock = clock;
    this.moonList = moons;
    this.asteroidList = asteroids;
    this.player = player;
  }

  public boolean asteroidCollision() {
    ListIterator iterator = asteroidList.listIterator();
    while( iterator.hasNext() ) {
      AsteroidObject asteroid = ( AsteroidObject ) iterator.next();
      if( asteroid.collides( player ) && !player.isStopped() ) {
        player.setIsStopped( true );
        player.setIsAlive( false );
        sound.explosion();
        end = new GameEnder( this.clock, player );
        return true;
      }
    }
    return false;
  }

  public boolean moonCollision() {
    ListIterator iterator = moonList.listIterator();
    while( iterator.hasNext() ) {
      MoonObject moon = ( MoonObject ) iterator.next();
      if( moon.collides( player ) && !player.isStopped() ) {
        player.setIsStopped( true );
        player.changeMoney( 100 );
        player.setIsAlive( true );
        isOnMoon = true;
        currentMoon = moon;
        return true;
      }
    }
    return false;
  }

  public void removeMoon() {
    moonList.remove( currentMoon );
  }

  @Override
  public void update( Observable o, Object arg ) {
    asteroidCollision();
    moonCollision();
  }

  public MoonObject getCurrentMoon() {
    return currentMoon;
  }

  public boolean isOnMoon() {
    return isOnMoon;
  }

  public ArrayList<MoonObject> getMoonList() {
    return moonList;
  }
}
