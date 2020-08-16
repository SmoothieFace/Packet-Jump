/**
 * This is the SoundSystem that allows the playback of Sounds.
 * @author Team Tough Times
 * @version 4-27-20
 */
package util;

import java.io.*;
import javafx.scene.media.*;

public class SoundSystem {
  public static MediaPlayer[] players = new MediaPlayer[32];

  public static boolean isAvailable(int i) { return players[i] == null || players[i].getStatus() == MediaPlayer.Status.STOPPED; }

  public static MediaPlayer getAvailablePlayer(Media m) {
    for (int i = 0; i < players.length; i++) {
      if (isAvailable(i)) {
        players[i] = new MediaPlayer(m);
        return players[i];
      }
    }
    return null;
  }
  public static MediaPlayer waitForAvailablePlayer(Media m,long timeout) {
    long st = System.nanoTime();
    while ((System.nanoTime() - st < timeout) || timeout < 0L) {
      for (int i = 0; i < players.length; i++) {
        if (isAvailable(i)) {
          players[i] = new MediaPlayer(m);
          return players[i];
        }
      }
    }
    return null;
  }

  public static void waitAndPlay(Sound sound,long timeout) {
    MediaPlayer pl = waitForAvailablePlayer(sound.media,timeout);
    sound.player = pl;
    pl.play();
  }
}