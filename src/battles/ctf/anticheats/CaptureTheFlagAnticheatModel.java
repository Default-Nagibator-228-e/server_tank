package battles.ctf.anticheats;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.ctf.flags.FlagServer;
import battles.ctf.flags.FlagState;
import java.util.HashMap;

public class CaptureTheFlagAnticheatModel {
   private static final long MIN_TIME_DELIVERED = 4000L;
   private HashMap datas = new HashMap();
   private BattlefieldModel bfModel;

   public CaptureTheFlagAnticheatModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public boolean onTakeFlag(BattlefieldPlayerController taker, FlagServer flag) {
      CaptureTheFlagAnticheatModel.Data data = (CaptureTheFlagAnticheatModel.Data)this.datas.get(taker);
      if(data == null) {
         this.datas.put(taker, data = new CaptureTheFlagAnticheatModel.Data());
      }

      data.lastTimeTakeFlag = System.currentTimeMillis();
      data.prevState = flag.state;
      return false;
   }

   public boolean onDeliveredFlag(BattlefieldPlayerController taker, FlagServer flag) {
      CaptureTheFlagAnticheatModel.Data data = (CaptureTheFlagAnticheatModel.Data)this.datas.get(taker);
      long time;
      if((time = System.currentTimeMillis() - data.lastTimeTakeFlag) <= 4000L && data.prevState == FlagState.BASE) {
         this.bfModel.cheatDetected(taker, this.getClass());
         System.out.println(time);
         return true;
      } else {
         return false;
      }
   }

   class Data {
      long lastTimeTakeFlag;
      long lastTimeDeliveredFlag;
      FlagState prevState;
   }
}
