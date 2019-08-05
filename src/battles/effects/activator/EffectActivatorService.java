package battles.effects.activator;

import java.util.Timer;
import java.util.TimerTask;

public class EffectActivatorService {
   private static EffectActivatorService instance = new EffectActivatorService();
   private static final Timer TIMER = new Timer();

   public static EffectActivatorService getInstance() {
      return instance;
   }

   public void activateEffect(TimerTask effectTask, long delay) {
      TIMER.schedule(effectTask, delay);
   }
}
