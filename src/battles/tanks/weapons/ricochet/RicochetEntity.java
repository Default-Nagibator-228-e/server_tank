package battles.tanks.weapons.ricochet;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class RicochetEntity implements IEntity {
   public float shotRadius;
   public float shotSpeed;
   public int energyCapacity;
   public int energyPerShot;
   public float energyRechargeSpeed;
   public float shotDistance;
   public float damage_min;
   public float damage_max;
   private ShotData shotData;

   public RicochetEntity(float shotRadius, float shotSpeed, int energyCapacity, int energyPerShot, float energyRechargeSpeed, float shotDistance, float damage_min, float damage_max, ShotData shotData) {
      this.shotRadius = shotRadius;
      this.shotSpeed = shotSpeed;
      this.energyCapacity = energyCapacity;
      this.energyPerShot = energyPerShot;
      this.energyRechargeSpeed = energyRechargeSpeed;
      this.shotDistance = shotDistance;
      this.damage_min = damage_min;
      this.damage_max = damage_max;
      this.shotData = shotData;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return EntityType.RICOCHET;
   }
}
