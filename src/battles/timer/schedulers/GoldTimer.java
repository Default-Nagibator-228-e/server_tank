package battles.timer.schedulers;

import battles.bonuses.BonusType;
import battles.bonuses.BonusesSpawnService;


public class GoldTimer implements Runnable {
   public long time;
   public BonusesSpawnService event;

   public GoldTimer(long time, BonusesSpawnService event) {
      this.time = time;
      this.event = event;
      (new Thread(this)).start();
   }

   public void run() {
      try {
         Thread.sleep(this.time);
         event.spawnBonus(BonusType.GOLD);
         (new Thread(this)).stop();
         event.dsff.remove(this);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }
}
