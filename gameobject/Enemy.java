/**
 * Enemy is the abstract base class for all enemies
 * which updates, draws, and
 * checks if enemies are out of screen
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Enemy {
 public abstract void draw(Graphics g);
 public abstract Rectangle getBound();
 public abstract boolean isOutOfScreen();
 public abstract void update();
 
}