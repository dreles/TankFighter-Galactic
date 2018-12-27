package map;

import javax.swing.*;

/**
 * Houses the JPanel that the GameWorld is rendered onto.
 */
public class GameFrame extends JFrame {

  JPanel panel;

  public GameFrame( JPanel panel ) {
    this.panel = panel;
  }

  public void initialize( String title, int WIDTH, int HEIGHT ) {
    this.setTitle( title );
    setSize( WIDTH, HEIGHT );
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    add( panel );
    setVisible( true );
    setResizable( false );
  }
}
