package lobby.battles;

import utils.StringUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import commands.Type;
import json.JSONUtils;
import lobby.LobbyManager;
import services.LobbysServices;
import services.annotations.ServicesInject;
import users.locations.UserLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BattlesList extends BattlesListComandsConst {
   private static ArrayList battles = new ArrayList();
   private static int countBattles = 0;
   @ServicesInject(
      target = LobbysServices.class
   )
   private static LobbysServices lobbysServices = LobbysServices.getInstance();

   public static boolean tryCreateBatle(BattleInfo btl, LobbyManager lo) {
      btl.battleId = generateId(btl.name, btl.map.id);
      if(getBattleInfoById(btl.battleId) != null) {
         return false;
      } else {
         battles.add(btl);
         ++countBattles;
         if(!btl.isPrivate) {
            lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, "create_battle", JSONUtils.parseBattleInfo(btl));
         }else{
            lo.send(Type.LOBBY, "create_battle", JSONUtils.parseBattleInfo(btl));
         }
         BattlefieldModel model = new BattlefieldModel(btl);
         btl.model = model;
         return true;
      }
   }

   public static void removeBattle(BattleInfo battle) {
      if(battle != null) {
         lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, StringUtils.concatStrings(new String[]{"remove_battle", ";", battle.battleId}));
         if(battle.model != null && battle.model.players != null) {
            Iterator var2 = battle.model.players.values().iterator();

            while(var2.hasNext()) {
               BattlefieldPlayerController player = (BattlefieldPlayerController)var2.next();
               player.parentLobby.kick();
            }
         }

         battle.model.destroy();
         battles.remove(battle);
      }
   }

   public static ArrayList getList() {
      return battles;
   }

   private static String generateId(String gameName, String mapId) {
      String id = (new Random()).nextInt('썐') + "@" + gameName + "@" + "#" + countBattles;
      return id;
   }

   public static BattleInfo getBattleInfoById(String id) {
      Iterator var2 = battles.iterator();

      while(var2.hasNext()) {
         BattleInfo battle = (BattleInfo)var2.next();
         if(battle.battleId.equals(id)) {
            return battle;
         }
      }

      return null;
   }
}
