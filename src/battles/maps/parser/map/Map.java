//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package battles.maps.parser.map;

import battles.maps.parser.Vector3d;
import battles.maps.parser.map.bonus.BonusRegion;
import battles.maps.parser.map.spawn.SpawnPosition;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "map")
@XmlRootElement

public class Map {
   private SpawnPoints spawnPoints;
   private BonusRegions bonusRegions;
   private FlagsPositions flagPositions;

   public Map() {
   }

   public SpawnPoints getSpawnPoints() {
      return this.spawnPoints;
   }

   @XmlElement(
           name = "spawn-points"
   )
   public void setSpawnPoints(SpawnPoints spawnPoints) {
      this.spawnPoints = spawnPoints;
   }

   public BonusRegions getBonusRegions() {
      return this.bonusRegions;
   }

   @XmlElement(
           name = "bonus-regions"
   )
   public void setBonusRegions(BonusRegions bonusRegions) {
      this.bonusRegions = bonusRegions;
   }

   public FlagsPositions getFlagPositions() {
      return this.flagPositions;
   }

   @XmlElement(
           name = "ctf-flags"
   )
   public void setFlagPositions(FlagsPositions flagPositions) {
      this.flagPositions = flagPositions;
   }

   public Vector3d getPositionBlueFlag() {
      return this.getFlagPositions() != null ? this.getFlagPositions().getBlueFlag() : null;
   }

   public Vector3d getPositionRedFlag() {
      return this.getFlagPositions() != null ? this.getFlagPositions().getRedFlag() : null;
   }

   public List<SpawnPosition> getSpawnPositions() {
      return this.spawnPoints.getSpawnPositions();
   }

   public List<BonusRegion> getBonusesRegion() {
      return this.bonusRegions.getBonusRegions();
   }
}
