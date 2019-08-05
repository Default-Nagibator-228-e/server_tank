package battles.effects.model;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.effects.Effect;
import battles.spectator.SpectatorController;
import commands.Type;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class EffectsVisualizationModel {
   private BattlefieldModel bfModel;

   public EffectsVisualizationModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public void sendInitData(BattlefieldPlayerController player) {
      JSONObject _obj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var5 = this.bfModel.players.values().iterator();

      while(true) {
         BattlefieldPlayerController _player;
         do {
            if(!var5.hasNext()) {
               _obj.put("effects", array);
               player.send(Type.BATTLE, new String[]{"init_effects", _obj.toJSONString()});
               return;
            }

            _player = (BattlefieldPlayerController)var5.next();
         } while(player == _player);

         ArrayList var6 = _player.tank.activeEffects;
         synchronized(_player.tank.activeEffects) {
            Iterator var8 = _player.tank.activeEffects.iterator();

            while(var8.hasNext()) {
               Effect effect = (Effect)var8.next();
               JSONObject obj = new JSONObject();
               obj.put("userID", _player.getUser().getNickname());
               obj.put("itemIndex", Integer.valueOf(effect.getID()));
               obj.put("durationTime", Integer.valueOf('\uea60'));
               array.add(obj);
            }
         }
      }
   }

   public void sendInitData(SpectatorController player) {
      JSONObject _obj = new JSONObject();
      JSONArray array = new JSONArray();
      Iterator var5 = this.bfModel.players.values().iterator();

      while(var5.hasNext()) {
         BattlefieldPlayerController _player = (BattlefieldPlayerController)var5.next();
         ArrayList var6 = _player.tank.activeEffects;
         synchronized(_player.tank.activeEffects) {
            Iterator var8 = _player.tank.activeEffects.iterator();

            while(var8.hasNext()) {
               Effect effect = (Effect)var8.next();
               JSONObject obj = new JSONObject();
               obj.put("userID", _player.getUser().getNickname());
               obj.put("itemIndex", Integer.valueOf(effect.getID()));
               obj.put("durationTime", Integer.valueOf('\uea60'));
               array.add(obj);
            }
         }
      }

      _obj.put("effects", array);
      player.sendCommand(Type.BATTLE, new String[]{"init_effects", _obj.toJSONString()});
   }
}
