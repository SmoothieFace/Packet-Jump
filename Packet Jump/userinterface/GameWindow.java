/**
 * This is the main method, where all the action starts, assigns that chat to it's own thread and the game screen to it's own thread.
 * @author Team Tough Times
 * @version 4-27-20
 */
package userinterface;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import gameobject.*;
public class GameWindow extends JFrame {

 public static final int SCREEN_WIDTH = 600;
 private GameScreen gameScreen;
 private ChatThread chatThread;
 private ChatClient playerChat = new ChatClient();
 private ChatClient readyChat;
 public GameWindow() {
  super("Packet Jump!");
  setSize(SCREEN_WIDTH, 175);
  setLocation(400, 200);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setResizable(false);


  chatThread = new ChatThread(playerChat);
  chatThread.start();

  readyChat = chatThread.getChat();
  while (readyChat.isNotReady()) { // Waits for the user to enter a name and ip
   readyChat = chatThread.getChat();
  }
  //Once ready, starts the game
  gameScreen = new GameScreen(readyChat);

  addKeyListener(gameScreen);
  add(gameScreen);
  addWindowListener(new CloseHandler());
  setVisible(true);
 }


 public static void main(String args[]) {
  GameWindow playerGame = new GameWindow();
 }

 private class CloseHandler extends WindowAdapter {
  public void windowClosing(WindowEvent we) {
   System.out.println("\nThank you for playing Packet Jump!");

  }
 }
}