package battles.maps;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;

@AnticheatModel(
   name = "MapChecksumModel",
   actionInfo = "Проверяет чек-суму(md5) карты на клиенте"
)
public class MapChecksumModel {
   private BattlefieldModel bfModel;

   public MapChecksumModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public void check(BattlefieldPlayerController player, String hashSum) {
      this.bfModel.battleInfo.map.md5Hash.equals(hashSum);
   }
}
