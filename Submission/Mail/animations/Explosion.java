/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animations;

import controllers.GameClock;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import map.GameWorld;
import objects.BasicObject;

/**
 *
 * @author andreleslie
 */
public class Explosion extends BasicObject implements Observer {

    private final int x;
    private final int y;
    static BufferedImage[] sprites = new BufferedImage[7];
    static boolean init = false;
    protected GameClock clock;
    private int currentImageNumber;
    private boolean done = true;
    GameWorld world;

    public Explosion(int x, int y, GameWorld world) {
        this.x = x;
        this.y = y;
        this.clock = world.clock;
        currentImageNumber = 0;
        clock.addObserver(this);
        done = false;

        //if first time loading explosion fill static array with images
        if (!init) {
            SpriteLoader ss = new SpriteLoader("../resources/Explosion_strip9.png");
            sprites = ss.getSprite(60, 60, 7);
        }
    }

    public void repaint(Graphics graphics) {
        if (!isDone()) {
            graphics.drawImage(sprites[currentImageNumber], x, y - 10, null);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (clock.getFrame() % 2 == 0) {
            currentImageNumber++;
            if (currentImageNumber == 6) {
                done = true;
                clock.deleteObserver(this);
            }
        }
    }

    public boolean isDone() {
        return done;
    }
}
