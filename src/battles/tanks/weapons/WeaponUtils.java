package battles.tanks.weapons;

import battles.tanks.Tank;

public class WeaponUtils {
   public static int calculateHealth(Tank tank, float damage) {
      float _damage = 10000.0F / (tank.getHull().hp / damage);
      return (int)_damage;
   }

   public static float calculateDamageFromDistance(float damage, int procent) {
      return damage - damage * (float)(procent / 100);
   }

   public static float calculateDamageWithResistance(float damage, int resistancePercent) {
      return damage - damage / 100.0F * (float)resistancePercent;
   }

   public static float transformDamageToTank(float hullHp, float damage) {
      return 10000.0F / (hullHp / damage);
   }

   public static int calculateHealthTest(int hp, int hullHp, float damage) {
      float b = 10000.0F / ((float)hullHp / damage);
      return (int)((float)hp - b);
   }
}
