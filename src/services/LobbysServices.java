package services;

import collections.FastHashMap;
import commands.Command;
import commands.Type;
import lobby.LobbyManager;
import users.User;
import users.locations.UserLocation;
import java.util.Iterator;

public class LobbysServices {
   private static LobbysServices instance = new LobbysServices();
   public FastHashMap lobbys = new FastHashMap();

   public static LobbysServices getInstance() {
      return instance;
   }

   public void addLobby(LobbyManager lobby) {
      this.lobbys.put(lobby.getLocalUser().getNickname(), lobby);
   }

   public void removeLobby(LobbyManager lobby) {
      this.lobbys.remove(lobby.getLocalUser(), lobby);
   }

   public boolean containsLobby(LobbyManager lobby) {
      return this.lobbys.containsKey(lobby.getLocalUser());
   }

   public LobbyManager getLobbyByUser(User user) {
      return (LobbyManager)this.lobbys.get(user.getNickname());
   }

   public LobbyManager getLobbyByNick(String nick) {
      LobbyManager lobbyManager = null;
      Iterator var4 = this.lobbys.iterator();

      while(var4.hasNext()) {
         LobbyManager lobby = (LobbyManager)var4.next();
         if(lobby.getLocalUser().getNickname().equals(nick)) {
            lobbyManager = lobby;
            break;
         }
      }

      return lobbyManager;
   }

   public void sendCommandToAllUsers(Type type, UserLocation onlyFor, String... args) {
      try {
         Iterator var5 = this.lobbys.iterator();

         while(var5.hasNext()) {
            LobbyManager ex = (LobbyManager)var5.next();
            if (ex != null) {
               if (onlyFor == UserLocation.ALL) {
                  ex.send(type, args);
               } else {
                  if (ex.getLocalUser().getUserLocation() == onlyFor) {
                     ex.send(type, args);
                  }
               }
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public void chCommandToAllUsers(Type type, String... args) {
      try {
         Iterator var5 = this.lobbys.iterator();

         while(var5.hasNext()) {
            LobbyManager ex = (LobbyManager)var5.next();
            if(ex != null) {
               ex.executeCommand(new Command(type, args));
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public void sendCommandToAllUsersBesides(Type type, UserLocation besides, String... args) {
      try {
         Iterator var5 = this.lobbys.iterator();

         while(var5.hasNext()) {
            LobbyManager ex = (LobbyManager)var5.next();
            if(ex != null && ex.getLocalUser().getUserLocation() != besides) {
               ex.send(type, args);
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
