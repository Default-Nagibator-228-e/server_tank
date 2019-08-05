package battles.managers;

import battles.maps.Map;
import battles.tanks.math.Vector3;
import java.util.Random;

public class SpawnManager {
   private static Random rand = new Random();

   public static Vector3 getSpawnState(Map map, String forTeam) {
      Vector3 pos = null;

      try {
         if(forTeam.equals("BLUE")) {
            pos = (Vector3)map.spawnPositonsBlue.get(rand.nextInt(map.spawnPositonsBlue.size()));
         } else if(forTeam.equals("RED")) {
            pos = (Vector3)map.spawnPositonsRed.get(rand.nextInt(map.spawnPositonsRed.size()));
         } else {
            pos = (Vector3)map.spawnPositonsDM.get(rand.nextInt(map.spawnPositonsDM.size()));
         }

         if(pos == null) {
            pos = (Vector3)map.spawnPositonsDM.get(rand.nextInt(map.spawnPositonsDM.size()));
         }
      } catch (Exception var4) {
         pos = (Vector3)map.spawnPositonsDM.get(rand.nextInt(map.spawnPositonsDM.size()));
      }

      return pos;
   }
}
