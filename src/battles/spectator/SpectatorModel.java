package battles.spectator;

import battles.BattlefieldModel;
import battles.spectator.chat.SpectatorChatModel;
import commands.Type;
import system.BattlesGC;
import java.util.HashMap;
import java.util.Iterator;

public class SpectatorModel {
   private HashMap spectators;
   private BattlefieldModel bfModel;
   private SpectatorChatModel chatModel;

   public SpectatorModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
      this.spectators = new HashMap();
      this.chatModel = new SpectatorChatModel(this);
   }

   public void addSpectator(SpectatorController spec) {
      this.spectators.put(spec.getId(), spec);
      BattlesGC.cancelRemoving(this.bfModel);
   }

   public void removeSpectator(SpectatorController spec) {
      this.spectators.remove(spec.getId());
      if(this.bfModel != null) {
         if(this.bfModel.players != null) {
            if(this.bfModel.players.size() == 0 && this.spectators.size() == 0) {
               BattlesGC.addBattleForRemove(this.bfModel);
            }

         }
      }
   }

   public SpectatorChatModel getChatModel() {
      return this.chatModel;
   }

   public BattlefieldModel getBattleModel() {
      return this.bfModel;
   }

   public void sendCommandToSpectators(Type type, String... args) {
      Iterator var4 = this.spectators.values().iterator();

      while(var4.hasNext()) {
         SpectatorController sc = (SpectatorController)var4.next();
         sc.sendCommand(type, args);
      }

   }
}
