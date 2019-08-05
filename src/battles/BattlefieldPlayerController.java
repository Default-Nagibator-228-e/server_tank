package battles;

import utils.StringUtils;
import battles.tanks.loaders.Prop;
import lobby.battles.BattleInfo;
import json.JSONUtils;
import main.database.impl.DatabaseManagerImpl;
import users.locations.UserLocation;
import battles.effects.Effect;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import logger.Logger;
import logger.Type;
import commands.Command;
import users.garage.Garage;
import users.User;
import battles.tanks.colormaps.ColormapsFactory;
import battles.tanks.loaders.WeaponsFactory;
import battles.tanks.loaders.HullsFactory;
import battles.tanks.math.Vector3;
import services.AutoEntryServices;
import services.annotations.ServicesInject;
import services.LobbysServices;
import battles.inventory.InventoryController;
import battles.ctf.flags.FlagServer;
import battles.tanks.statistic.PlayerStatistic;
import battles.tanks.Tank;
import lobby.LobbyManager;
import network.listeners.IDisconnectListener;
import battles.comands.BattlefieldPlayerComandsConst;

import java.util.Iterator;

public class BattlefieldPlayerController extends BattlefieldPlayerComandsConst implements IDisconnectListener, Comparable<BattlefieldPlayerController>
{
   public LobbyManager parentLobby;
   public BattlefieldModel battle;
   public Tank tank;
   public PlayerStatistic statistic;
   public String playerTeamType;
   public FlagServer flag;
   public InventoryController inventory;
   public long timer;
   public long lastFireTime;
   public boolean userInited;
   @ServicesInject(target = LobbysServices.class)
   private LobbysServices lobbysServices;
   @ServicesInject(target = AutoEntryServices.class)
   private AutoEntryServices autoEntryServices;
   public float per = 0F;

   public BattlefieldPlayerController(final LobbyManager parent, final BattlefieldModel battle, final String playerTeamType) {
      this.userInited = false;
      this.lobbysServices = LobbysServices.getInstance();
      this.autoEntryServices = AutoEntryServices.instance();
      this.parentLobby = parent;
      this.battle = battle;
      this.playerTeamType = playerTeamType;
      (this.tank = new Tank(null)).setHull(HullsFactory.getHull(this.getGarage().mountHull));
      this.tank.setWeapon(WeaponsFactory.getWeapon(this.getGarage().mountTurret, this, battle));
      this.tank.setColormap(ColormapsFactory.getColormap(this.getGarage().mountColormap));
      this.statistic = new PlayerStatistic(0, 0, 0,parent.getLocalUser());
      this.inventory = new InventoryController(this,this.battle.battleInfo.isPaid);
      battle.addPlayer(this);
      this.sendShotsData();
   }

   public User getUser() {
      return this.parentLobby.getLocalUser();
   }

   public Garage getGarage() {
      return this.parentLobby.getLocalUser().getGarage();
   }

   public void executeCommand(final Command cmd) {
      try {
         switch (cmd.type) {
            case BATTLE:
               switch (cmd.args[0]) {
                   case "move":
                       try {
                           String[] temp = cmd.args[1].split("@");
                           this.tank.position = new Vector3(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]), Float.parseFloat(temp[2]));
                           this.tank.orientation = new Vector3(Float.parseFloat(temp[3]), Float.parseFloat(temp[4]), Float.parseFloat(temp[5]));
                           this.tank.linVel = new Vector3(Float.parseFloat(temp[6]), Float.parseFloat(temp[7]), Float.parseFloat(temp[8]));
                           this.tank.angVel = new Vector3(Float.parseFloat(temp[9]), Float.parseFloat(temp[10]), Float.parseFloat(temp[11]));
                           this.tank.turretDir = Float.parseFloat(cmd.args[2]);
                           this.tank.controllBits = Integer.parseInt(cmd.args[3]);
                           this.battle.moveTank(this);
                       }
                       catch (Exception ex) {
                           ex.printStackTrace();
                       }
                       break;
                   case "attempt_to_take_bonus":
                       this.battle.onTakeBonus(this, cmd.args[1]);
                       break;
                   case "start_fire":
                       if (this.tank.state.equals("active")) {
                           this.tank.getWeapon().startFire((cmd.args.length >= 2) ? cmd.args[1] : "");
                       }
                       break;
                   case "fire":
                       if (this.tank.state.equals("active")) {
                           this.tank.getWeapon().fire(cmd.args[1]);
                       }
                       break;
                   case "stop_fire":
                       this.tank.getWeapon().stopFire();
                       break;
                   case "activate_item":
                       try {
                           this.inventory.activateItem(cmd.args[1], new Vector3(Float.parseFloat(cmd.args[2]), Float.parseFloat(cmd.args[3]), Float.parseFloat(cmd.args[4])));
                       } catch (Exception ex2) {
                           this.inventory.activateItem(cmd.args[1], new Vector3(0.0f, 0.0f, 0.0f));
                       }
                       break;
                  case "get_init_data_local_tank":
                     this.battle.initLocalTank(this);
                     break;
                  case "activate_tank":
                     this.battle.activateTank(this);
                     break;
                  case "suicide":
                     this.battle.respawnPlayer(this, true);
                     break;
                  case "physics":
                     if (parentLobby.getLocalUser().getType().toString() == "admin") {
                        Prop var = DatabaseManagerImpl.instance().getPrByName(parentLobby.getLocalUser().getGarage().mountTurret.id);
                        Prop var1 = DatabaseManagerImpl.instance().getPrByName(parentLobby.getLocalUser().getGarage().mountHull.id);
                        JSONObject jobjt = new JSONObject();
                        JSONObject jobjh = new JSONObject();
                        JSONArray uh = new JSONArray();
                        JSONArray ut = new JSONArray();
                        jobjt.put("type", parentLobby.getLocalUser().getGarage().mountTurret.id);
                        Iterator var4 = ((JSONArray) (new JSONParser()).parse(cmd.args[1])).iterator();
                        int va = 0;
                        while (var4.hasNext()) {
                           JSONObject jt = (JSONObject) var4.next();
                           jt.remove("modification");
                           jt.put("modification", "m" + va);
                           va++;
                           ut.add(jt);
                        }
                        jobjt.put("params", ut);
                        jobjh.put("type", parentLobby.getLocalUser().getGarage().mountHull.id);
                        Iterator var5 = ((JSONArray) (new JSONParser()).parse(cmd.args[2])).iterator();
                        int va1 = 0;
                        while (var5.hasNext()) {
                           JSONObject jt = (JSONObject) var5.next();
                           jt.remove("modification");
                           jt.put("modification", "m" + va1);
                           va1++;
                           uh.add(jt);
                        }
                        jobjh.put("modifications", uh);
                        var.set_val(jobjt.toString());
                        var1.set_val(jobjh.toString());
                        ///System.out.println(cmd.args[2]);
                        WeaponsFactory.init(ut.toString(), parentLobby.getLocalUser().getGarage().mountTurret.id);
                        HullsFactory.init(uh.toString(), parentLobby.getLocalUser().getGarage().mountHull.id);
                        DatabaseManagerImpl.instance().update(var);
                        DatabaseManagerImpl.instance().update(var1);
                        Vector3 pos = this.tank.position;
                        this.battle.kPlayer(this, true);
                        Tank var2;
                        (var2 = new Tank(null)).setHull(HullsFactory.getHull(this.getGarage().mountHull));
                        var2.setWeapon(WeaponsFactory.getWeapon(this.getGarage().mountTurret, this, battle));
                        var2.setColormap(ColormapsFactory.getColormap(this.getGarage().mountColormap));
                        this.battle.setTank(this, var2);
                        this.sendShotsData();
                        this.battle.rPlayer(this, pos);
                        this.tank = var2;
                     }
                     break;
                  case "del" :
                     this.tank.state = "suicide";
                     break;
                  case "suicide1" :
                     this.battle.kPlayer(this, true);
                     this.tank.setHull(HullsFactory.getHull(this.getGarage().mountHull));
                     this.tank.setWeapon(WeaponsFactory.getWeapon(this.getGarage().mountTurret, this, battle));
                     this.tank.setColormap(ColormapsFactory.getColormap(this.getGarage().mountColormap));
                     this.sendShotsData();
                     this.battle.rPlayer(this);
                     this.inventory.init();
                     break;
                  case "chat":
                     this.battle.chatModel.onMessage(this, cmd.args[1], Boolean.valueOf(cmd.args[2]));
                     break;
                  case "i_exit_from_battle":
                     this.parentLobby.onExitFromBattle();
                     break;
                  case "exit_from_statistic":
                     this.parentLobby.onExitFromStatistic();
                     break;
                  case "attempt_to_take_flag":
                     this.battle.ctfModel.attemptToTakeFlag(this, cmd.args[1]);
                     break;
                  case "flag_drop":
                     this.parseAndDropFlag(cmd.args[1]);
                     break;
                  case "speedhack_detected":
                     this.battle.cheatDetected(this, this.getClass());
                     break;
                  case "mine_hit":
                     this.battle.battleMinesModel.hitMine(this, cmd.args[1]);
                     break;
                  case "check_md5_map":
                     this.battle.mapChecksumModel.check(this, cmd.args[1]);
               }
               break;
            case GARAGE:
            case PING:
               break;
            default:
               Logger.log(Type.ERROR, "User " + this.parentLobby.getLocalUser().getNickname() + "[" + this.parentLobby.networker.toString() + "] send unknowed request!");
         }
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void parseAndDropFlag(final String json) {
      try {
         final JSONObject _json = (JSONObject)new JSONParser().parse(json);
         this.battle.ctfModel.dropFlag(this, new Vector3(Float.parseFloat(_json.get("x")+""), Float.parseFloat(_json.get("y")+""), Float.parseFloat(_json.get("z")+"")));
      }
      catch (ParseException e) {
         e.printStackTrace();
      }
   }

   public void sendShotsData() {
      this.send(commands.Type.BATTLE, "init_shots_data", WeaponsFactory.getJSONList());
   }

   public void clearEffects() {
      while (this.tank.activeEffects.size() > 0) {
         ((Effect)this.tank.activeEffects.get(0)).deactivate();
      }
   }

   public void toggleTeamType() {
      if (this.playerTeamType.equals("NONE")) {
         return;
      }
      if (this.playerTeamType.equals("BLUE")) {
         this.playerTeamType = "RED";
         final BattleInfo battleInfo = this.battle.battleInfo;
         ++battleInfo.redPeople;
         final BattleInfo battleInfo2 = this.battle.battleInfo;
         --battleInfo2.bluePeople;
      }
      else {
         this.playerTeamType = "BLUE";
         final BattleInfo battleInfo3 = this.battle.battleInfo;
         --battleInfo3.redPeople;
         final BattleInfo battleInfo4 = this.battle.battleInfo;
         ++battleInfo4.bluePeople;
      }
      this.lobbysServices.sendCommandToAllUsers(commands.Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "update_count_users_in_team_battle", JSONUtils.parseUpdateCoundPeoplesCommand(this.battle.battleInfo) });
      this.battle.sendToAllPlayers(commands.Type.BATTLE, new String[] { "change_user_team", this.tank.id, this.playerTeamType });
   }

   public void destroy(final boolean cache) {
      this.battle.removeUser(this, cache);
      if (!cache) {
         this.lobbysServices.sendCommandToAllUsers(commands.Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "remove_player_from_battle", JSONUtils.parseRemovePlayerComand(this) });
         if (!this.battle.battleInfo.team) {
            this.lobbysServices.sendCommandToAllUsers(commands.Type.LOBBY, UserLocation.BATTLESELECT, new String[] { StringUtils.concatStrings(new String[] { "update_count_users_in_dm_battle", ";", this.battle.battleInfo.battleId, ";", String.valueOf(this.battle.players.size()) }) });
         }
         else {
            this.lobbysServices.sendCommandToAllUsers(commands.Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "update_count_users_in_team_battle", JSONUtils.parseUpdateCoundPeoplesCommand(this.battle.battleInfo) });
         }
      }
      this.parentLobby = null;
      this.battle = null;
      this.tank = null;
   }

   public void send(final commands.Type type, final String... args) {
      if (this.parentLobby != null) {
         this.parentLobby.send(type, args);
      }
   }

   public void onDisconnect() {
      this.autoEntryServices.userExit(this);
      this.destroy(true);
   }

   public int compareTo(final BattlefieldPlayerController o) {
      return (int)(o.statistic.getScore() - this.statistic.getScore());
   }
}