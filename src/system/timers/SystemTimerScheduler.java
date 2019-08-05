package system.timers;

import java.util.Timer;
import java.util.TimerTask;

public class SystemTimerScheduler {
   private static final Timer TIMER = new Timer("SystemTimerScheduler timer");

   public static void scheduleTask(TimerTask task, long delay) {
      TIMER.schedule(task, delay);
   }

   public static void scheduleTask(final TimerTaskExecutor task, long delay) {
      TIMER.schedule(new TimerTask() {
         public void run() {
            try {
               task.run();
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         }
      }, delay);
   }
}
