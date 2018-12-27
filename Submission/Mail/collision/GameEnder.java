package collision;

import controllers.GameClock;
import galacticmail.GalacticMail;
import java.util.Observable;
import java.util.Observer;
import map.GameWorld;
import objects.PlayerObject;

/**
 * Ends the game by displaying the high scores.
 */
public class GameEnder implements Observer {

  PlayerObject player;
  int startFrame;
  int endFrame;
  GameClock clock;

  public GameEnder( GameClock clock, PlayerObject player ) {
    clock.addObserver( GameEnder.this );
    this.clock = clock;
    this.player = player;
    startFrame = clock.getFrame();
    endFrame = startFrame + 100;
  }

  @Override
  public void update( Observable o, Object arg ) {
    if( clock.getFrame() == endFrame ) {
      GalacticMail.getGameWorld().STATE = GameWorld.GAME_STATE.SCORES;
      GalacticMail.getGameWorld().initializeEndScores();
    }
  }
}
