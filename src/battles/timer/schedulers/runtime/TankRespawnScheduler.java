package battles.timer.schedulers.runtime;

import utils.StringUtils;
import battles.BattlefieldPlayerController;
import battles.managers.SpawnManager;
import battles.tanks.math.Vector3;
import commands.Type;
import json.JSONUtils;
import logger.remote.RemoteDatabaseLogger;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TankRespawnScheduler {
   private static final Timer TIMER = new Timer("TankRespawnScheduler timer");
   private static final long TIME_TO_PREPARE_SPAWN = 3000L;
   private static final long TIME_TO_SPAWN = 5000L;
   private static HashMap tasks = new HashMap();
   private static boolean disposed;

   public static void startRespawn(BattlefieldPlayerController player, boolean onlySpawn) {
      if(!disposed) {
         try {
            if(player == null) {
               return;
            }

            if(player.battle == null) {
               return;
            }

            TankRespawnScheduler.PrepareToSpawnTask ex = new TankRespawnScheduler.PrepareToSpawnTask();
            ex.player = player;
            ex.onlySpawn = onlySpawn;
            tasks.put(player, ex);
            TIMER.schedule(ex, onlySpawn?1L:3000L);
         } catch (Exception var3) {
            var3.printStackTrace();
            RemoteDatabaseLogger.error(var3);
         }

      }
   }

   public static void dispose() {
      disposed = true;
   }

   public static void cancelRespawn(BattlefieldPlayerController player) {
      try {
         TankRespawnScheduler.PrepareToSpawnTask ex = (TankRespawnScheduler.PrepareToSpawnTask)tasks.get(player);
         if(ex == null) {
            return;
         }

         if(ex.spawnTask == null) {
            ex.cancel();
         } else {
            ex.spawnTask.cancel();
         }

         tasks.remove(player);
      } catch (Exception var2) {
         var2.printStackTrace();
         RemoteDatabaseLogger.error(var2);
      }

   }

   static class PrepareToSpawnTask extends TimerTask {
      public TankRespawnScheduler.SpawnTask spawnTask;
      public BattlefieldPlayerController player;
      public Vector3 preparedPosition;
      public boolean onlySpawn;

      public void run() {
         try {
            if(this.player == null) {
               return;
            }

            if(this.player.tank == null) {
               return;
            }

            if(this.player.battle == null) {
               return;
            }

            this.preparedPosition = SpawnManager.getSpawnState(this.player.battle.battleInfo.map, this.player.playerTeamType);
            if(this.onlySpawn) {
               this.player.tank.position = this.preparedPosition;
               this.player.send(Type.BATTLE, new String[]{"prepare_to_spawn", StringUtils.concatStrings(new String[]{this.player.tank.id, ";", String.valueOf(this.preparedPosition.x), "@", String.valueOf(this.preparedPosition.y), "@", String.valueOf(this.preparedPosition.z), "@", String.valueOf(this.preparedPosition.rot)})});
               this.spawnTask = new TankRespawnScheduler.SpawnTask();
               this.spawnTask.preparedSpawnTask = this;
               TankRespawnScheduler.TIMER.schedule(this.spawnTask, 100L);
            } else {
               if(this.player.battle == null) {
                  return;
               }

               this.player.tank.position = this.preparedPosition;
               this.player.send(Type.BATTLE, new String[]{"prepare_to_spawn", StringUtils.concatStrings(new String[]{this.player.tank.id, ";", String.valueOf(this.preparedPosition.x), "@", String.valueOf(this.preparedPosition.y), "@", String.valueOf(this.preparedPosition.z), "@", String.valueOf(this.preparedPosition.rot)})});
               this.spawnTask = new TankRespawnScheduler.SpawnTask();
               this.spawnTask.preparedSpawnTask = this;
               TankRespawnScheduler.TIMER.schedule(this.spawnTask, 5000L);
            }
         } catch (Exception var2) {
            var2.printStackTrace();
            RemoteDatabaseLogger.error(var2);
         }

      }
   }

   static class SpawnTask extends TimerTask {
      TankRespawnScheduler.PrepareToSpawnTask preparedSpawnTask;

      public void run() {
         try {
            BattlefieldPlayerController ex = this.preparedSpawnTask.player;
            if(ex == null) {
               return;
            }

            if(ex.tank == null) {
               return;
            }

            if(ex.battle == null) {
               return;
            }

            ex.battle.tanksKillModel.changeHealth(ex.tank, 10000);
            ex.battle.sendToAllPlayers(Type.BATTLE, new String[]{"spawn", JSONUtils.parseSpawnCommand(ex, this.preparedSpawnTask.preparedPosition)});
            ex.tank.state = "newcome";
            TankRespawnScheduler.tasks.remove(ex);
         } catch (Exception var2) {
            var2.printStackTrace();
            RemoteDatabaseLogger.error(var2);
         }

      }
   }
}
