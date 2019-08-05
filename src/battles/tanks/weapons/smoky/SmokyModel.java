package battles.tanks.weapons.smoky;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.WeaponUtils;
import battles.tanks.weapons.WeaponWeakeningData;
import battles.tanks.weapons.anticheats.FireableWeaponAnticheatModel;
import commands.Type;
import logger.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@AnticheatModel(
   name = "SmokyModel",
   actionInfo = "Child FireableWeaponAnticheatModel"
)
public class SmokyModel extends FireableWeaponAnticheatModel implements IWeapon {
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private SmokyEntity entity;
   private WeaponWeakeningData weakeingData;

   public SmokyModel(SmokyEntity entity, WeaponWeakeningData weakeingData, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      super(entity.getShotData().reloadMsec);
      this.entity = entity;
      this.bfModel = bfModel;
      this.player = player;
      this.weakeingData = weakeingData;
      this.player.per = this.entity.per;
   }

   public void fire(String json) {
      JSONParser js = new JSONParser();
      JSONObject jo = null;

      try {
         jo = (JSONObject)js.parse(json);
         this.bfModel.fire(this.player, json);
         BattlefieldPlayerController victim = (BattlefieldPlayerController)this.bfModel.players.get(jo.get("victimId"));
         if(victim != null) {
            this.onTarget(new BattlefieldPlayerController[]{victim}, (int)Double.parseDouble(String.valueOf(jo.get("distance"))));
         }
         if(this.player.per < 101) {
            this.player.per += this.entity.aper;
         }
      } catch (ParseException var5) {
         System.out.println("Не успел прочитать данные о выстреле смоки");
      }
   }

   public void startFire(String json) {
   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      if(targetsTanks.length != 0) {
         if(targetsTanks.length > 1) {
            Logger.log("SmokyModel::onTarget() Warning! targetsTanks length = " + targetsTanks.length);
         }

         float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_max);
         if((double)distance >= this.weakeingData.minimumDamageRadius) {
            damage = WeaponUtils.calculateDamageFromDistance(damage, (int)this.weakeingData.minimumDamagePercent);
         }
         Boolean df = RandomUtils.getRandomper(this.player.per);
         this.bfModel.tanksKillModel.damageTank(targetsTanks[0], this.player, df ? this.entity.dper:damage, true,df);
         if(df)
         {
            this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"krit", targetsTanks[0].tank.id});
         }
      }
   }

   public IEntity getEntity() {
      return this.entity;
   }

   public void stopFire() {
   }
}
