package system.timers;

import system.timers.exceptions.TimerCausedException;

/** @deprecated */
@Deprecated
public class InterruptedTimer implements Runnable {
   private long time;
   private boolean interrupted = false;
   private boolean caused = false;
   private InterruptedTimerEvent event;

   public void startTimer(long time, InterruptedTimerEvent event) throws TimerCausedException {
      if(this.caused) {
         throw new TimerCausedException("InterruptedTimer Has already been called");
      } else {
         this.caused = true;
         this.time = time;
         this.event = event;
         (new Thread(this)).start();
      }
   }

   public void stopTimer() {
      this.interrupted = true;
   }

   private void onFinishTimer() {
      this.event.onComplete();
   }

   public void run() {
      try {
         Thread.sleep(this.time);
         if(!this.interrupted) {
            this.onFinishTimer();
         }
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }
}
