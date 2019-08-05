package users.karma;

import java.util.ArrayList;

/** @deprecated */
@Deprecated
public class UserKarma {
   private ArrayList karmaActions = new ArrayList();
   private boolean chatBanned = false;
   private boolean bannedInGame = false;

   public void addKarmaAction(UserKarmaAction action) {
      this.karmaActions.add(action);
   }

   public boolean isChatBanned() {
      return this.chatBanned;
   }

   public void setChatBanned(boolean chatBanned) {
      this.chatBanned = chatBanned;
   }

   public boolean isBannedInGame() {
      return this.bannedInGame;
   }

   public void setBannedInGame(boolean bannedInGame) {
      this.bannedInGame = bannedInGame;
   }
}
