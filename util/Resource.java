/**
 * This is the Resource class which allocates a resource file to be used by various other classes.
 * @author Team Tough Times
 * @version 4-27-20
 */
package util;

import java.io.*;
import java.awt.image.BufferedImage;


import javax.imageio.ImageIO;

public class Resource implements Serializable{
 
 public static BufferedImage getResourceImage(String name) {
    try (InputStream in = new Object().getClass().getResourceAsStream( "/"+name ) ) {
      return ImageIO.read(in);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
}