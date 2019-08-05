package services;

import utils.RankUtils;
import commands.Type;
import lobby.LobbyManager;
import logger.remote.RemoteDatabaseLogger;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import users.User;

public class TanksServices {
   private static TanksServices instance = new TanksServices();
   private static final int MAX_UPDATE_PROGRESS_NUMBER = 10000;
   @ServicesInject(
      target = DatabaseManagerImpl.class
   )
   private final DatabaseManager database = DatabaseManagerImpl.instance();

   public static TanksServices getInstance() {
      return instance;
   }

   public void addScore(LobbyManager lobby, int score) {
      if(lobby == null) {
         RemoteDatabaseLogger.error("TanksServices::addScore: lobby null!");
      } else {
         User user = lobby.getLocalUser();
         if(user == null) {
            RemoteDatabaseLogger.error("TanksServices::addScore: user null!");
         } else {
            user.addScore(score);
            boolean increase = user.getScore() >= user.getNextScore();
            boolean fall = user.getScore() < RankUtils.getRankByIndex(user.getRang()).min;
            if(increase || fall) {
               user.setRang(RankUtils.getNumberRank(RankUtils.getRankByScore(user.getScore())));
               user.setNextScore(user.getRang() == 29?RankUtils.getRankByIndex(user.getRang()).max:RankUtils.getRankByIndex(user.getRang()).max + 1);
               lobby.send(Type.LOBBY, new String[]{"update_rang_progress", String.valueOf(10000)});
               lobby.send(Type.LOBBY, new String[]{"update_rang", String.valueOf(user.getRang() + 1), String.valueOf(user.getNextScore())});
            }

            int update = RankUtils.getUpdateNumber(user.getScore());
            lobby.send(Type.LOBBY, new String[]{"update_rang_progress", String.valueOf(update)});
            lobby.send(Type.LOBBY, new String[]{"add_score", String.valueOf(user.getScore())});
            this.database.update(user);
         }
      }
   }

   public void addZv(LobbyManager lobby, int zv) {
      if(lobby != null) {
         User user = lobby.getLocalUser();
         if(user != null) {
            user.zv += user.bp?zv*2:zv;
            lobby.parseChalleng();
            this.database.update(user);
         }
      }
   }

   public void addCrystall(LobbyManager lobby, int crystall) {
      if(lobby == null) {
         RemoteDatabaseLogger.error("TanksServices::addCrystall: lobby null!");
      } else {
         User user = lobby.getLocalUser();
         if(user == null) {
            RemoteDatabaseLogger.error("TanksServices::addCrystall: user null!");
         } else {
            user.addCrystall(crystall);
            lobby.send(Type.LOBBY, new String[]{"add_crystall", String.valueOf(user.getCrystall())});
            this.database.update(user);
         }
      }
   }

   public void dummyAddCrystall(LobbyManager lobby, int crystall) {
      if(lobby == null) {
         RemoteDatabaseLogger.error("TanksServices::dummyAddCrystall: lobby null!");
      } else {
         User user = lobby.getLocalUser();
         if(user == null) {
            RemoteDatabaseLogger.error("TanksServices::dummyAddCrystall: user null!");
         } else {
            lobby.send(Type.LOBBY, new String[]{"add_crystall", String.valueOf(user.getCrystall())});
         }
      }
   }
}
