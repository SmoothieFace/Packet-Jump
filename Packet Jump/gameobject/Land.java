/**
 * Land is a class that draws the ground for
 * for the main character to run on.
 * This class allows the ground to change throughout the game. 
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import util.Resource;

public class Land {

 public static final int LAND_POSY = 103;

 private List < ImageLand > listLand;
 private BufferedImage land1;
 private BufferedImage land2;
 private BufferedImage land3;

 private MainCharacter mainCharacter;

 /**
  * This is the constructor used to create a new object of the Cloud class
  * @param width the width of the cloud
  * @param mainCharacter ////////////////////////////////////////////////////////////////////
  */
 public Land(int width, MainCharacter mainCharacter) {
  this.mainCharacter = mainCharacter;
  land1 = Resource.getResourceImage("data/land1.png");
  land2 = Resource.getResourceImage("data/land2.png");
  land3 = Resource.getResourceImage("data/land3.png");
  int numberOfImageLand = width / land1.getWidth() + 2;
  listLand = new ArrayList < ImageLand > ();
  for (int i = 0; i < numberOfImageLand; i++) {
   ImageLand imageLand = new ImageLand();
   imageLand.posX = i * land1.getWidth();
   setImageLand(imageLand);
   listLand.add(imageLand);
  }
 }

 /**
  * Updates land by changing the different land images.
  * Images change in relation of the character
  */
 public void update() {
  Iterator < ImageLand > itr = listLand.iterator();
  ImageLand firstElement = itr.next();
  firstElement.posX -= mainCharacter.getSpeedX();
  float previousPosX = firstElement.posX;
  while (itr.hasNext()) {
   ImageLand element = itr.next();
   element.posX = previousPosX + land1.getWidth();
   previousPosX = element.posX;
  }
  if (firstElement.posX < -land1.getWidth()) {
   listLand.remove(firstElement);
   firstElement.posX = previousPosX + land1.getWidth();
   setImageLand(firstElement);
   listLand.add(firstElement);
  }
 }

 /**
  * Sets the different images of land.
  */
 private void setImageLand(ImageLand imgLand) {
  int typeLand = getTypeOfLand();
  if (typeLand == 1) {
   imgLand.image = land1;
  } else if (typeLand == 3) {
   imgLand.image = land3;
  } else {
   imgLand.image = land2;
  }
 }

 /**
  * Draws land.
  */
 public void draw(Graphics g) {
  for (ImageLand imgLand: listLand) {
   g.drawImage(imgLand.image, (int) imgLand.posX, LAND_POSY, null);
  }
 }

 /**
  * Randomize the different types of land.
  */
 private int getTypeOfLand() {
  Random rand = new Random();
  int type = rand.nextInt(10);
  if (type == 1) {
   return 1;
  } else if (type == 9) {
   return 3;
  } else {
   return 2;
  }
 }

 /**
  * ImageLand class- holds values for the position and image of the land.
  */
 private class ImageLand {
  float posX;
  BufferedImage image;
 }

}