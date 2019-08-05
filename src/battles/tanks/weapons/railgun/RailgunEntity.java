package battles.tanks.weapons.railgun;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class RailgunEntity implements IEntity {
   public int chargingTime;
   public int weakeningCoeff;
   public float damage_min;
   public float damage_max;
   private ShotData shotData;
   public final EntityType type;

   public RailgunEntity(ShotData shotData, int charingTime, int weakeningCoeff, float damage_min, float damage_max) {
      this.type = EntityType.RAILGUN;
      this.chargingTime = charingTime;
      this.weakeningCoeff = weakeningCoeff;
      this.damage_min = damage_min;
      this.damage_max = damage_max;
      this.shotData = shotData;
   }

   public String toString() {
      return "chargingTime: " + this.chargingTime + "\nweakeningCoeff: " + this.weakeningCoeff + "\ndamage_min: " + this.damage_min + "\ndamage_max: " + this.damage_max;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return this.type;
   }
}
