package system;

import battles.BattlefieldModel;
import lobby.battles.BattlesList;
import logger.Logger;
import services.AutoEntryServices;
import services.LobbysServices;
import services.annotations.ServicesInject;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BattlesGC {
   private static final long TIME_FOR_REMOVING_EMPTY_BATTLE = 80000L;
   private static HashMap battlesForRemove = new HashMap();
   @ServicesInject(
      target = LobbysServices.class
   )
   private static LobbysServices lobbysServices = LobbysServices.getInstance();
   @ServicesInject(
      target = AutoEntryServices.class
   )
   private static AutoEntryServices autoEntryServices = AutoEntryServices.instance();

   public static void addBattleForRemove(BattlefieldModel battle) {
      if(battle != null) {
         Timer timer = new Timer("BattlesGC::Timer for battle: " + battle.battleInfo.battleId);
         timer.schedule(new BattlesGC.RemoveBattleTask(battle), 80000L);
         battlesForRemove.put(battle, timer);
      }
   }

   public static void cancelRemoving(BattlefieldModel model) {
      Timer timer = (Timer)battlesForRemove.get(model);
      if(timer != null) {
         timer.cancel();
         battlesForRemove.remove(model);
      }
   }

   private static void removeEmptyBattle(BattlefieldModel battle) {
      Logger.log("[BattlesGarbageCollector]: battle[" + battle.battleInfo + "] has been deleted by inactivity.");
      BattlesList.removeBattle(battle.battleInfo);
      autoEntryServices.battleDisposed(battle);
   }

   static class RemoveBattleTask extends TimerTask {
      private BattlefieldModel battle;

      public RemoveBattleTask(BattlefieldModel battle) {
         this.battle = battle;
      }

      public void run() {
         if(this.battle != null) {
            BattlesGC.removeEmptyBattle(this.battle);
         }

      }
   }
}
