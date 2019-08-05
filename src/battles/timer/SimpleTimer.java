package battles.timer;

public class SimpleTimer implements Runnable {
   public long time;
   public SimpleTimerEvent event;

   public SimpleTimer(long time, SimpleTimerEvent event) {
      this.time = time;
      this.event = event;
   }

   public void start() {
      (new Thread(this)).start();
   }

   public void run() {
      try {
         Thread.sleep(this.time);
         this.event.eventComplete();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }
}
