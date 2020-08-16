/**
 * The main game screen which controls when sound is played, what animations are happening, and the states of the game.
 * @author Team Tough Times
 * @version 4-27-20
 */
package userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.sound.sampled.*;
import javax.swing.JPanel;
import java.awt.Font;
import java.util.*;
import java.io.*;
import java.net.*;
import gameobject.Clouds;
import gameobject.EnemiesManager;
import gameobject.Land;
import gameobject.MainCharacter;
import util.*;

public class GameScreen extends JPanel implements Runnable, KeyListener {

 private static final int START_GAME_STATE = 0;
 private static final int GAME_PLAYING_STATE = 1;
 private static final int GAME_OVER_STATE = 2;

 //List of packet's to update
 private Vector<MainCharacter> mainCharacters = new Vector<MainCharacter>();
 private Land land;
 private MainCharacter mainCharacter;
 private EnemiesManager enemiesManager;
 private Clouds clouds;
 private ChatClient chat;
 private Thread thread;
 private Socket s;
 private OutputStream out;
 private InputStream in;
 private String ipDestination;
 private ObjectOutputStream oos;
 private ObjectInputStream ooi;
 private boolean isKeyPressed;
 int count  = 0;
 private boolean commenceUpdates = false;
 private boolean death = true;
 private int gameState = START_GAME_STATE;

 private BufferedImage replayButtonImage;
 private BufferedImage gameOverButtonImage;

 public GameScreen(ChatClient chat) {

  this.chat = chat;
  mainCharacter = new MainCharacter(); //Assigning a main character to be controplled
  mainCharacters.add(mainCharacter);
  
  
  if(chat.isMultiplayer()){
     try{
        s = new Socket("localhost",16790);
        out = s.getOutputStream();
        in = s.getInputStream();
        oos = new ObjectOutputStream(out);
        ooi = new ObjectInputStream(in);
        shareMainCharacter();
     } catch(Exception e){
         e.printStackTrace();
     }
  }
  
  land = new Land(GameWindow.SCREEN_WIDTH, mainCharacter);
  mainCharacter.setSpeedX(4);
  replayButtonImage = Resource.getResourceImage("data/replay_button.png");
  gameOverButtonImage = Resource.getResourceImage("data/gameover_text.png");
  enemiesManager = new EnemiesManager(mainCharacter);
  clouds = new Clouds(GameWindow.SCREEN_WIDTH, mainCharacter);
  startGame();

 }
 public void startGame() {
  thread = new Thread(this);
  thread.start();
 }
 public void updateOtherPackets(){
      try{
      
         MainCharacter packetToUpdate = (MainCharacter)ooi.readObject();
         if(mainCharacters.contains(packetToUpdate)){
            int indexInList = mainCharacters.indexOf(packetToUpdate);
            mainCharacters.set(indexInList , packetToUpdate);
         }
         else{
            mainCharacters.add(packetToUpdate);
         }
         } catch(NullPointerException npe){ npe.printStackTrace();
         } catch(IOException ioe){
            
         } catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
         }  
 }
 public void gameUpdate() {
  if (gameState == GAME_PLAYING_STATE) {
   clouds.update();
   land.update();
   //updateOtherPackets();
   //Update all main character's (works in both single player and multiplayer)
   for(MainCharacter mc: mainCharacters){
      
      mc.update();
   
   }
   enemiesManager.update();
   if (enemiesManager.isCollision()) {
    mainCharacter.playDeadSound();
    gameState = GAME_OVER_STATE;
    mainCharacter.dead(true);
   }
  }
 }
 /**
  * State manager method
  * @param Graphics g the while loop for controlling what to draw when
  */
 public void paint(Graphics g) {
  g.setColor(Color.decode("#28282A")); //Setting background color
  g.fillRect(0, 0, getWidth(), getHeight());

  switch (gameState) {
   case START_GAME_STATE: 
   // draw all in a for loop
    
    for(MainCharacter mc: mainCharacters){
      count++;
      //chat.append("my count SGS"+count); 
      mc.draw(g);
   
    }
    g.setColor(Color.orange);
    g.setFont(new Font("Baghdad",Font.BOLD,25));
    g.drawString("Press Space to start! ", 250, 100);
    break;
   case GAME_PLAYING_STATE:
   case GAME_OVER_STATE:
    clouds.draw(g);
    land.draw(g);
    enemiesManager.draw(g);
    for(MainCharacter mc: mainCharacters){
    
      //chat.append("my count GOS"+count);
      mc.draw(g);
   
    }
    g.setColor(Color.orange);
    g.setFont(new Font("Baghdad",Font.BOLD,25));
    g.drawString("HI " + mainCharacter.score, 450, 30);
    if (gameState == GAME_OVER_STATE) {
     if (death) {
      int score = mainCharacter.getScore();
      chat.sendScore(score);
      chat.youDied(score);
      mainCharacter.stopBackMusic();
      death = false;
     }
     g.drawImage(gameOverButtonImage, 200, 30, null);
     g.drawImage(replayButtonImage, 283, 50, null);

    }
    break;
  }
 }
 
 @Override
 public void run() {

  int fps = 100;
  long msPerFrame = 1000 * 1000000 / fps;
  long lastTime = 0;
  long elapsed;

  int msSleep;
  int nanoSleep;

  long endProcessGame;
  long lag = 0;

  while (true) {
   gameUpdate();
   repaint();
   endProcessGame = System.nanoTime();
   elapsed = (lastTime + msPerFrame - System.nanoTime());
   msSleep = (int)(elapsed / 1000000);
   nanoSleep = (int)(elapsed % 1000000);
   if (msSleep <= 0) {
    lastTime = System.nanoTime();
    continue;
   }
   try {
    Thread.sleep(msSleep, nanoSleep);
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
   lastTime = System.nanoTime();
  }
 }

 @Override
 public void keyPressed(KeyEvent e) {
  if (!isKeyPressed) {
   isKeyPressed = true;
   switch (gameState) {
    case START_GAME_STATE:
     if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      gameState = GAME_PLAYING_STATE;
      mainCharacter.playBackMusic(); //Start background music
     }
     break;
    case GAME_PLAYING_STATE:
     if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      mainCharacter.jump();
     } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      mainCharacter.down(true);
     }
     break;
    case GAME_OVER_STATE:
     if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      gameState = GAME_PLAYING_STATE;
      resetGame();
     }
     break;

   }
  }
 }
 @Override
 public void keyReleased(KeyEvent e) {
  isKeyPressed = false;
  if (gameState == GAME_PLAYING_STATE) {
   if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    mainCharacter.down(false);
   }
  }
 }
 
 @Override
 public void keyTyped(KeyEvent e) {
  // TODO Auto-generated method stub

 }
 public void shareMainCharacter(){
     try{ 
         //FIRST PULL CHARACTERS TO GAMESCREEN VIA OOP INPUTS 
         Object charToDraw = ooi.readObject();
         addMainCharacter( (MainCharacter)charToDraw );
         oos.writeObject(mainCharacter);
         commenceUpdates = true;
    } catch(IOException ioe){ //Sharing my main character
         try{
            oos.writeObject((Object)mainCharacter);
            commenceUpdates = true;
         } catch(Exception e){
               e.printStackTrace();
         }   
    } catch(ClassNotFoundException cnfe){
         cnfe.printStackTrace();
    }
   }
 private void resetGame() {
  enemiesManager.reset();
  mainCharacter.dead(false);
  mainCharacter.reset();
  mainCharacter.playBackMusic(); //Start background music again
  death = true;
  //updateOtherPackets();
 }
 public void addMainCharacter(MainCharacter mc){
   mainCharacters.add(mc);
 }
}