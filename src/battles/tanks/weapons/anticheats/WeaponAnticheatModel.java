package battles.tanks.weapons.anticheats;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;
import logger.statistic.CheatDetectedLogger;
import java.util.ArrayList;

/** @deprecated */
@Deprecated
@AnticheatModel(
   name = "AnticheatModel",
   actionInfo = "Сравнивает время прошлого выстрела, если оно меньше чем норма - читер."
)
public class WeaponAnticheatModel {
   private int timeReloadWeapon;
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private ArrayList suspiciousDeltas;
   private long lastFireTime;

   public WeaponAnticheatModel(int timeReloadWeapon, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      this.timeReloadWeapon = timeReloadWeapon;
      this.bfModel = bfModel;
      this.player = player;
      this.suspiciousDeltas = new ArrayList();
   }

   public boolean onFire() {
      long delta;
      if((delta = System.currentTimeMillis() - this.lastFireTime) <= (long)this.timeReloadWeapon) {
         if(this.suspiciousDeltas.size() >= 7) {
            CheatDetectedLogger.cheatDetected(this.player.getUser().getNickname(), this.getClass(), this.player.tank.getWeapon().getClass(), (long)this.timeReloadWeapon, this.deltasToString());
            this.bfModel.cheatDetected(this.player, this.getClass());
         }

         this.suspiciousDeltas.add(Long.valueOf(delta));
         this.lastFireTime = System.currentTimeMillis();
         return true;
      } else {
         this.lastFireTime = System.currentTimeMillis();
         return false;
      }
   }

   public boolean onStartFire() {
      return false;
   }

   public boolean onStopFire() {
      return false;
   }

   private String deltasToString() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < this.suspiciousDeltas.size(); ++i) {
         sb.append(String.valueOf(this.suspiciousDeltas.get(i)));
         if(i != this.suspiciousDeltas.size() - 1) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }
}
