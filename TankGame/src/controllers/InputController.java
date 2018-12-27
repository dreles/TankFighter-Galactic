package controllers;

import objects.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/*
Accept input from keyboard and adds commands to an ArrayList.
 */
public class InputController implements KeyListener {

  /*----------Objects----------*/
  private final ArrayList<TankObject> tanks;
  private final ArrayList<KeyObject> keysPressed;


  /*----------Initialization----------*/
  public InputController() {
    keysPressed = new ArrayList<>();
    tanks = null;
  }

  public InputController( ArrayList<TankObject> tanks ) {
    this.tanks = tanks;
    keysPressed = new ArrayList<>();
  }

  /*----------KeyListener Methods----------*/
  @Override
  public void keyTyped( KeyEvent e ) {
  }

  @Override
  public void keyPressed( KeyEvent e ) {
    KeyObject keyPressed = new KeyObject();
    keyPressed.key = e;
    keyPressed.pressed = true;
    keysPressed.add( keyPressed );
  }

  @Override
  public void keyReleased( KeyEvent e ) {
    KeyObject keyReleased = new KeyObject();
    keyReleased.key = e;
    keyReleased.pressed = false;
    keysPressed.add( keyReleased );
  }

  /*----------Getters & Setters----------*/
  public ArrayList<KeyObject> getKeysTyped() {
    return keysPressed;
  }
}
