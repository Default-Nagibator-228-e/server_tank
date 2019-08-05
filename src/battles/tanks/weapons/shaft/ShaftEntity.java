package battles.tanks.weapons.shaft;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class ShaftEntity implements IEntity {
   public float damage_min;
   public float damage_max;
   private ShotData shotData;
   public final EntityType type;

   public ShaftEntity(ShotData shotData, float damage_min, float damage_max) {
      this.type = EntityType.SHAFT;
      this.damage_min = damage_min;
      this.damage_max = damage_max;
      this.shotData = shotData;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return this.type;
   }
}
