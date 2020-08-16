/**
 * This is the Cloud class which creates a cloud in the game
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import userinterface.GameWindow;
import util.Resource;

public class Clouds {
 private List < ImageCloud > listCloud;
 private BufferedImage cloud;

 private MainCharacter mainCharacter;

 /**
  * This is the constructor used to create a new object of the Cloud class
  * @param width the width of the cloud
  * @param mainCharacter ////////////////////////////////////////////////////////////////////
  */
 public Clouds(int width, MainCharacter mainCharacter) {
  this.mainCharacter = mainCharacter;
  cloud = Resource.getResourceImage("data/cloud.png");
  listCloud = new ArrayList < ImageCloud > ();

  ImageCloud imageCloud = new ImageCloud();
  imageCloud.posX = 0;
  imageCloud.posY = 30;
  listCloud.add(imageCloud);

  imageCloud = new ImageCloud();
  imageCloud.posX = 150;
  imageCloud.posY = 40;
  listCloud.add(imageCloud);

  imageCloud = new ImageCloud();
  imageCloud.posX = 300;
  imageCloud.posY = 50;
  listCloud.add(imageCloud);

  imageCloud = new ImageCloud();
  imageCloud.posX = 450;
  imageCloud.posY = 20;
  listCloud.add(imageCloud);

  imageCloud = new ImageCloud();
  imageCloud.posX = 600;
  imageCloud.posY = 60;
  listCloud.add(imageCloud);
 }

 /**
  * Updates the position of the clouds.
  */
 public void update() {
  Iterator < ImageCloud > itr = listCloud.iterator();
  ImageCloud firstElement = itr.next();
  firstElement.posX -= mainCharacter.getSpeedX() / 8;
  while (itr.hasNext()) {
   ImageCloud element = itr.next();
   element.posX -= mainCharacter.getSpeedX() / 8;
  }
  if (firstElement.posX < -cloud.getWidth()) {
   listCloud.remove(firstElement);
   firstElement.posX = GameWindow.SCREEN_WIDTH;
   listCloud.add(firstElement);
  }
 }
 /**
  * Draws cloud.
  */
 public void draw(Graphics g) {
  for (ImageCloud imgLand: listCloud) {
   g.drawImage(cloud, (int) imgLand.posX, imgLand.posY, null);
  }
 }

 /**
  * ImageCloud class- holds values for the x and y axis position of the clouds
  */
 private class ImageCloud {
  float posX;
  int posY;
 }
}