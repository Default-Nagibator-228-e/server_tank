package battles.mines.activator;

import battles.BattlefieldModel;
import battles.mines.ServerMine;
import commands.Type;
import json.JSONUtils;
import java.util.TimerTask;

public class MineActivator extends TimerTask {
   private static final String PUT_MINE_COMMAND = "put_mine";
   private static final String ACTIVATE_MINE_COMMAND = "activate_mine";
   private BattlefieldModel bfModel;
   private ServerMine mine;

   public MineActivator(BattlefieldModel bfModel, ServerMine mine) {
      this.bfModel = bfModel;
      this.mine = mine;
   }

   public void putMine() {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"put_mine", JSONUtils.parsePutMineComand(this.mine)});
   }

   public void activateMine() {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"activate_mine", this.mine.getId()});
   }

   public void run() {
      this.activateMine();
   }
}
