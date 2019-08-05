package lobby.chat;

import lobby.LobbyManager;
import users.User;

public class ChatMessage {
   public User user;
   public String message;
   public boolean addressed;
   public boolean system;
   public User userTo;
   public LobbyManager localLobby;
   public boolean yellowMessage;

   public ChatMessage(User user, String message, boolean addressed, User userTo, boolean yellowMessage, LobbyManager localLobby) {
      this.user = user;
      this.message = message;
      this.addressed = addressed;
      this.userTo = userTo;
      this.yellowMessage = yellowMessage;
      this.localLobby = localLobby;
   }

   public ChatMessage(User user, String message, boolean addressed, User userTo, LobbyManager localLobby) {
      this.user = user;
      this.message = message;
      this.addressed = addressed;
      this.userTo = userTo;
      this.localLobby = localLobby;
   }

   public String toString() {
      return (this.system?"SYSTEM: ":this.user.getNickname() + ": ") + (this.addressed?"->" + this.userTo.getNickname():"") + this.message;
   }
}
