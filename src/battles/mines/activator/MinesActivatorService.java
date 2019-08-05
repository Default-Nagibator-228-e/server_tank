package battles.mines.activator;

import battles.BattlefieldModel;
import battles.mines.ServerMine;
import test.osgi.OSGi;
import test.server.configuration.entitys.MineConfiguratorEntity;
import java.util.Timer;

public class MinesActivatorService {
   private static final int ACTIVATION_TIME = ((MineConfiguratorEntity)OSGi.getModelByInterface(MineConfiguratorEntity.class)).getActivationTimeMsec();
   private static final MinesActivatorService instance = new MinesActivatorService();
   private static final Timer TIMER = new Timer("MinesActivatorService Timer");

   public static MinesActivatorService getInstance() {
      return instance;
   }

   public void activate(BattlefieldModel model, ServerMine mine) {
      MineActivator activator = new MineActivator(model, mine);
      TIMER.schedule(activator, (long)ACTIVATION_TIME);
      activator.putMine();
   }
}
