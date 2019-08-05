package battles.tanks.weapons.hammer;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class HammerEntity implements IEntity {
   public float shotRange;
   public float shotSpeed;
   public float shotRadius;
   public float damage_min;
   public float damage_max;
   public float frezeeSpeed;
   private ShotData shotData;

   public HammerEntity(float shotRange, float shotSpeed, float shotRadius, float damage_min, float damage_max, float frezeeSpeed, ShotData shotData) {
      this.shotRange = shotRange;
      this.shotSpeed = shotSpeed;
      this.shotRadius = shotRadius;
      this.shotData = shotData;
      this.damage_max = damage_max;
      this.damage_min = damage_min;
      this.frezeeSpeed = frezeeSpeed;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return EntityType.HAMMER;
   }
}
