package battles.anticheats;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.math.Vector3;

public class MoveComandAnticheat {
   private static final long MAX_TIME_CHECK = 2000L;
   private static final long MAX_DISTANCE = 2000L;
   private BattlefieldModel bfModel;
   private Vector3 lastPosition;
   private long lastTimeSentComand;

   public MoveComandAnticheat(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public void onSentComand(Vector3 pos) {
      if(System.currentTimeMillis() - this.lastTimeSentComand < 2000L) {
         if(this.lastPosition.distanceTo(pos) >= 2000.0D) {
            this.bfModel.cheatDetected((BattlefieldPlayerController)null, this.getClass());
         }

      }
   }
}
