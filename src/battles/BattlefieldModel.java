package battles;

import utils.StringUtils;
import battles.anticheats.AnticheatModel;
import battles.bonuses.Bonus;
import battles.bonuses.BonusRegion;
import battles.bonuses.BonusesSpawnService;
import battles.bonuses.model.BonusTakeModel;
import battles.chat.BattlefieldChatModel;
import battles.comands.BattlefieldModelComandsConst;
import battles.ctf.CTFModel;
import battles.effects.model.EffectsVisualizationModel;
import battles.managers.SpawnManager;
import battles.maps.MapChecksumModel;
import battles.mines.model.BattleMinesModel;
import battles.spectator.SpectatorController;
import battles.spectator.SpectatorModel;
import battles.tanks.Tank;
import battles.tanks.math.Vector3;
import battles.tanks.statistic.PlayersStatisticModel;
import battles.timer.schedulers.bonuses.BonusesScheduler;
import battles.timer.schedulers.runtime.TankRespawnScheduler;
import collections.FastHashMap;
import commands.Type;
import json.JSONUtils;
import lobby.battles.BattleInfo;
import lobby.battles.BattlesList;
import logger.Logger;
import logger.remote.RemoteDatabaseLogger;
import services.AutoEntryServices;
import services.TanksServices;
import services.annotations.ServicesInject;
import system.BattlesGC;
import system.destroy.Destroyable;
import system.quartz.QuartzService;
import system.quartz.TimeType;
import system.quartz.impl.QuartzServiceImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BattlefieldModel extends BattlefieldModelComandsConst implements Destroyable {
   public static final String QUARTZ_GROUP = BattlefieldModel.class.getName();
   public final String QUARTZ_NAME;
   public final String QUARTZ_RESTART_NAME;
   @ServicesInject(
      target = TanksServices.class
   )
   private TanksServices tanksServices = TanksServices.getInstance();
   @ServicesInject(
      target = QuartzService.class
   )
   private QuartzService quartzService = QuartzServiceImpl.inject();
   @ServicesInject(
      target = AutoEntryServices.class
   )
   private AutoEntryServices autoEntryServices = AutoEntryServices.instance();
   public FastHashMap players;
   public HashMap activeBonuses;
   public BattleInfo battleInfo;
   public int incration = 0;
   private boolean battleFinish = false;
   private long endBattleTime = 0L;
   private JSONParser jsonParser = new JSONParser();
   public PlayersStatisticModel statistics;
   public TankKillModel tanksKillModel;
   public CTFModel ctfModel;
   public BattlefieldChatModel chatModel;
   public BonusesSpawnService bonusesSpawnService;
   public SpectatorModel spectatorModel;
   public EffectsVisualizationModel effectsModel;
   public BonusTakeModel bonusTakeModel;
   public BattleMinesModel battleMinesModel;
   public MapChecksumModel mapChecksumModel;

   public BattlefieldModel(BattleInfo battleInfo) {
      this.battleInfo = battleInfo;
      this.statistics = new PlayersStatisticModel(this);
      this.tanksKillModel = new TankKillModel(this);
      this.chatModel = new BattlefieldChatModel(this);
      this.spectatorModel = new SpectatorModel(this);
      this.effectsModel = new EffectsVisualizationModel(this);
      this.bonusTakeModel = new BonusTakeModel(this);
      this.battleMinesModel = new BattleMinesModel(this);
      this.mapChecksumModel = new MapChecksumModel(this);
      this.QUARTZ_NAME = "TimeBattle " + this.hashCode() + " battle=" + battleInfo.battleId;
      this.QUARTZ_RESTART_NAME = "RestartBattle  battle=" + battleInfo.battleId;
      if (battleInfo.time > 0) {
         this.startTimeBattle();
      }

      if (battleInfo.battleType.equals("CTF")) {
         this.ctfModel = new CTFModel(this);
      }

      this.players = new FastHashMap();
      this.activeBonuses = new HashMap();
      this.bonusesSpawnService = new BonusesSpawnService(this);
      if (!this.battleInfo.inventory || !this.battleInfo.isPaid) {
         (new Thread(this.bonusesSpawnService)).start();
      }
      BattlesGC.addBattleForRemove(this);
   }

   private void startTimeBattle() {
      //if(this.endBattleTime == 0L) {
         this.endBattleTime = System.currentTimeMillis() + (long) (this.battleInfo.time * 1000);
      //}
      this.quartzService.addJob(this.QUARTZ_NAME, QUARTZ_GROUP, (e) -> {
         Logger.debug("battle end...");
         this.tanksKillModel.restartBattle(true);
      }, TimeType.SEC, (long)this.battleInfo.time);
   }

   public void sendTableMessageToPlayers(String msg) {
      this.sendToAllPlayers(Type.BATTLE, new String[]{"show_warning_table", msg});
   }

   public void battleRestart() {
      if(this.battleInfo.team) {
         this.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "RED", String.valueOf(this.battleInfo.scoreRed)});
         this.sendToAllPlayers(Type.BATTLE, new String[]{"change_team_scores", "BLUE", String.valueOf(this.battleInfo.scoreBlue)});
      }

      this.battleFinish = false;
      Iterator var2 = this.players.values().iterator();

      while(var2.hasNext()) {
         BattlefieldPlayerController currentTimeMillis = (BattlefieldPlayerController)var2.next();
         if(currentTimeMillis != null) {
            currentTimeMillis.statistic.clear();
            currentTimeMillis.clearEffects();
            this.respawnPlayer(currentTimeMillis, false);
         }
      }
      this.sendToAllPlayers(Type.BATTLE, new String[]{"battle_restart", String.valueOf(this.battleInfo.time)});
      if(this.battleInfo.time > 0) {
         this.startTimeBattle();
      }

   }

   public void battleFinish() {
      if(this.players != null) {
         this.battleFinish = true;
         if(this.activeBonuses != null) {
            this.activeBonuses.clear();
         }

         this.bonusesSpawnService.battleFinished();
         this.tanksKillModel.setBattleFund(0);
         this.battleInfo.scoreBlue = 0;
         this.battleInfo.scoreRed = 0;
         Iterator var2 = this.players.values().iterator();

         while(var2.hasNext()) {
            BattlefieldPlayerController player = (BattlefieldPlayerController)var2.next();
            if(player != null) {
               TankRespawnScheduler.cancelRespawn(player);
            }
         }

         this.autoEntryServices.battleRestarted(this);
      }
   }

   public int getTimeLeft() {
      if(this.endBattleTime == 0L) {
         this.endBattleTime = System.currentTimeMillis() + (long) (this.battleInfo.time * 1000);
      }
      return (int)((this.endBattleTime - System.currentTimeMillis()) / 1000L);
   }

   public void fire(BattlefieldPlayerController tank, String json) {
      this.sendToAllPlayers(tank, Type.BATTLE, new String[]{"fire", tank.tank.id, json});
   }

   public void startFire(BattlefieldPlayerController tank) {
      this.sendToAllPlayers(tank, Type.BATTLE, new String[]{"start_fire", tank.tank.id});
   }

   public void stopFire(BattlefieldPlayerController tank) {
      this.sendToAllPlayers(tank, Type.BATTLE, new String[]{"stop_fire", tank.tank.id});
   }

   public synchronized void onTakeBonus(BattlefieldPlayerController controller, String json) {
      try {
         JSONObject ex = (JSONObject)this.jsonParser.parse(json);
         JSONObject posObj = (JSONObject)ex.get("real_tank_position");
         String bonusId = (String)ex.get("bonus_id");
         Vector3 realPosTank = new Vector3((float)((Double)posObj.get("x")).doubleValue(), (float)((Double)posObj.get("y")).doubleValue(), (float)((Double)posObj.get("z")).doubleValue());
         Bonus bonus = (Bonus)this.activeBonuses.get(bonusId);
         if(bonus == null) {
            return;
         }

         if(this.bonusTakeModel.onTakeBonus(bonus, realPosTank, controller)) {
            this.sendToAllPlayers(Type.BATTLE, new String[]{"take_bonus_by", bonusId});
            this.activeBonuses.remove(bonusId);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void spawnBonus(Bonus bonus, int inc, int disappearingTime) {
      if(bonus.position.x != 0.0F || bonus.position.y != 0.0F || bonus.position.z != 0.0F) {
         String id = StringUtils.concatStrings(new String[]{bonus.type.toString(), "_", String.valueOf(inc)});
         this.activeBonuses.put(id, bonus);
         BonusesScheduler.runRemoveTask(this, id, (long)disappearingTime);
         this.sendToAllPlayers(Type.BATTLE, new String[]{"spawn_bonus", JSONUtils.parseBonusInfo(bonus, inc, disappearingTime)});
      }
   }

   public void parseBonus(BattlefieldPlayerController controller) {
      Iterator var3 = this.activeBonuses.values().iterator();
      Bonus item;
      while(var3.hasNext()) {
         item = (Bonus) var3.next();
         controller.send(Type.BATTLE, new String[]{"parse_bonus", JSONUtils.parseBonusInfo(item, item.id, item.dis)});
      }
   }

   public void parseBonus(SpectatorController controller) {
      Iterator var3 = this.activeBonuses.values().iterator();
      Bonus item;
      while(var3.hasNext()) {
         item = (Bonus) var3.next();
         controller.sendCommand(Type.BATTLE, new String[]{"parse_bonus", JSONUtils.parseBonusInfo(item, item.id, item.dis)});
      }
   }

   public void respawnPlayer(BattlefieldPlayerController controller, boolean kill) {
      if(!this.battleFinish) {
         controller.send(Type.BATTLE, new String[]{"local_user_killed"});
         this.battleMinesModel.playerDied(controller);
         if(kill) {
            controller.clearEffects();
            this.sendToAllPlayers(Type.BATTLE, new String[]{"kill_tank", controller.tank.id, "suicide"});
            controller.statistic.addDeaths();
            this.statistics.changeStatistic(controller);
            if(this.ctfModel != null && controller.flag != null) {
               this.ctfModel.dropFlag(controller, controller.tank.position);
            }
         }

         controller.tank.state = "suicide";
         TankRespawnScheduler.startRespawn(controller, false);
      }
   }

   public void kPlayer1(BattlefieldPlayerController controller, boolean kill) {
      if(!this.battleFinish) {
         controller.send(Type.BATTLE, new String[]{"local_user_killed"});
         this.battleMinesModel.playerDied(controller);
         if(kill) {
            controller.clearEffects();
            this.sendToAllPlayers(Type.BATTLE, new String[]{"kill_tank", controller.tank.id, "suicide"});
            //controller.statistic.addDeaths();
            //this.statistics.changeStatistic(controller);
            //if(this.ctfModel != null && controller.flag != null) {
               //this.ctfModel.dropFlag(controller, controller.tank.position);
            //}
            //this.sendToAllPlayers(Type.BATTLE, new String[]{"res_user", controller.tank.id});
         }
      }
   }

   public void kPlayer(BattlefieldPlayerController controller, boolean kill) {
      if(!this.battleFinish) {
         controller.send(Type.BATTLE, new String[]{"local_user_killed"});
         this.battleMinesModel.playerDied(controller);
         if(kill) {
            controller.clearEffects();
            this.sendToAllPlayers(Type.BATTLE, new String[]{"kill_tank1", controller.tank.id, "suicide"});
            //controller.statistic.addDeaths();
            //this.statistics.changeStatistic(controller);
            if(this.ctfModel != null && controller.flag != null) {
               this.ctfModel.dropFlag(controller, controller.tank.position);
            }
            this.sendToAllPlayers(Type.BATTLE, new String[]{"res_user", controller.tank.id});
         }
      }
   }

   public void rPlayer(BattlefieldPlayerController controller) {
      Vector3 pos = SpawnManager.getSpawnState(this.battleInfo.map, controller.playerTeamType);
      //this.setupTank(controller);
      this.sendToAllPlayers(Type.BATTLE, new String[]{"res1_user", JSONUtils.parseTankData(this, controller, controller.parentLobby.getLocalUser().getGarage(), pos, true, this.incration, controller.tank.id, controller.parentLobby.getLocalUser().getNickname(), controller.parentLobby.getLocalUser().getRang())});
      this.statistics.changeStatistic(controller);
      if(!this.battleFinish) {
         //controller.send(Type.BATTLE, new String[]{"local_user_killed"});
         controller.tank.state = "suicide";
         this.spawnPlayer(controller, pos);
      }
   }

   public void rPlayer(BattlefieldPlayerController controller,Vector3 pos) {
      this.setupTank(controller);
      this.sendToAllPlayers(Type.BATTLE, new String[]{"res1_user", JSONUtils.parseTankData(this, controller, controller.parentLobby.getLocalUser().getGarage(), pos, true, 0, controller.tank.id, controller.parentLobby.getLocalUser().getNickname(), controller.parentLobby.getLocalUser().getRang())});
      this.statistics.changeStatistic(controller);
      if(!this.battleFinish) {
         controller.tank.state = "suicide";
         this.spawnPlayer(controller, pos);
      }
   }

   public void moveTank(BattlefieldPlayerController controller) {
      String json = JSONUtils.parseMoveCommand(controller);
      this.sendToAllPlayers(Type.BATTLE, new String[]{"move", json});
   }

   public void spawnPlayer(BattlefieldPlayerController controller, Vector3 pos) {
      controller.per = 0F;
      if(!this.battleFinish) {
         TankRespawnScheduler.startRespawn(controller, false);
      }
   }

   public void setupTank(BattlefieldPlayerController controller) {
      controller.tank.id = controller.parentLobby.getLocalUser().getNickname();
   }

   public void addPlayer(BattlefieldPlayerController controller) {
      this.setupTank(controller);
      this.players.put(controller.tank.id, controller);
      ++this.incration;
      BattlesGC.cancelRemoving(this);
   }

   public void removeUser(BattlefieldPlayerController controller, boolean cache) {
      controller.clearEffects();
      this.battleMinesModel.playerDied(controller);
      try{
         this.players.remove(controller.parentLobby.getLocalUser().getNickname(), controller);
      }catch (NullPointerException e)
      {
         if(this.players == null) {
            RemoteDatabaseLogger.error("BattlefieldModel.removeUser() this.players is null");
         }
         if(controller.parentLobby.getLocalUser().getNickname() == null) {
            RemoteDatabaseLogger.error("BattlefieldModel.removeUser() controller.parentLobby.getLocalUser().getNickname() is null");
         }
         if(controller == null) {
            RemoteDatabaseLogger.error("BattlefieldModel.removeUser() controller is null");
         }
      }
      if(!cache) {
         if(!this.battleInfo.team) {
            --BattlesList.getBattleInfoById(this.battleInfo.battleId).countPeople;
         } else if(controller.playerTeamType.equals("RED")) {
            --BattlesList.getBattleInfoById(this.battleInfo.battleId).redPeople;
         } else {
            --BattlesList.getBattleInfoById(this.battleInfo.battleId).bluePeople;
         }
      }

      if(this.ctfModel != null && controller.flag != null) {
         this.ctfModel.dropFlag(controller, controller.tank.position);
      }

      this.sendToAllPlayers(Type.BATTLE, new String[]{"remove_user", controller.tank.id});
      if(this.players.size() == 0) {
         BattlesGC.addBattleForRemove(this);
      }

   }

   public void initLocalTank(BattlefieldPlayerController controller) {
      controller.userInited = true;
      Vector3 pos = SpawnManager.getSpawnState(this.battleInfo.map, controller.playerTeamType);
      if(this.battleInfo.battleType.equals("CTF")) {
         controller.send(Type.BATTLE, new String[]{"init_ctf_model", JSONUtils.parseCTFModelData(this)});
      }
      this.sendAllTanks(controller);
      this.parseBonus(controller);
      //this.parseSpawnBonus(controller);
      controller.inventory.init();
      this.battleMinesModel.initModel(controller);
      this.battleMinesModel.sendMines(controller);
      this.sendToAllPlayers(Type.BATTLE, new String[]{"init_tank", JSONUtils.parseTankData(this, controller, controller.parentLobby.getLocalUser().getGarage(), pos, true, this.incration, controller.tank.id, controller.parentLobby.getLocalUser().getNickname(), controller.parentLobby.getLocalUser().getRang())});
      this.statistics.changeStatistic(controller);
      this.effectsModel.sendInitData(controller);
      this.spawnPlayer(controller, pos);
   }

   private Vector3 getRandomSpawnPostiton(final BonusRegion region) {
      final Vector3 f = new Vector3(0.0f, 0.0f, 0.0f);
      final Random rand = new Random();
      f.x = region.min.x + (region.max.x - region.min.x) * rand.nextFloat();
      f.y = region.min.y + (region.max.y - region.min.y) * rand.nextFloat();
      f.z = region.max.z;
      return f;
   }

   public void parseSpawnBonus(BattlefieldPlayerController controller) {
      for(int v = 0;v<this.battleInfo.map.crystallsRegions.size();v++)
      {
         controller.send(Type.BATTLE, new String[]{"z", JSONUtils.parseZoneInfo(getRandomSpawnPostiton(this.battleInfo.map.crystallsRegions.get(v)),"cry")});
      }
      for(int v = 0;v<this.battleInfo.map.armorsRegions.size();v++)
      {
         controller.send(Type.BATTLE, new String[]{"z", JSONUtils.parseZoneInfo(getRandomSpawnPostiton(this.battleInfo.map.armorsRegions.get(v)),"arm")});
      }
      for(int v = 0;v<this.battleInfo.map.damagesRegions.size();v++)
      {
         controller.send(Type.BATTLE, new String[]{"z", JSONUtils.parseZoneInfo(getRandomSpawnPostiton(this.battleInfo.map.damagesRegions.get(v)),"dam")});
      }
      for(int v = 0;v<this.battleInfo.map.healthsRegions.size();v++)
      {
         controller.send(Type.BATTLE, new String[]{"z", JSONUtils.parseZoneInfo(getRandomSpawnPostiton(this.battleInfo.map.healthsRegions.get(v)),"hea")});
      }
      for(int v = 0;v<this.battleInfo.map.nitrosRegions.size();v++)
      {
         controller.send(Type.BATTLE, new String[]{"z", JSONUtils.parseZoneInfo(getRandomSpawnPostiton(this.battleInfo.map.nitrosRegions.get(v)),"nit")});
      }
   }

   public void sendUserLogMessage(String idUser, String message) {
      this.sendToAllPlayers(Type.BATTLE, new String[]{"user_log", idUser, message});
   }

   public void sendAllTanks(BattlefieldPlayerController controller) {
      Iterator var3 = this.players.values().iterator();

      while(var3.hasNext()) {
         BattlefieldPlayerController player = (BattlefieldPlayerController)var3.next();
         if(player != controller && player.userInited) {
            controller.send(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, this.incration, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
         }
      }
      controller.send(Type.BATTLE, new String[]{"init_gui_model", JSONUtils.parseBattleData(this)});
      Iterator var4 = this.players.values().iterator();
      while(var4.hasNext()) {
         BattlefieldPlayerController player = (BattlefieldPlayerController)var4.next();
         if(player != controller && player.userInited) {
            this.statistics.changeStatistic(player);
         }
      }

   }

   public void sendAllTanks(SpectatorController controller) {
      Iterator var3 = this.players.values().iterator();

      while(var3.hasNext()) {
         BattlefieldPlayerController player = (BattlefieldPlayerController)var3.next();
         if(player.userInited) {
            controller.sendCommand(Type.BATTLE, "init_tank", JSONUtils.parseTankData(this, player, player.parentLobby.getLocalUser().getGarage(), player.tank.position, false, this.incration, player.tank.id, player.parentLobby.getLocalUser().getNickname(), player.parentLobby.getLocalUser().getRang()));
         }
      }
      Iterator var4 = this.players.values().iterator();
      while(var4.hasNext()) {
         BattlefieldPlayerController player = (BattlefieldPlayerController)var4.next();
         if(player.userInited) {
            this.statistics.changeStatistic(player);
         }
      }

   }

   public void activateTank(BattlefieldPlayerController player) {
      player.tank.state = "active";
      this.sendToAllPlayers(Type.BATTLE, new String[]{"activate_tank", player.tank.id});
   }

   public BattlefieldPlayerController getPlayer(String id) {
      return (BattlefieldPlayerController)this.players.get(id);
   }

   public void sendToAllPlayers(Type type, String... args) {
      if(this.players != null) {
         if(this.players.size() != 0) {
            Iterator var4 = this.players.values().iterator();

            while(var4.hasNext()) {
               BattlefieldPlayerController player = (BattlefieldPlayerController)var4.next();
               if(player.userInited) {
                  player.send(type, args);
               }
            }
         }

         this.spectatorModel.sendCommandToSpectators(type, args);
      }
   }

   public void sendToComand(String other, Type type, String... args) {
      if(this.players.size() != 0) {
         Iterator var5 = this.players.values().iterator();

         while(var5.hasNext()) {
            BattlefieldPlayerController player = (BattlefieldPlayerController)var5.next();
            if(player.userInited && player.playerTeamType != other) {
               player.send(type, args);
            }
         }
      }

      this.spectatorModel.sendCommandToSpectators(type, args);
   }

   public void sendToAllPlayers(BattlefieldPlayerController other, Type type, String... args) {
      if(this.players.size() != 0) {
         Iterator var5 = this.players.values().iterator();

         while(var5.hasNext()) {
            BattlefieldPlayerController player = (BattlefieldPlayerController)var5.next();
            if(player.userInited && player != other) {
               player.send(type, args);
            }
         }
      }

      this.spectatorModel.sendCommandToSpectators(type, args);
   }

   public void cheatDetected(BattlefieldPlayerController player, Class anticheat) {
      AnticheatModel[] model = (AnticheatModel[])anticheat.getAnnotationsByType(AnticheatModel.class);
      if(model != null && model.length >= 1) {
         Logger.log("Detected cheater![" + model[0].name() + "] " + player.getUser().getNickname() + " " + player.parentLobby.networker.getIP());
      }

      this.kickPlayer(player);
   }

   public void kickPlayer(BattlefieldPlayerController player) {
      player.send(Type.BATTLE, new String[]{"kick_for_cheats"});
      player.parentLobby.networker.closeConnection();
   }

   public void setTank(BattlefieldPlayerController player, Tank newTank) {
      ((BattlefieldPlayerController)this.players.get(player.parentLobby.getLocalUser().getNickname())).tank = newTank;
   }

   public void destroy() {
      this.players.clear();
      this.activeBonuses.clear();
      this.quartzService.deleteJob(this.QUARTZ_NAME, QUARTZ_GROUP);
      this.tanksKillModel.destroy();
      this.tanksKillModel = null;
      this.players = null;
      this.activeBonuses = null;
      this.battleInfo = null;
      this.spectatorModel = null;
   }
}
