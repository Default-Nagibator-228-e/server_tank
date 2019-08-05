package battles.tanks.weapons;

public interface IEntity {
   int chargingTime = 0;
   int weakeningCoeff = 0;
   float damage_min = 0.0F;
   float damage_max = 0.0F;

   ShotData getShotData();

   EntityType getType();

   String toString();
}
