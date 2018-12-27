package controllers;

import java.util.Observable;

/*
Ticks on each successive frame and notifies observers to update.
 */
public class GameClock extends Observable {

  private int frameNumber;
  private int gameRunTime;
  private final int gameStartTime;

  public GameClock() {
    frameNumber = 0;
    gameStartTime = ( int ) System.currentTimeMillis();
  }

  public void frameUpdate() {
    frameNumber++;
  }

  public void tick() {
    frameUpdate();
    setChanged();
    this.notifyObservers();
    gameRunTime = updateRunTime();
  }

  public int updateRunTime() {
    return gameStartTime - ( int ) System.currentTimeMillis();
  }

  public int getFrame() {
    return this.frameNumber;
  }

  public int getTime() {
    return ( int ) System.currentTimeMillis() - gameStartTime;
  }

  public int getRunTime() {
    return gameRunTime;
  }
}
