package battles.tanks.weapons.flamethrower;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class FlamethrowerEntity implements IEntity {
   public int targetDetectionInterval;
   public float range;
   public float coneAngle;
   public int heatingSpeed;
   public int coolingSpeed;
   public int heatLimit;
   public float damage_min;
   public float damage_max;
   public ShotData shotData;
   public final EntityType type;

   public FlamethrowerEntity(int targetDetectionInterval, float range, float coneAngle, int heatingSpeed, int coolingSpeed, int heatLimit, ShotData shotData, float damageMax, float damageMin) {
      this.type = EntityType.FLAMETHROWER;
      this.targetDetectionInterval = targetDetectionInterval;
      this.range = range;
      this.coneAngle = coneAngle;
      this.heatingSpeed = heatingSpeed;
      this.coolingSpeed = coolingSpeed;
      this.heatLimit = heatLimit;
      this.shotData = shotData;
      this.damage_min = damageMin;
      this.damage_max = damageMax;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return this.type;
   }
}
