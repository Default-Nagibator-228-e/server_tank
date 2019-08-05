package battles.ctf;

import battles.BattlefieldPlayerController;
import battles.ctf.flags.FlagServer;

public class FlagReturnTimer extends Thread {
   public boolean stop = false;
   private CTFModel ctfModel;
   private FlagServer flag;

   public FlagReturnTimer(CTFModel ctfModel, FlagServer flag) {
      super.setName("FlagReturnTimer THREAD");
      this.ctfModel = ctfModel;
      this.flag = flag;
   }

   public void run() {
      try {
         sleep(20000L);
         if(!this.stop) {
            this.ctfModel.returnFlag((BattlefieldPlayerController)null, this.flag);
         }
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }
}
