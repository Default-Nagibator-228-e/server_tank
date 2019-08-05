package battles.tanks.weapons.isida;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class IsidaEntity implements IEntity {
   public int capacity;
   public int chargeRate;
   public int dischargeRate;
   public int tickPeriod;
   public float lockAngle;
   public float lockAngleCos;
   public float maxAngle;
   public float maxAngleCos;
   public float maxRadius;
   private ShotData shotData;
   public float damage_min;
   public float damage_max;

   public IsidaEntity(int capacity, int chargeRate, int dischargeRate, int tickPeriod, float lockAngle, float lockAngleCos, float maxAngle, float maxAngleCos, float maxRadius, ShotData shotData, float damage_min, float damage_max) {
      this.capacity = capacity;
      this.chargeRate = chargeRate;
      this.dischargeRate = dischargeRate;
      this.tickPeriod = tickPeriod;
      this.lockAngle = lockAngle;
      this.lockAngleCos = lockAngleCos;
      this.maxAngle = maxAngle;
      this.maxAngleCos = maxAngleCos;
      this.maxRadius = maxRadius;
      this.shotData = shotData;
      this.damage_min = damage_min;
      this.damage_max = damage_max;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return EntityType.ISIDA;
   }
}
