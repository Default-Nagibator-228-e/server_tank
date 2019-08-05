package battles.mines.model;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.mines.ServerMine;
import battles.mines.activator.MinesActivatorService;
import battles.spectator.SpectatorController;
import battles.tanks.math.Vector3;
import collections.FastHashMap;
import commands.Type;
import json.JSONUtils;
import services.annotations.ServicesInject;
import test.osgi.OSGi;
import test.server.configuration.entitys.MineConfiguratorEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BattleMinesModel {
   private static final String REMOVE_MINES_COMMAND = "remove_mines";
   private static final String INIT_MINES_COMMAND = "init_mines";
   private static final String HIT_MINE_COMMAND = "hit_mine";
   private static final String INIT_MINE_MODEL_COMMAND = "init_mine_model";
   private BattlefieldModel bfModel;
   private FastHashMap mines;
   private static String _initObjectData;
   @ServicesInject(
      target = MinesActivatorService.class
   )
   private MinesActivatorService minesActivatorService = MinesActivatorService.getInstance();
   private int _incrationId;
   private static int minDamage;
   private static int maxDamage;

   static {
      MineConfiguratorEntity configurator = (MineConfiguratorEntity)OSGi.getModelByInterface(MineConfiguratorEntity.class);
      minDamage = configurator.getMinDamage();
      maxDamage = configurator.getMaxDamage();
   }

   public BattleMinesModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
      this.mines = new FastHashMap();
   }

   public void sendMines(BattlefieldPlayerController player) {
      player.send(Type.BATTLE, new String[]{"init_mines", JSONUtils.parseInitMinesComand(this.mines)});
   }

   public void sendMines(SpectatorController spectator) {
      spectator.sendCommand(Type.BATTLE, new String[]{"init_mines", JSONUtils.parseInitMinesComand(this.mines)});
   }

   public void initModel(BattlefieldPlayerController player) {
      if(_initObjectData == null) {
         _initObjectData = JSONUtils.parseConfiguratorEntity(OSGi.getModelByInterface(MineConfiguratorEntity.class), MineConfiguratorEntity.class);
      }

      player.send(Type.BATTLE, new String[]{"init_mine_model", _initObjectData});
   }

   public void initModel(SpectatorController spectator) {
      if(_initObjectData == null) {
         _initObjectData = JSONUtils.parseConfiguratorEntity(OSGi.getModelByInterface(MineConfiguratorEntity.class), MineConfiguratorEntity.class);
      }

      spectator.sendCommand(Type.BATTLE, new String[]{"init_mine_model", _initObjectData});
   }

   public void tryPutMine(BattlefieldPlayerController player, Vector3 pos) {
      ServerMine mine = new ServerMine();
      mine.setId(player.tank.id + "_" + this._incrationId);
      mine.setOwner(player);
      mine.setPosition(pos);
      ArrayList userMines;
      if((userMines = (ArrayList)this.mines.get(player)) == null) {
         userMines = new ArrayList(Arrays.asList(new ServerMine[]{mine}));
         this.mines.put(player, userMines);
      } else {
         userMines.add(mine);
      }

      this.minesActivatorService.activate(this.bfModel, mine);
      ++this._incrationId;
   }

   public void playerDied(BattlefieldPlayerController player) {
      ArrayList _mines;
      if((_mines = (ArrayList)this.mines.get(player)) != null) {
         _mines.clear();
         this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"remove_mines", player.tank.id});
      }

   }

   public void hitMine(BattlefieldPlayerController whoHiter, String mineId) {
      BattlefieldPlayerController mineOwner = null;
      Iterator var5 = this.mines.values().iterator();

      while(true) {
         while(var5.hasNext()) {
            ArrayList serverMines = (ArrayList)var5.next();

            for(int i = 0; i < serverMines.size(); ++i) {
               ServerMine _mine = (ServerMine)serverMines.get(i);
               if(_mine.getId().equals(mineId)) {
                  mineOwner = _mine.getOwner();
                  serverMines.remove(i);
                  break;
               }
            }
         }

         this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"hit_mine", mineId, whoHiter.tank.id});
         if(mineOwner != null) {
            this.bfModel.tanksKillModel.damageTank(whoHiter, mineOwner, RandomUtils.getRandom((float)minDamage, (float)maxDamage), false,false);
         }

         return;
      }
   }
}
