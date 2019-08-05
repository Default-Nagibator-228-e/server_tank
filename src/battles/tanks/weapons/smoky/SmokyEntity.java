package battles.tanks.weapons.smoky;

import battles.tanks.weapons.EntityType;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.ShotData;

public class SmokyEntity implements IEntity {
   public float damage_min;
   public float damage_max;
   public float aper;
   public float dper;
   public float per;
   private ShotData shotData;
   public final EntityType type;

   public SmokyEntity(ShotData shotData, float damage_min, float damage_max, float per, float aper, float dper) {
      this.type = EntityType.SMOKY;
      this.damage_min = damage_min;
      this.damage_max = damage_max;
      this.per = per;
      this.dper = dper;
      this.aper = aper;
      this.shotData = shotData;
   }

   public ShotData getShotData() {
      return this.shotData;
   }

   public EntityType getType() {
      return this.type;
   }
}
