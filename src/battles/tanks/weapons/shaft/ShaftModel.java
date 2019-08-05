package battles.tanks.weapons.shaft;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.WeaponWeakeningData;
import logger.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ShaftModel implements IWeapon {
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private ShaftEntity entity;
   private WeaponWeakeningData weakeingData;

   public ShaftModel(ShaftEntity entity, WeaponWeakeningData weakeingData, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      this.entity = entity;
      this.bfModel = bfModel;
      this.player = player;
      this.weakeingData = weakeingData;
   }

   public void fire(String json) {
      JSONParser js = new JSONParser();
      JSONObject jo = null;

      try {
         jo = (JSONObject)js.parse(json);
      } catch (ParseException var5) {
         var5.printStackTrace();
      }

      //if(!this.check((int)((Long)jo.get("reloadTime")).longValue())) {
         //this.bfModel.cheatDetected(this.player, this.getClass());
      //} else {
         this.bfModel.fire(this.player, json);
         BattlefieldPlayerController victim = (BattlefieldPlayerController)this.bfModel.players.get(jo.get("victimId"));
         if(victim != null) {
            this.onTarget1(new BattlefieldPlayerController[]{victim}, (int)Double.parseDouble(String.valueOf(jo.get("distance"))),Double.parseDouble(jo.get("rel") + ""));
         }
      //}
   }

   public void startFire(String json) {
   }

   public void stopFire() {
   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {

   }

   public void onTarget1(BattlefieldPlayerController[] targetsTanks, int distance, Double rel) {
      if(targetsTanks.length != 0) {
         if(targetsTanks.length > 1) {
            Logger.log("SmokyModel::onTarget() Warning! targetsTanks length = " + targetsTanks.length);
         }

         float damage = this.entity.damage_min + Float.parseFloat((this.entity.damage_max * rel) + "");

         this.bfModel.tanksKillModel.damageTank(targetsTanks[0], this.player, damage, true,false);
      }
   }

   public IEntity getEntity() {
      return this.entity;
   }
}
