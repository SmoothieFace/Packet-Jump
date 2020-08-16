/**
 * This is the EnemiesManager class that manages 
 * multiple enemies at a time, checks for collisions
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Resource;

public class EnemiesManager {

 private BufferedImage traffic1;
 private BufferedImage traffic2;
 private BufferedImage ddos1;
 private Random rand;
 private List < Enemy > enemies;
 private MainCharacter mainCharacter;

 /**
  * This is the constructor used to create a new object of the EnemiesManager class
  * @param mainCharacter the main character /////////////////////////////////////////////////////
  */
 public EnemiesManager(MainCharacter mainCharacter) {
  rand = new Random();
  traffic1 = Resource.getResourceImage("data/enemy1.png");
  traffic2 = Resource.getResourceImage("data/enemy2.png");
  ddos1 = Resource.getResourceImage("data/ddos.png");
  enemies = new ArrayList < Enemy > ();
  this.mainCharacter = mainCharacter;
  enemies.add(createEnemy());
 }

 /**
  * Replaces the enemies everytime the character passes it.
  */
 public void update() {
  for (Enemy e: enemies) {
   e.update();
  }
  Enemy enemy = enemies.get(0);
  if (enemy.isOutOfScreen()) {
   mainCharacter.upScore();
   enemies.clear();
   enemies.add(createEnemy());
  }
 }

 /**
  * Draws enemies.
  */
 public void draw(Graphics g) {
  for (Enemy e: enemies) {
   e.draw(g);
  }
 }

 /**
  * Creates different types of enemies randomly.
  * @return Cactus
  */
 private Enemy createEnemy() {
  // if (enemyType = getRandom)
  int type = rand.nextInt(3);
  if (type == 0) {
   return new Traffic(mainCharacter, 800, traffic1.getWidth() - 10, traffic1.getHeight() - 10, traffic1);
  } 
  else if(type == 1){
   return new Traffic(mainCharacter, 800, traffic2.getWidth() - 10, traffic2.getHeight() - 10, traffic2);
  }
  else{
   return new DDOS(mainCharacter, 800, traffic2.getWidth() - 10, traffic2.getHeight(), ddos1);
  }
 }

 /**
  * Checks if the enemies touches the character
  * @return the state of the enemy touching the character or not.
  */
 public boolean isCollision() {
  for (Enemy e: enemies) {
   if (mainCharacter.getBound().intersects(e.getBound())) {
    return true;
   }
  }
  return false;
 }

 /**
  * Resets enemies.
  */
 public void reset() {
  enemies.clear();
  enemies.add(createEnemy());
 }

}