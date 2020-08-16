/**
 * This is the Animation class which allows animation through a list of buffered images (frames)
 * @author Team Tough Times
 * @version 4-27-20
 */
package util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Animation implements Serializable {

 private List < BufferedImage > list;
 private long deltaTime;
 private int currentFrame = 0;
 private long previousTime;

 public Animation(int deltaTime) {
  this.deltaTime = deltaTime;
  list = new ArrayList < BufferedImage > ();
  previousTime = 0;
 }

 public void updateFrame() {
  if (System.currentTimeMillis() - previousTime >= deltaTime) {
   currentFrame++;
   if (currentFrame >= list.size()) {
    currentFrame = 0;
   }
   previousTime = System.currentTimeMillis();
  }
 }

 public void addFrame(BufferedImage image) {
  list.add(image);
 }

 public BufferedImage getFrame() {
  return list.get(currentFrame);
 }

}