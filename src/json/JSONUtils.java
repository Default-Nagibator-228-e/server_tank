package json;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.bonuses.Bonus;
import battles.chat.BattleChatMessage;
import battles.ctf.CTFModel;
import battles.ctf.flags.FlagServer;
import battles.maps.Map;
import battles.maps.MapsLoader;
import battles.mines.ServerMine;
import battles.tanks.Tank;
import battles.tanks.math.Vector3;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.WeaponWeakeningData;
import battles.tanks.weapons.flamethrower.FlamethrowerEntity;
import battles.tanks.weapons.frezee.FrezeeEntity;
import battles.tanks.weapons.isida.IsidaEntity;
import battles.tanks.weapons.railgun.RailgunEntity;
import battles.tanks.weapons.ricochet.RicochetEntity;
import battles.tanks.weapons.annihilat.AnnihilatorEntity;
import battles.tanks.weapons.thunder.ThunderEntity;
import battles.tanks.weapons.twins.TwinsEntity;
import collections.FastHashMap;
import commands.Type;
import lobby.LobbyManager;
import lobby.battles.BattleInfo;
import lobby.battles.BattlesList;
import lobby.chat.ChatMessage;
import lobby.top.HallOfFame;
import main.Main;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import main.params.OnlineStats;
import users.garage.items.kit.Kit;
import users.garage.items.kit.KitItem;
import users.garage.items.kit.loader.KitsLoader;
import services.AutoEntryServices;
import services.annotations.ServicesInject;
import users.TypeUser;
import users.User;
import users.friends.Friends;
import users.garage.Garage;
import users.garage.GarageItemsLoader;
import users.garage.enums.ItemType;
import users.garage.items.Item;
import users.garage.items.PropertyItem;
import users.garage.items.modification.ModificationInfo;
import java.lang.reflect.Field;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {
   @ServicesInject(
      target = AutoEntryServices.class
   )
   private static AutoEntryServices autoEntryServices = AutoEntryServices.instance();
   @ServicesInject(
      target = AutoEntryServices.class
   )
   private static DatabaseManager databaseManager = DatabaseManagerImpl.instance();

   public static String parseConfiguratorEntity(Object entity, Class clazz) {
      JSONObject jobj = new JSONObject();

      try {
         Field[] var6;
         int var5 = (var6 = clazz.getDeclaredFields()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            Field e = var6[var4];
            e.setAccessible(true);
            jobj.put(e.getName(), e.get(entity));
         }
      } catch (IllegalAccessException | IllegalArgumentException var7) {
         var7.printStackTrace();
      }

      return jobj.toJSONString();
   }

   public static String parseInitMinesComand(FastHashMap mines) {
      JSONObject jobj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var4 = mines.values().iterator();

      while(var4.hasNext()) {
         ArrayList userMines = (ArrayList)var4.next();
         Iterator var6 = userMines.iterator();

         while(var6.hasNext()) {
            ServerMine mine = (ServerMine)var6.next();
            JSONObject _mine = new JSONObject();
            _mine.put("ownerId", mine.getOwner().tank.id);
            _mine.put("mineId", mine.getId());
            _mine.put("x", Float.valueOf(mine.getPosition().x));
            _mine.put("y", Float.valueOf(mine.getPosition().y));
            _mine.put("z", Float.valueOf(mine.getPosition().z));
            array.add(_mine);
         }
      }

      jobj.put("mines", array);
      return jobj.toJSONString();
   }

   public static String parsePutMineComand(ServerMine mine) {
      JSONObject jobj = new JSONObject();
      jobj.put("mineId", mine.getId());
      jobj.put("userId", mine.getOwner().tank.id);
      jobj.put("x", Float.valueOf(mine.getPosition().x));
      jobj.put("y", Float.valueOf(mine.getPosition().y));
      jobj.put("z", Float.valueOf(mine.getPosition().z));
      return jobj.toJSONString();
   }

   public static String parseInitInventoryComand(Garage garage,Boolean d) {
      JSONObject jobj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var4 = garage.getInventoryItems().iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         JSONObject io = new JSONObject();
         io.put("id", item.id);
         io.put("count", d ? 0:Integer.valueOf(item.count));
         io.put("slotId", Integer.valueOf(item.index));
         io.put("itemEffectTime", Integer.valueOf(45));
         io.put("itemRestSec", Integer.valueOf(10));
         array.add(io);
      }

      jobj.put("items", array);
      return jobj.toJSONString();
   }

   public static String parseInv(String g,String g1,String g2) {
      JSONObject jobj = new JSONObject();
      jobj.put("n", g);
      jobj.put("r", g1);
      jobj.put("id", g2);
      return jobj.toJSONString();
   }

   public static String parseRemovePlayerComand(BattlefieldPlayerController player) {
      JSONObject jobj = new JSONObject();
      jobj.put("battleId", player.battle.battleInfo.battleId);
      jobj.put("id", player.getUser().getNickname());
      return jobj.toJSONString();
   }

   public static String parseRemovePlayerComand(String userId, String battleid) {
      JSONObject jobj = new JSONObject();
      jobj.put("battleId", battleid);
      jobj.put("id", userId);
      return jobj.toJSONString();
   }

   public static String parseAddPlayerComand(BattlefieldPlayerController player, BattleInfo battleInfo) {
      JSONObject obj = new JSONObject();
      obj.put("battleId", battleInfo.battleId);
      obj.put("id", player.getUser().getNickname());
      obj.put("kills", Long.valueOf(player.statistic.getScore()));
      obj.put("name", player.getUser().getNickname());
      obj.put("rank", Integer.valueOf(player.getUser().getRang() + 1));
      obj.put("type", player.playerTeamType);
      return obj.toJSONString();
   }

   public static String parseDropFlagCommand(FlagServer flag) {
      JSONObject obj = new JSONObject();
      obj.put("x", Float.valueOf(flag.position.x));
      obj.put("y", Float.valueOf(flag.position.y));
      obj.put("z", Float.valueOf(flag.position.z));
      obj.put("flagTeam", flag.flagTeamType);
      return obj.toJSONString();
   }

   public static String parseCTFModelData(BattlefieldModel model) {
      JSONObject obj = new JSONObject();
      CTFModel ctfModel = model.ctfModel;
      JSONObject basePosBlue = new JSONObject();
      basePosBlue.put("x", Float.valueOf(model.battleInfo.map.flagBluePosition.x));
      basePosBlue.put("y", Float.valueOf(model.battleInfo.map.flagBluePosition.y));
      basePosBlue.put("z", Float.valueOf(model.battleInfo.map.flagBluePosition.z));
      JSONObject basePosRed = new JSONObject();
      basePosRed.put("x", Float.valueOf(model.battleInfo.map.flagRedPosition.x));
      basePosRed.put("y", Float.valueOf(model.battleInfo.map.flagRedPosition.y));
      basePosRed.put("z", Float.valueOf(model.battleInfo.map.flagRedPosition.z));
      JSONObject posBlue = new JSONObject();
      posBlue.put("x", Float.valueOf(ctfModel.getBlueFlag().position.x));
      posBlue.put("y", Float.valueOf(ctfModel.getBlueFlag().position.y));
      posBlue.put("z", Float.valueOf(ctfModel.getBlueFlag().position.z));
      JSONObject posRed = new JSONObject();
      posRed.put("x", Float.valueOf(ctfModel.getRedFlag().position.x));
      posRed.put("y", Float.valueOf(ctfModel.getRedFlag().position.y));
      posRed.put("z", Float.valueOf(ctfModel.getRedFlag().position.z));
      obj.put("basePosBlueFlag", basePosBlue);
      obj.put("basePosRedFlag", basePosRed);
      obj.put("posBlueFlag", posBlue);
      obj.put("posRedFlag", posRed);
      obj.put("blueFlagCarrierId", ctfModel.getBlueFlag().owner == null?null:ctfModel.getBlueFlag().owner.tank.id);
      obj.put("redFlagCarrierId", ctfModel.getRedFlag().owner == null?null:ctfModel.getRedFlag().owner.tank.id);
      return obj.toJSONString();
   }

   public static String parseUpdateCoundPeoplesCommand(BattleInfo battle) {
      JSONObject obj = new JSONObject();
      obj.put("battleId", battle.battleId);
      obj.put("redPeople", Integer.valueOf(battle.redPeople));
      obj.put("bluePeople", Integer.valueOf(battle.bluePeople));
      return obj.toJSONString();
   }

   public static String parseFishishBattle(FastHashMap players, int timeToRestart) {
      JSONObject obj = new JSONObject();
      JSONArray users = new JSONArray();
      obj.put("time_to_restart", Integer.valueOf(timeToRestart));
      if(players == null) {
         return obj.toString();
      } else {
         Iterator var5 = players.values().iterator();

         while(var5.hasNext()) {
            BattlefieldPlayerController bpc = (BattlefieldPlayerController)var5.next();
            JSONObject stat = new JSONObject();
            stat.put("kills", Long.valueOf(bpc.statistic.getKills()));
            stat.put("deaths", Integer.valueOf(bpc.statistic.getDeaths()));
            stat.put("id", bpc.getUser().getNickname());
            stat.put("rank", Integer.valueOf(bpc.getUser().getRang() + 1));
            stat.put("prize", Integer.valueOf(bpc.statistic.getPrize()));
            if(Main.ych)
            {
               stat.put("zv", Integer.valueOf(bpc.statistic.gz()));
            }
            stat.put("team_type", bpc.playerTeamType);
            stat.put("score", Long.valueOf(bpc.statistic.getScore()));
            users.add(stat);
         }

         obj.put("users", users);
         return obj.toString();
      }
   }

   public static String parsePlayerStatistic(BattlefieldPlayerController player) {
      JSONObject obj = new JSONObject();
      obj.put("kills", Long.valueOf(player.statistic.getKills()));
      obj.put("deaths", Integer.valueOf(player.statistic.getDeaths()));
      obj.put("id", player.getUser().getNickname());
      obj.put("rank", Integer.valueOf(player.getUser().getRang() + 1));
      obj.put("team_type", player.playerTeamType);
      obj.put("score", Long.valueOf(player.statistic.getScore()));
      return obj.toString();
   }

   public static String parseSpawnCommand(BattlefieldPlayerController bpc, Vector3 pos) {
      JSONObject obj = new JSONObject();
      if(bpc != null && bpc.tank != null) {
         obj.put("tank_id", bpc.tank.id);
         obj.put("health", Integer.valueOf(bpc.tank.health));
         obj.put("speed", Float.valueOf(bpc.tank.speed));
         obj.put("turn_speed", Float.valueOf(bpc.tank.turnSpeed));
         obj.put("turret_rotation_speed", Float.valueOf(bpc.tank.turretRotationSpeed));
         obj.put("incration_id", Integer.valueOf(bpc.battle.incration));
         obj.put("team_type", bpc.playerTeamType);
         obj.put("x", Float.valueOf(pos.x));
         obj.put("y", Float.valueOf(pos.y));
         obj.put("z", Float.valueOf(pos.z));
         obj.put("rot", Double.valueOf(pos.rot));
         return obj.toString();
      } else {
         return null;
      }
   }

   public static String parseBattleData(BattlefieldModel model) {
      JSONObject obj = new JSONObject();
      JSONArray users = new JSONArray();
      obj.put("name", model.battleInfo.name);
      obj.put("fund", Double.valueOf(model.tanksKillModel.getBattleFund()));
      obj.put("scoreLimit", Integer.valueOf(model.battleInfo.battleType.equals("CTF")?model.battleInfo.numFlags:model.battleInfo.numKills));
      obj.put("timeLimit", Integer.valueOf(model.battleInfo.time));
      obj.put("currTime", Integer.valueOf(model.getTimeLeft()));
      obj.put("score_red", Integer.valueOf(model.battleInfo.scoreRed));
      obj.put("score_blue", Integer.valueOf(model.battleInfo.scoreBlue));
      obj.put("team", Boolean.valueOf(model.battleInfo.team));
      Iterator var4 = model.players.values().iterator();

      while(var4.hasNext()) {
         BattlefieldPlayerController bpc = (BattlefieldPlayerController)var4.next();
         JSONObject usr = new JSONObject();
         usr.put("nickname", bpc.parentLobby.getLocalUser().getNickname());
         usr.put("rank", Integer.valueOf(bpc.parentLobby.getLocalUser().getRang() + 1));
         usr.put("teamType", bpc.playerTeamType);
         users.add(usr);
      }

      obj.put("users", users);
      return obj.toJSONString();
   }

   public static String parseUserToJSON(User user) {
      JSONObject obj = new JSONObject();
      obj.put("name", user.getNickname());
      obj.put("crystall", Integer.valueOf(user.getCrystall()));
      obj.put("email", user.getEmail());
      obj.put("tester", Boolean.valueOf(user.getType() != TypeUser.DEFAULT));
      obj.put("next_score", Integer.valueOf(user.getNextScore()));
      obj.put("place", Integer.valueOf(user.getPlace()));
      obj.put("rang", Integer.valueOf(user.getRang() + 1));
      obj.put("rating", Integer.valueOf(user.getRating()));
      obj.put("score", Integer.valueOf(user.getScore()));
      return obj.toJSONString();
   }

   public static JSONObject parseUserToJSONObject(User user) {
      JSONObject obj = new JSONObject();
      obj.put("name", user.getNickname());
      obj.put("crystall", Integer.valueOf(user.getCrystall()));
      obj.put("email", user.getEmail());
      obj.put("tester", Boolean.valueOf(user.getType() != TypeUser.DEFAULT));
      obj.put("next_score", Integer.valueOf(user.getNextScore()));
      obj.put("place", Integer.valueOf(user.getPlace()));
      obj.put("rang", Integer.valueOf(user.getRang() + 1));
      obj.put("rating", Integer.valueOf(user.getRating()));
      obj.put("score", Integer.valueOf(user.getScore()));
      return obj;
   }

   public static String parseHallOfFame(HallOfFame top) {
      JSONObject obj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var4 = top.getData().iterator();

      while(var4.hasNext()) {
         User user = (User)var4.next();
         array.add(parseUserToJSONObject(user));
      }

      obj.put("users_data", array);
      return obj.toJSONString();
   }

   public static String parseChatLobbyMessage(ChatMessage msg) {
      JSONObject obj = new JSONObject();
      obj.put("name", msg.user.getNickname());
      obj.put("rang", Integer.valueOf(msg.user.getRang() + 1));
      obj.put("message", msg.message);
      obj.put("addressed", Boolean.valueOf(msg.addressed));
      obj.put("nameTo", msg.userTo == null?"NULL":msg.userTo.getNickname());
      obj.put("rangTo", Integer.valueOf(msg.userTo == null?0:msg.userTo.getRang() + 1));
      obj.put("system", Boolean.valueOf(msg.system));
      obj.put("yellow", Boolean.valueOf(msg.yellowMessage));
      return obj.toJSONString();
   }

   public static String parseFriend(Friends fr) {
      JSONObject obj = new JSONObject();
      obj.put("d", fr.getDr());
      obj.put("i", fr.getInn());
      obj.put("o", fr.getOutt());
      String var = "";
      Vector<String> d = fr.getDrV();
      Vector<String> ccd = new Vector<>();
      for (int f=0;f<d.size(); f++)
      {
         int cd = OnlineStats.inOnline(d.get(f));
         if(!var.isEmpty()) {
            var += "э" + cd;
         }else{
            var += cd;
         }
         if(cd == 1) {
            ccd.add(f + "");
         }
      }
      obj.put("l", var);
      String ids = "";
      for (int f=0;f<ccd.size(); f++)
      {
         String ni = fr.getDrV().get(Integer.parseInt(ccd.get(f)));
         String id = "";
         try {
            id = DatabaseManagerImpl.instance().getUserById(ni).idbat;
            if(!id.isEmpty())
            {
               id = "#battle" + id;
               System.out.println(id);
            }else{
               id = "";
            }
         } catch(NullPointerException v){

         }
         if(ids.isEmpty())
         {
            ids += id;
         }else{
            ids += "э" + id;
         }
      }
      obj.put("b", ids);
      return obj.toJSONString();
   }

   public static String parseR(int fr, String id, String type) {
      JSONObject obj = new JSONObject();
      obj.put("id", id);
      obj.put("r", fr);
      obj.put("type", type);
      return obj.toJSONString();
   }

   public static String parseV(int fr) {
      JSONObject obj = new JSONObject();
      obj.put("h", fr);
      return obj.toJSONString();
   }

   public static JSONObject parseChatLobbyMessageObject(ChatMessage msg) {
      JSONObject obj = new JSONObject();
      obj.put("name", msg.user == null?"":msg.user.getNickname());
      obj.put("rang", Integer.valueOf(msg.user == null?0:msg.user.getRang() + 1));
      obj.put("message", msg.message);
      obj.put("addressed", Boolean.valueOf(msg.addressed));
      obj.put("nameTo", msg.userTo == null?"":msg.userTo.getNickname());
      obj.put("rangTo", Integer.valueOf(msg.userTo == null?0:msg.userTo.getRang() + 1));
      obj.put("system", Boolean.valueOf(msg.system));
      obj.put("yellow", Boolean.valueOf(msg.yellowMessage));
      return obj;
   }

   public static String parseMod(int prog, String tth, String rtth, String tth1, int cost) {
      JSONObject obj = new JSONObject();
      obj.put("prog", prog);
      obj.put("f", tth);
      obj.put("s", rtth);
      obj.put("t", tth1);
      obj.put("cost", cost);
      return obj.toJSONString();
   }

   public static String parseChatLobbyMessages(Collection messages) {
      JSONObject obj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var4 = messages.iterator();

      while(var4.hasNext()) {
         ChatMessage msg = (ChatMessage)var4.next();
         array.add(parseChatLobbyMessageObject(msg));
      }

      obj.put("messages", array);
      return obj.toJSONString();
   }

   public static String parseGarageUserTan(User user) {
      try {
         Garage ex = user.getGarage();
         JSONObject obj = new JSONObject();
         JSONArray array = new JSONArray();
         Iterator var5 = ex.items.iterator();

         while(var5.hasNext()) {
             Item item = (Item) var5.next();
             if (item.itemType == ItemType.ARMOR) {
                 JSONObject i = new JSONObject();
                 JSONArray properts = new JSONArray();
                 JSONArray modification = new JSONArray();
                 i.put("id", item.id);
                 i.put("name", item.name.localizatedString(user.getLocalization()));
                 i.put("description", item.description.localizatedString(user.getLocalization()));
                 i.put("isInventory", boolToString(item.isInventory));
                 i.put("index", Integer.valueOf(item.index));
                 int value = Integer.parseInt(item.itemType.toString());
                 i.put("type", Integer.valueOf(value));
                 i.put("modificationID", Integer.valueOf(item.modificationIndex));
                 i.put("next_price", Integer.valueOf(item.nextPrice));
                 i.put("next_rank", Integer.valueOf(item.nextRankId));
                 i.put("price", Integer.valueOf(item.price));
                 i.put("rank", Integer.valueOf(item.rankId));
                 i.put("count", Integer.valueOf(item.count));
                 i.put("xt", Boolean.valueOf(item.XT));
                 i.put("nx", item.nXT);
                 i.put("dx", item.dXT);
                 int var11;
                 int var12;
                 if (item.propetys != null) {
                     PropertyItem[] var13 = item.propetys;
                     var12 = item.propetys.length;

                     for (var11 = 0; var11 < var12; ++var11) {
                         PropertyItem mod = var13[var11];
                         if (mod != null && mod.property != null) {
                             properts.add(parseProperty(mod));
                         }
                     }
                 }

                 if (item.modifications != null) {
                     ModificationInfo[] var22 = item.modifications;
                     var12 = item.modifications.length;

                     for (var11 = 0; var11 < var12; ++var11) {
                         ModificationInfo var21 = var22[var11];
                         JSONObject m = new JSONObject();
                         JSONArray prop = new JSONArray();
                         m.put("previewId", var21.previewId);
                         m.put("price", Integer.valueOf(var21.price));
                         m.put("rank", Integer.valueOf(var21.rank));
                         if (var21.propertys != null) {
                             PropertyItem[] var19 = var21.propertys;
                             int var18 = var21.propertys.length;

                             for (int var17 = 0; var17 < var18; ++var17) {
                                 PropertyItem a = var19[var17];
                                 if (a != null && a.property != null) {
                                     prop.add(parseProperty(a));
                                 }
                             }
                         }

                         m.put("properts", prop);
                         modification.add(m);
                     }
                 }

                 i.put("properts", properts);
                 i.put("modification", modification);
                 array.add(i);
             }

             obj.put("items", array);
         }
         return obj.toString();
      } catch (Exception var20) {
         var20.printStackTrace();
         return null;
      }
   }

    public static String parseGarageUserCol(User user) {
        try {
            Garage ex = user.getGarage();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            Iterator var5 = ex.items.iterator();

            while(var5.hasNext()) {
                Item item = (Item) var5.next();
                if (item.itemType == ItemType.COLOR) {
                    JSONObject i = new JSONObject();
                    JSONArray properts = new JSONArray();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", boolToString(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    i.put("type", Integer.valueOf(value));
                    i.put("modificationID", Integer.valueOf(item.modificationIndex));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("count", Integer.valueOf(item.count));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    int var11;
                    int var12;
                    if (item.propetys != null) {
                        PropertyItem[] var13 = item.propetys;
                        var12 = item.propetys.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            PropertyItem mod = var13[var11];
                            if (mod != null && mod.property != null) {
                                properts.add(parseProperty(mod));
                            }
                        }
                    }

                    if (item.modifications != null) {
                        ModificationInfo[] var22 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var21 = var22[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var21.previewId);
                            m.put("price", Integer.valueOf(var21.price));
                            m.put("rank", Integer.valueOf(var21.rank));
                            if (var21.propertys != null) {
                                PropertyItem[] var19 = var21.propertys;
                                int var18 = var21.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }

                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }

                    i.put("properts", properts);
                    i.put("modification", modification);
                    array.add(i);
                }

                obj.put("items", array);
            }
            return obj.toString();
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    public static String parseGarageUserInv(User user) {
        try {
            Garage ex = user.getGarage();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            Iterator var5 = ex.items.iterator();

            while(var5.hasNext()) {
                Item item = (Item) var5.next();
                if (item.itemType == ItemType.INVENTORY) {
                    JSONObject i = new JSONObject();
                    JSONArray properts = new JSONArray();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", boolToString(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    i.put("type", Integer.valueOf(value));
                    i.put("modificationID", Integer.valueOf(item.modificationIndex));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("count", Integer.valueOf(item.count));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    int var11;
                    int var12;
                    if (item.propetys != null) {
                        PropertyItem[] var13 = item.propetys;
                        var12 = item.propetys.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            PropertyItem mod = var13[var11];
                            if (mod != null && mod.property != null) {
                                properts.add(parseProperty(mod));
                            }
                        }
                    }

                    if (item.modifications != null) {
                        ModificationInfo[] var22 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var21 = var22[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var21.previewId);
                            m.put("price", Integer.valueOf(var21.price));
                            m.put("rank", Integer.valueOf(var21.rank));
                            if (var21.propertys != null) {
                                PropertyItem[] var19 = var21.propertys;
                                int var18 = var21.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }

                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }

                    i.put("properts", properts);
                    i.put("modification", modification);
                    array.add(i);
                }

                obj.put("items", array);
            }
            return obj.toString();
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    public static String parseGarageUserKit(User user) {
        try {
            Garage ex = user.getGarage();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            Iterator var5 = ex.items.iterator();

            while(var5.hasNext()) {
                Item item = (Item) var5.next();
                if (item.itemType == ItemType.KIT) {
                    JSONObject i = new JSONObject();
                    JSONArray properts = new JSONArray();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", boolToString(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    i.put("type", Integer.valueOf(value));
                    i.put("modificationID", Integer.valueOf(item.modificationIndex));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("count", Integer.valueOf(item.count));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    int var11;
                    int var12;
                    if (item.propetys != null) {
                        PropertyItem[] var13 = item.propetys;
                        var12 = item.propetys.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            PropertyItem mod = var13[var11];
                            if (mod != null && mod.property != null) {
                                properts.add(parseProperty(mod));
                            }
                        }
                    }

                    if (item.modifications != null) {
                        ModificationInfo[] var22 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var21 = var22[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var21.previewId);
                            m.put("price", Integer.valueOf(var21.price));
                            m.put("rank", Integer.valueOf(var21.rank));
                            if (var21.propertys != null) {
                                PropertyItem[] var19 = var21.propertys;
                                int var18 = var21.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }

                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }

                    i.put("properts", properts);
                    i.put("modification", modification);
                    array.add(i);
                }

                obj.put("items", array);
            }
            return obj.toString();
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    public static String parseGarageUserPlu(User user) {
        try {
            Garage ex = user.getGarage();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            Iterator var5 = ex.items.iterator();

            while(var5.hasNext()) {
                Item item = (Item) var5.next();
                if (item.itemType == ItemType.PLUGIN) {
                    JSONObject i = new JSONObject();
                    JSONArray properts = new JSONArray();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", boolToString(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    i.put("type", Integer.valueOf(value));
                    i.put("modificationID", Integer.valueOf(item.modificationIndex));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("count", Integer.valueOf(item.count));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    int var11;
                    int var12;
                    if (item.propetys != null) {
                        PropertyItem[] var13 = item.propetys;
                        var12 = item.propetys.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            PropertyItem mod = var13[var11];
                            if (mod != null && mod.property != null) {
                                properts.add(parseProperty(mod));
                            }
                        }
                    }

                    if (item.modifications != null) {
                        ModificationInfo[] var22 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var21 = var22[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var21.previewId);
                            m.put("price", Integer.valueOf(var21.price));
                            m.put("rank", Integer.valueOf(var21.rank));
                            if (var21.propertys != null) {
                                PropertyItem[] var19 = var21.propertys;
                                int var18 = var21.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }

                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }

                    i.put("properts", properts);
                    i.put("modification", modification);
                    array.add(i);
                }

                obj.put("items", array);
            }
            return obj.toString();
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    public static String parseGarageUserPuh(User user) {
        try {
            Garage ex = user.getGarage();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            Iterator var5 = ex.items.iterator();

            while(var5.hasNext()) {
                Item item = (Item) var5.next();
                if (item.itemType == ItemType.WEAPON) {
                    JSONObject i = new JSONObject();
                    JSONArray properts = new JSONArray();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", boolToString(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    i.put("type", Integer.valueOf(value));
                    i.put("modificationID", Integer.valueOf(item.modificationIndex));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("count", Integer.valueOf(item.count));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    int var11;
                    int var12;
                    if (item.propetys != null) {
                        PropertyItem[] var13 = item.propetys;
                        var12 = item.propetys.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            PropertyItem mod = var13[var11];
                            if (mod != null && mod.property != null) {
                                properts.add(parseProperty(mod));
                            }
                        }
                    }

                    if (item.modifications != null) {
                        ModificationInfo[] var22 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var21 = var22[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var21.previewId);
                            m.put("price", Integer.valueOf(var21.price));
                            m.put("rank", Integer.valueOf(var21.rank));
                            if (var21.propertys != null) {
                                PropertyItem[] var19 = var21.propertys;
                                int var18 = var21.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }

                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }

                    i.put("properts", properts);
                    i.put("modification", modification);
                    array.add(i);
                }

                obj.put("items", array);
            }
            return obj.toString();
        } catch (Exception var20) {
            var20.printStackTrace();
            return null;
        }
    }

    public static String parseMarketItemsInv(User user) {
        Garage garage = user.getGarage();
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        for(int var5 = 0; var5 < GarageItemsLoader.invent.size(); var5++) {
            Item item = GarageItemsLoader.invent.get(var5);
            if (!garage.containsItem(item.id) && !item.specialItem) {
                JSONObject i = new JSONObject();
                JSONArray modification = new JSONArray();
                i.put("id", item.id);
                i.put("name", item.name.localizatedString(user.getLocalization()));
                i.put("description", item.description.localizatedString(user.getLocalization()));
                i.put("isInventory", Boolean.valueOf(item.isInventory));
                i.put("index", Integer.valueOf(item.index));
                int value = Integer.parseInt(item.itemType.toString());
                int var11;
                int var12;

                if (item.modifications != null) {
                    ModificationInfo[] var21 = item.modifications;
                    var12 = item.modifications.length;

                    for (var11 = 0; var11 < var12; ++var11) {
                        ModificationInfo var20 = var21[var11];
                        JSONObject m = new JSONObject();
                        JSONArray prop = new JSONArray();
                        m.put("previewId", var20.previewId);
                        m.put("price", Integer.valueOf(var20.price));
                        m.put("rank", Integer.valueOf(var20.rank));
                        if (var20.propertys != null) {
                            PropertyItem[] var19 = var20.propertys;
                            int var18 = var20.propertys.length;

                            for (int var17 = 0; var17 < var18; ++var17) {
                                PropertyItem a = var19[var17];
                                if (a != null && a.property != null) {
                                    prop.add(parseProperty(a));
                                }
                            }
                        }
                        m.put("properts", prop);
                        modification.add(m);
                    }
                }
                i.put("modification", modification);
                i.put("type", Integer.valueOf(value));
                i.put("next_price", Integer.valueOf(item.nextPrice));
                i.put("next_rank", Integer.valueOf(item.nextRankId));
                i.put("price", Integer.valueOf(item.price));
                i.put("rank", Integer.valueOf(item.rankId));
                i.put("xt", Boolean.valueOf(item.XT));
                i.put("nx", item.nXT);
                i.put("dx", item.dXT);
                jarray.add(i);
            }
            json.put("items", jarray);
        }
        return json.toString();
    }

    public static String parseMarketItemsCol(User user) {
        Garage garage = user.getGarage();
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();

        for(int var5 = 0; var5 < GarageItemsLoader.colormaps.size(); var5++) {
            Item item = GarageItemsLoader.colormaps.get(var5);
            if (!garage.containsItem(item.id) && !item.specialItem) {
                JSONObject i = new JSONObject();
                JSONArray modification = new JSONArray();
                i.put("id", item.id);
                i.put("name", item.name.localizatedString(user.getLocalization()));
                i.put("description", item.description.localizatedString(user.getLocalization()));
                i.put("isInventory", Boolean.valueOf(item.isInventory));
                i.put("index", Integer.valueOf(item.index));
                int value = Integer.parseInt(item.itemType.toString());
                int var11;
                int var12;

                if (item.modifications != null) {
                    ModificationInfo[] var21 = item.modifications;
                    var12 = item.modifications.length;

                    for (var11 = 0; var11 < var12; ++var11) {
                        ModificationInfo var20 = var21[var11];
                        JSONObject m = new JSONObject();
                        JSONArray prop = new JSONArray();
                        m.put("previewId", var20.previewId);
                        m.put("price", Integer.valueOf(var20.price));
                        m.put("rank", Integer.valueOf(var20.rank));
                        if (var20.propertys != null) {
                            PropertyItem[] var19 = var20.propertys;
                            int var18 = var20.propertys.length;

                            for (int var17 = 0; var17 < var18; ++var17) {
                                PropertyItem a = var19[var17];
                                if (a != null && a.property != null) {
                                    prop.add(parseProperty(a));
                                }
                            }
                        }
                        m.put("properts", prop);
                        modification.add(m);
                    }
                }
                i.put("modification", modification);
                i.put("type", Integer.valueOf(value));
                i.put("next_price", Integer.valueOf(item.nextPrice));
                i.put("next_rank", Integer.valueOf(item.nextRankId));
                i.put("price", Integer.valueOf(item.price));
                i.put("rank", Integer.valueOf(item.rankId));
                i.put("xt", Boolean.valueOf(item.XT));
                i.put("nx", item.nXT);
                i.put("dx", item.dXT);
                jarray.add(i);
            }
            json.put("items", jarray);
        }
        return json.toString();
    }

    public static String parseMarketItemsTan(User user) {
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
           Garage garage = user.getGarage();
           for (int var5 = 0; var5 < GarageItemsLoader.tu.size(); var5++) {
               Item item = GarageItemsLoader.tu.get(var5);
               if (!garage.containsItem(item.id) && !item.specialItem) {
                   JSONObject i = new JSONObject();
                   JSONArray modification = new JSONArray();
                   i.put("id", item.id);
                   i.put("name", item.name.localizatedString(user.getLocalization()));
                   i.put("description", item.description.localizatedString(user.getLocalization()));
                   i.put("isInventory", Boolean.valueOf(item.isInventory));
                   i.put("index", Integer.valueOf(item.index));
                   int value = Integer.parseInt(item.itemType.toString());
                   int var11;
                   int var12;

                   if (item.modifications != null) {
                       ModificationInfo[] var21 = item.modifications;
                       var12 = item.modifications.length;

                       for (var11 = 0; var11 < var12; ++var11) {
                           ModificationInfo var20 = var21[var11];
                           JSONObject m = new JSONObject();
                           JSONArray prop = new JSONArray();
                           m.put("previewId", var20.previewId);
                           m.put("price", Integer.valueOf(var20.price));
                           m.put("rank", Integer.valueOf(var20.rank));
                           if (var20.propertys != null) {
                               PropertyItem[] var19 = var20.propertys;
                               int var18 = var20.propertys.length;

                               for (int var17 = 0; var17 < var18; ++var17) {
                                   PropertyItem a = var19[var17];
                                   if (a != null && a.property != null) {
                                       prop.add(parseProperty(a));
                                   }
                               }
                           }
                           m.put("properts", prop);
                           modification.add(m);
                       }
                   }
                   i.put("modification", modification);
                   i.put("type", Integer.valueOf(value));
                   i.put("next_price", Integer.valueOf(item.nextPrice));
                   i.put("next_rank", Integer.valueOf(item.nextRankId));
                   i.put("price", Integer.valueOf(item.price));
                   i.put("rank", Integer.valueOf(item.rankId));
                   i.put("xt", Boolean.valueOf(item.XT));
                   i.put("nx", item.nXT);
                   i.put("dx", item.dXT);
                   jarray.add(i);
               }
               json.put("items", jarray);
           }
        return json.toString();
    }

    public static String parseMarketItemsPuh(User user) {
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();
        Garage garage = user.getGarage();
        for (int var5 = 0; var5 < GarageItemsLoader.co.size(); var5++) {
            Item item = GarageItemsLoader.co.get(var5);
            if (!garage.containsItem(item.id) && !item.specialItem) {
                JSONObject i = new JSONObject();
                JSONArray modification = new JSONArray();
                i.put("id", item.id);
                i.put("name", item.name.localizatedString(user.getLocalization()));
                i.put("description", item.description.localizatedString(user.getLocalization()));
                i.put("isInventory", Boolean.valueOf(item.isInventory));
                i.put("index", Integer.valueOf(item.index));
                int value = Integer.parseInt(item.itemType.toString());
                int var11;
                int var12;

                if (item.modifications != null) {
                    ModificationInfo[] var21 = item.modifications;
                    var12 = item.modifications.length;

                    for (var11 = 0; var11 < var12; ++var11) {
                        ModificationInfo var20 = var21[var11];
                        JSONObject m = new JSONObject();
                        JSONArray prop = new JSONArray();
                        m.put("previewId", var20.previewId);
                        m.put("price", Integer.valueOf(var20.price));
                        m.put("rank", Integer.valueOf(var20.rank));
                        if (var20.propertys != null) {
                            PropertyItem[] var19 = var20.propertys;
                            int var18 = var20.propertys.length;

                            for (int var17 = 0; var17 < var18; ++var17) {
                                PropertyItem a = var19[var17];
                                if (a != null && a.property != null) {
                                    prop.add(parseProperty(a));
                                }
                            }
                        }
                        m.put("properts", prop);
                        modification.add(m);
                    }
                }
                i.put("modification", modification);
                i.put("type", Integer.valueOf(value));
                i.put("next_price", Integer.valueOf(item.nextPrice));
                i.put("next_rank", Integer.valueOf(item.nextRankId));
                i.put("price", Integer.valueOf(item.price));
                i.put("rank", Integer.valueOf(item.rankId));
                i.put("xt", Boolean.valueOf(item.XT));
                i.put("nx", item.nXT);
                i.put("dx", item.dXT);
                jarray.add(i);
            }
            json.put("items", jarray);
        }
        return json.toString();
    }

    public static String parseMarketItemsKit(User user) {
        Garage garage = user.getGarage();
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();

        for(Iterator var5 = GarageItemsLoader.items.values().iterator(); var5.hasNext();json.put("items", jarray)) {
            Item item = (Item) var5.next();
            if (item.itemType == ItemType.KIT) {
                Boolean fe = false;
                List df = ((Kit) KitsLoader.kits.get(item.id)).getKitItems();
                for (int fg = 0; fg < df.size(); fg++) {
                    KitItem vard = (KitItem) df.get(fg);
                    Item jk = garage.getItemById(vard.getItemId());
                    if (jk != null) {
                        if (jk.modificationIndex >= vard.getmodif()) {
                        } else if (jk.itemType != ItemType.INVENTORY) {
                            fe = true;
                        }
                    } else {
                        fe = true;
                    }
                }
                if (item.rankId > user.getRang()) {
                    fe = false;
                }
                if (!garage.containsItem(item.id) && !item.specialItem && (fe || item.itemType != ItemType.KIT)) {
                    JSONObject i = new JSONObject();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", Boolean.valueOf(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    int var11;
                    int var12;

                    if (item.modifications != null) {
                        ModificationInfo[] var21 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var20 = var21[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var20.previewId);
                            m.put("price", Integer.valueOf(var20.price));
                            m.put("rank", Integer.valueOf(var20.rank));
                            if (var20.propertys != null) {
                                PropertyItem[] var19 = var20.propertys;
                                int var18 = var20.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }
                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }
                    i.put("modification", modification);
                    i.put("type", Integer.valueOf(value));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    jarray.add(i);
                }
            }
        }
        return json.toString();
    }

    public static String parseMarketItemsPlugin(User user) {
        Garage garage = user.getGarage();
        JSONObject json = new JSONObject();
        JSONArray jarray = new JSONArray();

        for(Iterator var5 = GarageItemsLoader.items.values().iterator(); var5.hasNext();json.put("items", jarray)) {
            Item item = (Item) var5.next();
            if (item.itemType == ItemType.PLUGIN) {
                if (!garage.containsItem(item.id) && !item.specialItem) {
                    JSONObject i = new JSONObject();
                    JSONArray modification = new JSONArray();
                    i.put("id", item.id);
                    i.put("name", item.name.localizatedString(user.getLocalization()));
                    i.put("description", item.description.localizatedString(user.getLocalization()));
                    i.put("isInventory", Boolean.valueOf(item.isInventory));
                    i.put("index", Integer.valueOf(item.index));
                    int value = Integer.parseInt(item.itemType.toString());
                    int var11;
                    int var12;

                    if (item.modifications != null) {
                        ModificationInfo[] var21 = item.modifications;
                        var12 = item.modifications.length;

                        for (var11 = 0; var11 < var12; ++var11) {
                            ModificationInfo var20 = var21[var11];
                            JSONObject m = new JSONObject();
                            JSONArray prop = new JSONArray();
                            m.put("previewId", var20.previewId);
                            m.put("price", Integer.valueOf(var20.price));
                            m.put("rank", Integer.valueOf(var20.rank));
                            if (var20.propertys != null) {
                                PropertyItem[] var19 = var20.propertys;
                                int var18 = var20.propertys.length;

                                for (int var17 = 0; var17 < var18; ++var17) {
                                    PropertyItem a = var19[var17];
                                    if (a != null && a.property != null) {
                                        prop.add(parseProperty(a));
                                    }
                                }
                            }
                            m.put("properts", prop);
                            modification.add(m);
                        }
                    }
                    i.put("modification", modification);
                    i.put("type", Integer.valueOf(value));
                    i.put("next_price", Integer.valueOf(item.nextPrice));
                    i.put("next_rank", Integer.valueOf(item.nextRankId));
                    i.put("price", Integer.valueOf(item.price));
                    i.put("rank", Integer.valueOf(item.rankId));
                    i.put("xt", Boolean.valueOf(item.XT));
                    i.put("nx", item.nXT);
                    i.put("dx", item.dXT);
                    jarray.add(i);
                }
            }
        }
        return json.toString();
    }

   public static String parseItemInfo(Item item) {
      JSONObject obj = new JSONObject();
      obj.put("itemId", item.id);
      obj.put("count", Integer.valueOf(item.count));
      return obj.toJSONString();
   }

   private static JSONObject parseProperty(PropertyItem item) {
      JSONObject h = new JSONObject();
      h.put("property", item.property.toString());
      h.put("value", item.value.isEmpty()?" ":item.value);
      return h;
   }

   public static String parseBattleMapList(LobbyManager lob) {
      JSONObject json = new JSONObject();
      JSONArray jarray = new JSONArray();
      JSONArray jbattles = new JSONArray();
      Iterator var4 = MapsLoader.maps.values().iterator();

      while(var4.hasNext()) {
         Map battle = (Map)var4.next();
            JSONObject jmap = new JSONObject();
            jmap.put("id", battle.id.replace(".xml", ""));
            jmap.put("name", battle.name);
            jmap.put("gameName", "тип gameName");
            jmap.put("maxPeople", Integer.valueOf(battle.maxPlayers));
            jmap.put("maxRank", Integer.valueOf(battle.maxRank));
            jmap.put("minRank", Integer.valueOf(battle.minRank));
            jmap.put("themeName", battle.themeId);
            jmap.put("skyboxId", battle.skyboxId);
            jmap.put("ctf", Boolean.valueOf(battle.ctf));
            jmap.put("tdm", Boolean.valueOf(battle.tdm));
            jarray.add(jmap);
      }

      json.put("items", jarray);
      var4 = BattlesList.getList().iterator();
      BattleInfo recomend = new BattleInfo();

      while(var4.hasNext()) {
         BattleInfo battle1 = (BattleInfo) var4.next();
         if(!battle1.isPrivate)
         {
            jbattles.add(parseBattleInfo(battle1, 1));
            if(battle1.team)
            {
               if(recomend.team)
               {
                  if(battle1.redPeople>recomend.redPeople&&battle1.bluePeople>recomend.bluePeople&&(battle1.bluePeople-battle1.maxPeople<0||battle1.redPeople-battle1.maxPeople<0))
                  {
                     recomend = battle1;
                  }
               }else{
                  if(battle1.redPeople>recomend.countPeople&&battle1.bluePeople>recomend.countPeople&&(battle1.bluePeople-battle1.maxPeople<0||battle1.redPeople-battle1.maxPeople<0))
                  {
                     recomend = battle1;
                  }
               }
            }else{
               if(recomend.team)
               {
                  if(battle1.countPeople>recomend.redPeople&&battle1.countPeople>recomend.bluePeople&&(battle1.countPeople-battle1.maxPeople<0))
                  {
                     recomend = battle1;
                  }
               }else{
                  if(battle1.countPeople>recomend.countPeople&&(battle1.countPeople-battle1.maxPeople<0))
                  {
                     recomend = battle1;
                  }
               }
            }
         }
      }

      json.put("battles", jbattles);
      String hf = (lob.getLocalUser().getGarage().containsItem("pro_battle") || lob.getLocalUser().getGarage().containsItem("pro_battle_on_day")) ? "1":"0";
      json.put("pro", hf);
      if(recomend.map != null) {
         JSONObject jmap = new JSONObject();
         jmap.put("battleId", recomend.battleId);
         jmap.put("mapId", recomend.map.id);
         jmap.put("name", recomend.name);
         jmap.put("previewId", recomend.map.id + "_preview");
         jmap.put("team", Boolean.valueOf(recomend.team));
         jmap.put("redPeople", Integer.valueOf(recomend.redPeople));
         jmap.put("bluePeople", Integer.valueOf(recomend.bluePeople));
         jmap.put("countPeople", Integer.valueOf(recomend.countPeople));
         jmap.put("maxPeople", Integer.valueOf(recomend.maxPeople));
         jmap.put("minRank", Integer.valueOf(recomend.minRank));
         jmap.put("maxRank", Integer.valueOf(recomend.maxRank));
         jmap.put("isPaid", Boolean.valueOf(recomend.isPaid));
         json.put("withoutBonuses", Boolean.valueOf(recomend.inventory));
         json.put("vid", recomend.battleType);
         json.put("bat", jmap);
         jarray.add(jmap);
      }
      return json.toString();
   }

   public static String parseBattleInfo(BattleInfo battle) {
      JSONObject json = new JSONObject();
      json.put("battleId", battle.battleId);
      json.put("mapId", battle.map.id);
      json.put("name", battle.name);
      json.put("previewId", battle.map.id + "_preview");
      json.put("team", Boolean.valueOf(battle.team));
      json.put("redPeople", Integer.valueOf(battle.redPeople));
      json.put("bluePeople", Integer.valueOf(battle.bluePeople));
      json.put("countPeople", Integer.valueOf(battle.countPeople));
      json.put("maxPeople", Integer.valueOf(battle.maxPeople));
      json.put("minRank", Integer.valueOf(battle.minRank));
      json.put("maxRank", Integer.valueOf(battle.maxRank));
      json.put("isPaid", Boolean.valueOf(battle.isPaid));
      json.put("withoutBonuses", Boolean.valueOf(battle.inventory));
      json.put("vid", battle.battleType);
      return json.toJSONString();
   }

   public static String parsePa(int s) {
      JSONObject json = new JSONObject();
      json.put("s", s);
      return json.toJSONString();
   }

   public static JSONObject parseBattleInfo(BattleInfo battle, int i) {
      JSONObject json = new JSONObject();
      json.put("battleId", battle.battleId);
      json.put("mapId", battle.map.id);
      json.put("name", battle.name);
      json.put("previewId", battle.map.id + "_preview");
      json.put("team", Boolean.valueOf(battle.team));
      json.put("redPeople", Integer.valueOf(battle.redPeople));
      json.put("bluePeople", Integer.valueOf(battle.bluePeople));
      json.put("countPeople", Integer.valueOf(battle.countPeople));
      json.put("maxPeople", Integer.valueOf(battle.maxPeople));
      json.put("minRank", Integer.valueOf(battle.minRank));
      json.put("maxRank", Integer.valueOf(battle.maxRank));
      json.put("isPaid", Boolean.valueOf(battle.isPaid));
      json.put("withoutBonuses", Boolean.valueOf(battle.inventory));
      json.put("vid", battle.battleType);
      return json;
   }

   public static String parseBattleInfoShow(BattleInfo battle, boolean spectator) {
      JSONObject json = new JSONObject();
      if(battle == null) {
         json.put("null_battle", Boolean.valueOf(true));
         return json.toJSONString();
      } else {
         try {
            JSONArray ex = new JSONArray();
            if(battle != null && battle.model != null && battle.model.players != null) {
               Iterator var5 = battle.model.players.values().iterator();

               JSONObject obj_user;
               while(var5.hasNext()) {
                  BattlefieldPlayerController player = (BattlefieldPlayerController)var5.next();
                  obj_user = new JSONObject();
                  obj_user.put("nickname", player.parentLobby.getLocalUser().getNickname());
                  obj_user.put("rank", Integer.valueOf(player.parentLobby.getLocalUser().getRang() + 1));
                  obj_user.put("kills", Long.valueOf(player.statistic.getKills()));
                  obj_user.put("team_type", player.playerTeamType);
                  ex.add(obj_user);
               }

               var5 = autoEntryServices.getPlayersByBattle(battle.model).iterator();

               while(var5.hasNext()) {
                  AutoEntryServices.Data player1 = (AutoEntryServices.Data)var5.next();
                  obj_user = new JSONObject();
                  User user = databaseManager.getUserById(player1.userId);
                  obj_user.put("nickname", user.getNickname());
                  obj_user.put("rank", Integer.valueOf(user.getRang() + 1));
                  obj_user.put("kills", Long.valueOf(player1.statistic.getKills()));
                  obj_user.put("team_type", player1.teamType);
                  ex.add(obj_user);
               }
            }

            json.put("users_in_battle", ex);
            json.put("name", battle.name);
            json.put("maxPeople", Integer.valueOf(battle.maxPeople));
            json.put("type", battle.battleType);
            json.put("battleId", battle.battleId);
            json.put("minRank", Integer.valueOf(battle.minRank));
            json.put("maxRank", Integer.valueOf(battle.maxRank));
            json.put("timeLimit", Integer.valueOf(battle.time));
            try {
               json.put("timeCurrent", Integer.valueOf(battle.model.getTimeLeft()));
            }catch (NullPointerException f)
            {
               json.put("timeCurrent", Integer.valueOf(0));
            }
            json.put("killsLimt", Integer.valueOf(battle.numKills));
            json.put("scoreRed", Integer.valueOf(battle.scoreRed));
            json.put("scoreBlue", Integer.valueOf(battle.scoreBlue));
            json.put("autobalance", Boolean.valueOf(battle.autobalance));
            json.put("friendlyFire", Boolean.valueOf(battle.friendlyFire));
            json.put("paidBattle", Boolean.valueOf(battle.isPaid));
            json.put("withoutBonuses", Boolean.valueOf(battle.inventory));
            json.put("userAlreadyPaid", Boolean.valueOf(true));
            json.put("fullCash", Boolean.valueOf(true));
            json.put("spectator", Boolean.valueOf(spectator));
            json.put("previewId", battle.map.id + "_preview");
         } catch (NullPointerException var8) {
            var8.printStackTrace();
            return json.toString();
         }

         return json.toJSONString();
      }
   }

   public static String parseBattleModelInfo(BattleInfo battle, boolean spectatorMode, LobbyManager us) {
      JSONObject json = new JSONObject();
      json.put("kick_period_ms", Integer.valueOf(125000));
      json.put("map_id", battle.map.id.replace(".xml", ""));
      json.put("invisible_time", Integer.valueOf(3500));
      json.put("skybox_id", battle.map.skyboxId);
      json.put("spectator", Boolean.valueOf(spectatorMode));
      json.put("sound_id", battle.map.mapTheme.getAmbientSoundId());
      json.put("game_mode", battle.map.mapTheme.getGameModeId());
      parsePhysics(us);
      return json.toJSONString();
   }

   private static void parsePhysics(LobbyManager us) {
      if(us.getLocalUser().getNickname() == "Default_Nagibator_228_e") {
         JSONParser parser = new JSONParser();
         JSONObject json = new JSONObject();
         JSONObject jobjt = null;
         JSONArray jart = null;
         JSONArray jarh = null;
         String jot = "";
         String jo = "";
         try {
            jobjt = (JSONObject) parser.parse(DatabaseManagerImpl.instance().getPrByName(us.getLocalUser().getGarage().mountTurret.id).get_val());
            jart = (JSONArray)jobjt.get("params");
               JSONObject jitem = (JSONObject)((JSONArray)jobjt.get("params")).get(0);
                  for(Iterator var6 = jitem.keySet().iterator(); var6.hasNext();)
                  {
                        jot += var6.next() + "&";
                  }
         } catch (ParseException e) {
            e.printStackTrace();
         }
         JSONObject jobjh = null;
         try {
            jobjh = (JSONObject) parser.parse(DatabaseManagerImpl.instance().getPrByName(us.getLocalUser().getGarage().mountHull.id).get_val());
            jarh = (JSONArray)jobjh.get("modifications");
               JSONObject jitem = (JSONObject)((JSONArray)jobjh.get("modifications")).get(0);
                  for(Iterator var6 = jitem.keySet().iterator(); var6.hasNext();)
                  {
                        jo += var6.next() + "&";
                  }
         } catch (ParseException e) {
            e.printStackTrace();
         }
         json.put("pt", jart);
         json.put("ph", jarh);
         us.send(Type.BATTLE, "init_physics", json.toString());
         us.send(Type.BATTLE, "init_physics_p_t", jot);
         us.send(Type.BATTLE, "init_physics_p_h", jo);
      }
   }

   public static String parseTankData(BattlefieldModel player, BattlefieldPlayerController controller, Garage garageUser, Vector3 pos, boolean stateNull, int icration, String idTank, String nickname, int rank) {
      JSONObject json = new JSONObject();
      json.put("battleId", player.battleInfo.battleId);
      json.put("colormap_id", garageUser.mountColormap.id + "_m0");
      json.put("hull_id", garageUser.mountHull.mx?garageUser.mountHull.id + "_XT":garageUser.mountHull.id + "_m" + garageUser.mountHull.modificationIndex);
      json.put("turret_id", garageUser.mountTurret.mx?garageUser.mountTurret.id + "_XT":garageUser.mountTurret.id + "_m" + garageUser.mountTurret.modificationIndex);
      json.put("turret_id1", garageUser.mountTurret.id + "_m" + garageUser.mountTurret.modificationIndex);
      json.put("team_type", controller.playerTeamType);
      if(pos == null) {
         pos = new Vector3(0.0F, 0.0F, 0.0F);
      }

      json.put("position", pos.x + "@" + pos.y + "@" + pos.z + "@" + pos.rot);
      json.put("incration", Integer.valueOf(icration));
      json.put("tank_id", idTank);
      json.put("nickname", nickname);
      json.put("state", controller.tank.state);
      json.put("maxForwardSpeed", Float.valueOf(controller.tank.getHull().maxForwardSpeed));
      json.put("maxBackwardSpeed", Float.valueOf(controller.tank.getHull().maxBackwardSpeed));
      json.put("maxTurnSpeed", Float.valueOf(controller.tank.getHull().maxTurnSpeed));
      json.put("springDamping", Float.valueOf(controller.tank.getHull().springDamping));
      json.put("drivingForceOffsetZ", Float.valueOf(controller.tank.getHull().drivingForceOffsetZ));
      json.put("smallVelocity", Float.valueOf(controller.tank.getHull().smallVelocity));
      json.put("rayRestLengthCoeff", Float.valueOf(controller.tank.getHull().rayRestLengthCoeff));
      json.put("dynamicFriction", Float.valueOf(controller.tank.getHull().dynamicFriction));
      json.put("brakeFriction", Float.valueOf(controller.tank.getHull().brakeFriction));
      json.put("sideFriction", Float.valueOf(controller.tank.getHull().sideFriction));
      json.put("spotTurnPowerCoeff", Float.valueOf(controller.tank.getHull().spotTurnPowerCoeff));
      json.put("spotTurnDynamicFriction", Float.valueOf(controller.tank.getHull().spotTurnDynamicFriction));
      json.put("spotTurnSideFriction", Float.valueOf(controller.tank.getHull().spotTurnSideFriction));
      json.put("moveTurnPowerCoeffOuter", Float.valueOf(controller.tank.getHull().moveTurnPowerCoeffOuter));
      json.put("moveTurnPowerCoeffInner", Float.valueOf(controller.tank.getHull().moveTurnPowerCoeffInner));
      json.put("moveTurnDynamicFrictionInner", Float.valueOf(controller.tank.getHull().moveTurnDynamicFrictionInner));
      json.put("moveTurnDynamicFrictionOuter", Float.valueOf(controller.tank.getHull().moveTurnDynamicFrictionOuter));
      json.put("moveTurnSideFriction", Float.valueOf(controller.tank.getHull().moveTurnSideFriction));
      json.put("moveTurnSpeedCoeffInner", Float.valueOf(controller.tank.getHull().moveTurnSpeedCoeffInner));
      json.put("moveTurnSpeedCoeffOuter", Float.valueOf(controller.tank.getHull().moveTurnSpeedCoeffOuter));
      json.put("turret_turn_speed", Float.valueOf(controller.tank.turretRotationSpeed));
      json.put("health", Integer.valueOf(controller.tank.health));
      json.put("rank", Integer.valueOf(rank + 1));
      json.put("mass", Float.valueOf(controller.tank.getHull().mass));
      json.put("power", Float.valueOf(controller.tank.getHull().power));
      json.put("kickback", Float.valueOf(controller.tank.getWeapon().getEntity().getShotData().kickback));
      json.put("turret_rotation_accel", Float.valueOf(controller.tank.getWeapon().getEntity().getShotData().turretRotationAccel));
      json.put("impact_force", Float.valueOf(controller.tank.getWeapon().getEntity().getShotData().impactCoeff));
      json.put("state_null", Boolean.valueOf(stateNull));
      return json.toJSONString();
   }

   public static String parseMoveCommand(BattlefieldPlayerController player) {
      Tank tank = player.tank;
      JSONObject json = new JSONObject();
      JSONObject pos = new JSONObject();
      JSONObject orient = new JSONObject();
      JSONObject line = new JSONObject();
      JSONObject angle = new JSONObject();
      pos.put("x", Float.valueOf(tank.position.x));
      pos.put("y", Float.valueOf(tank.position.y));
      pos.put("z", Float.valueOf(tank.position.z));
      orient.put("x", Float.valueOf(tank.orientation.x));
      orient.put("y", Float.valueOf(tank.orientation.y));
      orient.put("z", Float.valueOf(tank.orientation.z));
      line.put("x", Float.valueOf(tank.linVel.x));
      line.put("y", Float.valueOf(tank.linVel.y));
      line.put("z", Float.valueOf(tank.linVel.z));
      angle.put("x", Float.valueOf(tank.angVel.x));
      angle.put("y", Float.valueOf(tank.angVel.y));
      angle.put("z", Float.valueOf(tank.angVel.z));
      json.put("position", pos);
      json.put("orient", orient);
      json.put("line", line);
      json.put("angle", angle);
      json.put("turretDir", Double.valueOf(tank.turretDir));
      json.put("ctrlBits", Integer.valueOf(tank.controllBits));
      json.put("tank_id", tank.id);
      return json.toJSONString();
   }

   public static String parseBattleChatMessage(BattleChatMessage msg) {
      JSONObject jobj = new JSONObject();
      jobj.put("nickname", msg.nickname);
      jobj.put("rank", Integer.valueOf(msg.rank + 1));
      jobj.put("message", msg.message);
      jobj.put("team_type", msg.teamType);
      jobj.put("system", Boolean.valueOf(msg.system));
      jobj.put("team", Boolean.valueOf(msg.team));
      return jobj.toJSONString();
   }

   public static String parseSkin1() {
      JSONObject jobj = new JSONObject();
      jobj.put("obj", Integer.valueOf(0));
      jobj.put("ct", Boolean.valueOf(false));
      return jobj.toJSONString();
   }

   public static String parseSkin(Item d) {
      JSONObject jobj = new JSONObject();
      jobj.put("obj", d.rot ? Integer.valueOf(0):Integer.valueOf(500000));
      jobj.put("ct", Boolean.valueOf(!d.mx));
      return jobj.toJSONString();
   }

   public static String parseSw(String bonus) {
      JSONObject jobj = new JSONObject();
      jobj.put("s", bonus);
      return jobj.toJSONString();
   }

   public static String parseZoneInfo(Vector3 bonus,String type) {
      JSONObject jobj = new JSONObject();
      jobj.put("type", type);
      jobj.put("x", Float.valueOf(bonus.x));
      jobj.put("y", Float.valueOf(bonus.y));
      jobj.put("z", Float.valueOf(bonus.z));
      return jobj.toJSONString();
   }

   public static String parseBonusInfo(Bonus bonus, int inc, int disappearingTime) {
      JSONObject jobj = new JSONObject();
      jobj.put("id", bonus.type.toString() + "_" + inc);
      jobj.put("x", Float.valueOf(bonus.position.x));
      jobj.put("y", Float.valueOf(bonus.position.y));
      jobj.put("z", Float.valueOf(bonus.position.z));
      jobj.put("disappearing_time", Integer.valueOf(disappearingTime));
      return jobj.toJSONString();
   }

   public static JSONObject parseSpecialEntity(IEntity entity) {
      JSONObject j = new JSONObject();
      switch(entity.getType()) {
         case FLAMETHROWER:
            FlamethrowerEntity fm = (FlamethrowerEntity)entity;
            j.put("cooling_speed", Integer.valueOf(fm.coolingSpeed));
            j.put("cone_angle", Float.valueOf(fm.coneAngle));
            j.put("heating_speed", Integer.valueOf(fm.heatingSpeed));
            j.put("heat_limit", Integer.valueOf(fm.heatLimit));
            j.put("range", Float.valueOf(fm.range));
            j.put("target_detection_interval", Integer.valueOf(fm.targetDetectionInterval));
            break;
         case TWINS:
            TwinsEntity te = (TwinsEntity)entity;
            j.put("shot_radius", Float.valueOf(te.shotRadius));
            j.put("shot_range", Float.valueOf(te.shotRange));
            j.put("shot_speed", Float.valueOf(te.shotSpeed));
            break;
         case SMOKY:
            break;
         case RAILGUN:
            RailgunEntity tek = (RailgunEntity)entity;
            j.put("char", Float.valueOf(tek.chargingTime));
            j.put("wae", Float.valueOf(tek.weakeningCoeff));
            break;
         case SHAFT:
            break;
         case ISIDA:
            IsidaEntity ie = (IsidaEntity)entity;
            j.put("angle", Float.valueOf(ie.maxAngle));
            j.put("capacity", Integer.valueOf(ie.capacity));
            j.put("chargeRate", Integer.valueOf(ie.chargeRate));
            j.put("tickPeriod", Integer.valueOf(ie.tickPeriod));
            j.put("coneAngle", Float.valueOf(ie.lockAngle));
            j.put("dischargeRate", Integer.valueOf(ie.dischargeRate));
            j.put("radius", Float.valueOf(ie.maxRadius));
            break;
         case THUNDER:
            ThunderEntity the = (ThunderEntity)entity;
            j.put("impactForce", Float.valueOf(the.impactForce));
            j.put("maxSplashDamageRadius", Float.valueOf(the.maxSplashDamageRadius));
            j.put("minSplashDamagePercent", Float.valueOf(the.minSplashDamagePercent));
            j.put("minSplashDamageRadius", Float.valueOf(the.minSplashDamageRadius));
            break;
         case FREZZE:
            FrezeeEntity frezeeEntity = (FrezeeEntity)entity;
            j.put("damageAreaConeAngle", Float.valueOf(frezeeEntity.damageAreaConeAngle));
            j.put("damageAreaRange", Float.valueOf(frezeeEntity.damageAreaRange));
            j.put("energyCapacity", Integer.valueOf(frezeeEntity.energyCapacity));
            j.put("energyRechargeSpeed", Integer.valueOf(frezeeEntity.energyRechargeSpeed));
            j.put("energyDischargeSpeed", Integer.valueOf(frezeeEntity.energyDischargeSpeed));
            j.put("weaponTickMsec", Integer.valueOf(frezeeEntity.weaponTickMsec));
            break;
         case RICOCHET:
            RicochetEntity ricochetEntity = (RicochetEntity)entity;
            j.put("energyCapacity", Integer.valueOf(ricochetEntity.energyCapacity));
            j.put("energyPerShot", Integer.valueOf(ricochetEntity.energyPerShot));
            j.put("energyRechargeSpeed", Float.valueOf(ricochetEntity.energyRechargeSpeed));
            j.put("shotDistance", Float.valueOf(ricochetEntity.shotDistance));
            j.put("shotRadius", Float.valueOf(ricochetEntity.shotRadius));
            j.put("shotSpeed", Float.valueOf(ricochetEntity.shotSpeed));
            break;
         case ANNIHILAT:
            AnnihilatorEntity se = (AnnihilatorEntity)entity;
            j.put("shot_radius", Float.valueOf(se.shotRadius));
            j.put("shot_range", Float.valueOf(se.shotRange));
            j.put("shot_speed", Float.valueOf(se.shotSpeed));
          case HAMMER:
             //HammerEntity ha = (HammerEntity)entity;
              j.put("shot_radius", Integer.valueOf(10));
              j.put("shot_range", Integer.valueOf(10));
              j.put("shot_speed", Integer.valueOf(10));
          case VULCAN:
             //VulcanEntity vu = (VulcanEntity)entity;
              j.put("shot_radius", Integer.valueOf(10));
              j.put("shot_range", Integer.valueOf(10));
              j.put("shot_speed", Integer.valueOf(10));
      }

      return j;
   }

   public static String parseWeapons(Collection weapons, HashMap wwds) {
      JSONObject obj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var5 = weapons.iterator();

      while(var5.hasNext()) {
         IEntity entity = (IEntity)var5.next();
         JSONObject weapon = new JSONObject();
         WeaponWeakeningData wwd = (WeaponWeakeningData)wwds.get(entity.getShotData().id);
         weapon.put("auto_aiming_down", Double.valueOf(entity.getShotData().autoAimingAngleDown));
         weapon.put("auto_aiming_up", Double.valueOf(entity.getShotData().autoAimingAngleUp));
         weapon.put("num_rays_down", Integer.valueOf(entity.getShotData().numRaysDown));
         weapon.put("num_rays_up", Integer.valueOf(entity.getShotData().numRaysUp));
         weapon.put("reload", Integer.valueOf(entity.getShotData().reloadMsec));
         weapon.put("id", entity.getShotData().id);
         if(wwd != null) {
            weapon.put("max_damage_radius", Double.valueOf(wwd.maximumDamageRadius));
            weapon.put("min_damage_radius", Double.valueOf(wwd.minimumDamageRadius));
            weapon.put("min_damage_percent", Double.valueOf(wwd.minimumDamagePercent));
            weapon.put("has_wwd", Boolean.valueOf(true));
         } else {
            weapon.put("has_wwd", Boolean.valueOf(false));
         }

         weapon.put("special_entity", parseSpecialEntity(entity));
         array.add(weapon);
      }

      obj.put("weapons", array);
      return obj.toJSONString();
   }

   public static String parseTankSpec(Tank tank, boolean notSmooth) {
      JSONObject obj = new JSONObject();
      obj.put("speed", Float.valueOf(tank.speed));
      obj.put("turnSpeed", Float.valueOf(tank.turnSpeed));
      obj.put("turretRotationSpeed", Float.valueOf(tank.turretRotationSpeed));
      obj.put("immediate", Boolean.valueOf(notSmooth));
      return obj.toString();
   }

   public static String boolToString(boolean src) {
      return src?"true":"false";
   }
}
