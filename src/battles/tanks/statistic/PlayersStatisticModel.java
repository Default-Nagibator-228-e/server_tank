package battles.tanks.statistic;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import commands.Type;
import json.JSONUtils;

public class PlayersStatisticModel {
   private BattlefieldModel bfModel;

   public PlayersStatisticModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public void changeStatistic(BattlefieldPlayerController player) {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"update_player_statistic", JSONUtils.parsePlayerStatistic(player)});
   }
}
