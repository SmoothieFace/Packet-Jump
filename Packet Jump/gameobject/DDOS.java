/**
 * This is the Traffic class which creates a DDOS Missile in the game
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class DDOS extends Enemy {

 public static final int Y_LAND = 100;

 private int posX;
 private int width;
 private int height;

 private BufferedImage image;
 private MainCharacter mainCharacter;

 private Rectangle rectBound;

 /**
  * This is the constructor used to create a new object of the Traffic class
  * @param mainCharacter the main character /////////////////////////////////////////////////////
  * @param posX the position of the DDOS Missile on the x-axis(ground)
  * @param width the width of the DDOS Missile
  * @param height the height of the DDOS Missile
  * @param image image of DDOS Missile
  */
 public DDOS(MainCharacter mainCharacter, int posX, int width, int height, BufferedImage image) {
  this.posX = posX;
  this.width = width;
  this.height = height;
  this.image = image;
  this.mainCharacter = mainCharacter;
  rectBound = new Rectangle();
 }

 /**
  * Updates the position of the DDOS Missile.
  */
 public void update() {
  posX -= mainCharacter.getSpeedX();
 }

 /**
  * Draws DDOS Missile.
  */
 public void draw(Graphics g) {
  g.drawImage(image, posX, Y_LAND - image.getHeight(), null);
  g.setColor(Color.red);
  //		Rectangle bound = getBound();
  //		g.drawRect(bound.x, bound.y, bound.width, bound.height);
 }

 /**
  *  Get's the graphic boundaries/dimensions
  *  @return 
  */
 public Rectangle getBound() {
  rectBound = new Rectangle();
  rectBound.x = (int) posX + (image.getWidth() - width) / 2;
  rectBound.y = Y_LAND - image.getHeight() + (image.getHeight() - height) / 2;
  rectBound.width = width;
  rectBound.height = height;
  return rectBound;
 }

 /**
  * Checks if DDOS Missile is out of screen.
  * @return the state of the DDOS Missile if it's out of screen or not
  */
 @Override
 public boolean isOutOfScreen() {
  if (posX < -image.getWidth()) {
   return true;
  }
  return false;
 }

}