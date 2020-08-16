/**
 * MainCharacter is a class that represents the main character
 * (the dinosaur). It displays different animation of the
 * character throughout the game. Also sources all the audio for the game.
 * @author Team Tough Times
 * @version 4-27-20
 */

package gameobject;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import userinterface.*;
import util.Animation;
import util.Resource;
import util.*;
import java.io.*;
import javax.sound.sampled.*;

public class MainCharacter implements Serializable{

	public static final int LAND_POSY = 80;
	public static final float GRAVITY = 0.4f;
	
	private static final int NORMAL_RUN = 0;
	private static final int JUMPING = 1;
	private static final int DOWN_RUN = 2;
	private static final int DEATH = 3;
	
	private float posY;
	private float posX;
	private float speedX;
	private float speedY;
	private Rectangle rectBound;
	public int score = 0;
	public int sendCount = 0;
	private int state = NORMAL_RUN;
	private static Animation normalRunAnim;
	private static Animation downRunAnim;
   private static BufferedImage jumping;
	private static BufferedImage deathImage;
	private boolean musicStartStop;
	private transient AudioClip jumpSound;
	private transient AudioClip deadSound;
	private transient AudioClip scoreUpSound;
   //private AudioInputStream backgroundAudio;
   //private Clip audioClip ;
   private transient Sound backgroundSound;
   private transient SoundSystem soundSystem;
   private transient URL url;
   /**
    * This is the constructor used to create a new object of the MainCharacter class.
    * It uses different images, animations, and sounds for the character
    */
	public MainCharacter() {
		posX = 50;
		posY = LAND_POSY;
		rectBound = new Rectangle();
		normalRunAnim = new Animation(90);
		normalRunAnim.addFrame(Resource.getResourceImage("data/main-character1.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/main-character2.png"));
		jumping = Resource.getResourceImage("data/main-character3.png");
		downRunAnim = new Animation(90);
		downRunAnim.addFrame(Resource.getResourceImage("data/main-character5.png"));
		downRunAnim.addFrame(Resource.getResourceImage("data/main-character6.png"));
		deathImage = Resource.getResourceImage("data/main-character4.png");
		
		try {
         soundSystem = new SoundSystem(); // Creating the sound system for the background track
         backgroundSound = new Sound("data/background.wav"); //Allocating the file
			jumpSound =  Applet.newAudioClip(new URL("file","","data/jump.wav"));
			deadSound =  Applet.newAudioClip(new URL("file","","data/dead.wav"));
			scoreUpSound =  Applet.newAudioClip(new URL("file","","data/scoreup.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
   /**
    * Gets speed.
    * @return speed
    */
	public float getSpeedX() {
		return speedX;
	}
   
   /**
    * Sets speed of the character.
    */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
  
	/**
    * Controlling the state of the dinasaur at all times
    * @param Graphics g the running dinosaur moving
    */
	public void draw(Graphics g) {
		switch(state) {
			case NORMAL_RUN:
				g.drawImage(normalRunAnim.getFrame(), (int) posX, (int) posY, null);
            sendCount = 0;
				break;
			case JUMPING:
				g.drawImage(jumping, (int) posX, (int) posY, null);
            sendCount = 0;
				break;
			case DOWN_RUN:
				g.drawImage(downRunAnim.getFrame(), (int) posX, (int) (posY + 20), null);
            sendCount = 0;
				break;
			case DEATH:
				g.drawImage(deathImage, (int) posX, (int) posY, null);
				break;
		}
//		Rectangle bound = getBound();
//		g.setColor(Color.RED);
//		g.drawRect(bound.x, bound.y, bound.width, bound.height);
	}
	
   /**
    * Updates the speed of the character.
    */
	public void update() {
		normalRunAnim.updateFrame();
		downRunAnim.updateFrame();
		if(posY >= LAND_POSY) {
			posY = LAND_POSY;
			if(state != DOWN_RUN) {
				state = NORMAL_RUN;
			}
		} else {
			speedY += GRAVITY;
			posY += speedY;
		}
	}
   
	/**
    * Plays sound and shows character jumping.
    */
	public void jump() {
		if(posY >= LAND_POSY) {
			if(jumpSound != null) {
				jumpSound.play();
			}
			speedY = -7.5f;
			posY += speedY;
			state = JUMPING;
		}
	}
	
   /**
    * Shows character ducking.
    */
	public void down(boolean isDown) {
		if(state == JUMPING) {
			return;
		}
		if(isDown) {
			state = DOWN_RUN;
		} else {
			state = NORMAL_RUN;
		}
	}
   
	/**
    * ////////////////////////////////////////////////////////////////////////////////.
    */
	public Rectangle getBound() {
		rectBound = new Rectangle();
		if(state == DOWN_RUN) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY + 20;
			rectBound.width = downRunAnim.getFrame().getWidth() - 10;
			rectBound.height = downRunAnim.getFrame().getHeight();
		} else {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = normalRunAnim.getFrame().getWidth() - 10;
			rectBound.height = normalRunAnim.getFrame().getHeight();
		}
		return rectBound;
	}
	/**
    * If the dinasaur is dead change the state to DEATH
    * @param isDeath if the dinasaur is dead or not
    */
	public void dead(boolean isDeath) {
		if(isDeath) {
			state = DEATH;
		} else {
			state = NORMAL_RUN;
		}
	}
	public void reset() {
		posY = LAND_POSY;
      score = 0;
	}
	
   /**
    * Plays dead sound.
    */
	public void playDeadSound() {
		deadSound.play();
	}
   public void playBackMusic(){
      backgroundSound.play();
   }
   public void stopBackMusic(){
      backgroundSound.stop();
   }
   /**
    * Add 20 to score and plays a sound every time
    * score reaches increments of 100.
    */
	public void upScore() {
		score += 20;
		if(score % 100 == 0) {
			scoreUpSound.play();
		}
	}
   public int getScore(){
      return score;
   }
	
}
