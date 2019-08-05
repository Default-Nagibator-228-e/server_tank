//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package battles.maps.parser.map;

import battles.maps.parser.map.spawn.SpawnPosition;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
        name = "spawn-points"
)
class SpawnPoints {
   private List<SpawnPosition> spawnPositions;

   SpawnPoints() {
   }

   public List<SpawnPosition> getSpawnPositions() {
      return this.spawnPositions;
   }

   @XmlElement(
           name = "spawn-point"
   )
   public void setSpawnPositions(List<SpawnPosition> spawnPositions) {
      this.spawnPositions = spawnPositions;
   }
}
