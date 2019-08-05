package logger.statistic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CheatDetectedLogger {
   public static void cheatDetected(String nickname, Class anticheatClass, Class weaponModel, long reloadTime, String deltas) {
      String log = "\n--------------------------------------\nNickname: " + nickname + "\nAnticheat class: " + anticheatClass.getSimpleName() + "\nWeaponModel class: " + weaponModel.getSimpleName() + "\nNormal reload time: " + reloadTime + "\nDelts: " + deltas;

      try {
         Files.write(Paths.get("anticheats/logs/logs.data", new String[0]), log.getBytes(), new OpenOption[]{StandardOpenOption.APPEND});
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }
}
