package services;

import utils.StringUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.statistic.PlayerStatistic;
import collections.FastHashMap;
import commands.Type;
import json.JSONUtils;
import lobby.LobbyManager;
import lobby.battles.BattleInfo;
import lobby.chat.ChatLobby;
import services.annotations.ServicesInject;
import system.quartz.QuartzService;
import system.quartz.TimeType;
import system.quartz.impl.QuartzServiceImpl;
import users.User;
import users.locations.UserLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoEntryServices {
   private static final AutoEntryServices instance = new AutoEntryServices();
   private static final String QUARTZ_NAME = "AutoEntryServices GC";
   private static final String QUARTZ_GROUP = "runner";
   @ServicesInject(
      target = QuartzService.class
   )
   private QuartzService quartzService = QuartzServiceImpl.inject();
   @ServicesInject(
      target = ChatLobby.class
   )
   private ChatLobby chatLobby = ChatLobby.getInstance();
   @ServicesInject(
      target = LobbysServices.class
   )
   private LobbysServices lobbysServices = LobbysServices.getInstance();
   public FastHashMap playersForAutoEntry = new FastHashMap();

   private AutoEntryServices() {
      this.quartzService.addJobInterval("AutoEntryServices GC", "runner", (e) -> {
         long currentTime = System.currentTimeMillis();
         Iterator var5 = this.playersForAutoEntry.values().iterator();

         while(var5.hasNext()) {
            AutoEntryServices.Data data = (AutoEntryServices.Data)var5.next();
            if(currentTime - data.createdTime >= 120000L) {
               this.removePlayer(data.battle, data.userId, data.teamType, data.battle.battleInfo.team);
            }
         }

      }, TimeType.SEC, 30L);
   }

   public void removePlayer(String userId) {
      this.playersForAutoEntry.remove(userId);
   }

   public boolean removePlayer(BattlefieldModel data, String userId, String teamType, boolean team) {
      if(this.playersForAutoEntry.get(userId) == null) {
         return false;
      } else {
         this.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[]{"remove_player_from_battle", JSONUtils.parseRemovePlayerComand(userId, data.battleInfo.battleId)});
         if(!team) {
            --data.battleInfo.countPeople;
            this.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[]{StringUtils.concatStrings(new String[]{"update_count_users_in_dm_battle", ";", data.battleInfo.battleId, ";", String.valueOf(data.battleInfo.countPeople)})});
         } else {
            if(teamType.equals("RED")) {
               --data.battleInfo.redPeople;
            } else {
               --data.battleInfo.bluePeople;
            }

            this.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[]{"update_count_users_in_team_battle", JSONUtils.parseUpdateCoundPeoplesCommand(data.battleInfo)});
         }

         this.playersForAutoEntry.remove(userId);
         return true;
      }
   }

   public void prepareToEnter(LobbyManager lobby) {
      AutoEntryServices.Data data = (AutoEntryServices.Data)this.playersForAutoEntry.get(lobby.getLocalUser().getNickname());
      if(data == null) {
         this.transmitToLobby(lobby);
      } else {
         BattlefieldModel bModel = data.battle;
         if(bModel == null) {
            this.transmitToLobby(lobby);
         } else {
            this.removePlayer(lobby.getLocalUser().getNickname());
            PlayerStatistic statistic = data.statistic;
            BattleInfo battleInfo = bModel.battleInfo;
            lobby.getLocalUser().setUserLocation(UserLocation.BATTLE);
            lobby.battle = new BattlefieldPlayerController(lobby, bModel, data.teamType);
            lobby.battle.statistic = statistic;
            lobby.disconnectListener.addListener(lobby.battle);
            lobby.getLocalUser().idbat = battleInfo.battleId;
            LobbysServices.getInstance().chCommandToAllUsers(Type.LOBBY,"addon", lobby.getLocalUser().getNickname());
            lobby.send(Type.BATTLE, new String[]{"init_battle_model", JSONUtils.parseBattleModelInfo(battleInfo, false,lobby)});
         }
      }
   }

   private void transmitToLobby(LobbyManager lobby) {
      lobby.sendGarage();
      lobby.send(Type.LOBBY_CHAT, new String[]{"init_chat"});
      lobby.send(Type.LOBBY_CHAT, new String[]{"init_messages", JSONUtils.parseChatLobbyMessages(this.chatLobby.getMessages())});
   }

   public boolean needEnterToBattle(User user) {
      return this.playersForAutoEntry.get(user.getNickname()) != null;
   }

   public void userExit(BattlefieldPlayerController player) {
      AutoEntryServices.Data data = new AutoEntryServices.Data();
      data.battle = player.battle;
      data.statistic = player.statistic;
      data.createdTime = System.currentTimeMillis();
      data.teamType = player.playerTeamType;
      data.userId = player.getUser().getNickname();
      this.playersForAutoEntry.put(player.getUser().getNickname(), data);
   }

   public List getPlayersByBattle(BattlefieldModel battle) {
      ArrayList players = new ArrayList();
      Iterator var4 = this.playersForAutoEntry.values().iterator();

      while(var4.hasNext()) {
         AutoEntryServices.Data data = (AutoEntryServices.Data)var4.next();
         if(data.battle != null && data.battle == battle) {
            players.add(data);
         }
      }

      return players;
   }

   public void battleRestarted(BattlefieldModel battle) {
      Iterator var3 = this.playersForAutoEntry.values().iterator();

      while(var3.hasNext()) {
         AutoEntryServices.Data data = (AutoEntryServices.Data)var3.next();
         if(data.battle != null && data.battle == battle) {
            data.statistic.clear();
         }
      }

   }

   public void battleDisposed(BattlefieldModel battle) {
      Iterator var3 = this.playersForAutoEntry.values().iterator();

      while(var3.hasNext()) {
         AutoEntryServices.Data data = (AutoEntryServices.Data)var3.next();
         if(data.battle != null && data.battle == battle) {
            this.playersForAutoEntry.remove(data.userId);
         }
      }

   }

   public static AutoEntryServices instance() {
      return instance;
   }

   public class Data {
      public BattlefieldModel battle;
      public PlayerStatistic statistic;
      public String teamType;
      public long createdTime;
      public String userId;
   }
}
