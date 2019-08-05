package battles.bonuses;

import battles.tanks.math.Vector3;

public class Bonus {
   public Vector3 position;
   public BonusType type;
   public long spawnTime;
   public int dis;
   public int id;

   public Bonus(Vector3 position, BonusType type, int id, int dis) {
      this.position = position;
      this.type = type;
      this.spawnTime = System.currentTimeMillis();
      this.id = id;
      this.dis = dis;
   }
}
