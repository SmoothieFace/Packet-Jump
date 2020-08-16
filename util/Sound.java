/**
 * This is the Sound class that allocates a soundfile and allows it to be played through the soundsystem with simple controls.
 * @author Team Tough Times
 * @version 4-27-20
 */
package util;

import java.io.*;
import javafx.scene.media.*;

public class Sound {
  static int c = 0;
  Media media;
  MediaPlayer player;

  static {
    com.sun.javafx.application.PlatformImpl.startup(()->{});
  }

  public Sound(String resource) {
     try {
       media = new Media(ClassLoader.getSystemResource(resource).toURI().toString());
       player = new MediaPlayer(media);
     } catch (Exception e) {
       e.printStackTrace();
       System.exit(-1);
     }
   }

  public void play() {
    SoundSystem.waitAndPlay(this,-1L);
  }
  public boolean isPlaying() {
    return player.getStatus() == MediaPlayer.Status.PLAYING;
  }
  public void stop() {
    player.stop();
  }
}