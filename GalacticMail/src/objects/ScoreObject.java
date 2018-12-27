package objects;

/**
 * Object for holding a player's name and score.
 */
public class ScoreObject {

  public String name;
  public int score;

  public ScoreObject( String name, int score ) {
    this.name = name;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public Integer getScore() {
    return score;
  }
}
