package battles.tanks.weapons.annihilat;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.WeaponUtils;
import battles.tanks.weapons.WeaponWeakeningData;
import battles.tanks.weapons.anticheats.FireableWeaponAnticheatModel;
import battles.tanks.weapons.frezee.effects.FrezeeEffectModel;
import commands.Type;
import logger.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@AnticheatModel(
   name = "VulcanModel",
   actionInfo = "Child FireableWeaponAnticheatModel"
)
public class AnnihilatorModel extends FireableWeaponAnticheatModel implements IWeapon {
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private WeaponWeakeningData weakeingData;
   private AnnihilatorEntity entity;

   public AnnihilatorModel(AnnihilatorEntity annihilatorEntity, WeaponWeakeningData weakeingData, BattlefieldPlayerController tank, BattlefieldModel battle) {
      super(annihilatorEntity.getShotData().reloadMsec);
      this.bfModel = battle;
      this.player = tank;
      this.entity = annihilatorEntity;
      this.weakeingData = weakeingData;
   }

   public void startFire(String json) {
      this.bfModel.sendToAllPlayers(this.player, Type.BATTLE, new String[]{"start_fire_snowman", this.player.tank.id, json});
   }

   public void fire(String json) {
      this.bfModel.fire(this.player, json);

      try {
         JSONObject e = (JSONObject)(new JSONParser()).parse(json);
         if(!this.check((int)((Long)e.get("reloadTime")).longValue())) {
            //this.bfModel.cheatDetected(this.player, this.getClass());
            //return;
         }

         BattlefieldPlayerController victim = this.bfModel.getPlayer((String)e.get("victimId"));
         this.onTarget(new BattlefieldPlayerController[]{victim}, (int)((Long)e.get("distance")).longValue());
      } catch (Exception var4) {
         Logger.log("Error in parsing json VulcanModel().fire() Data: " + json);
      }

   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_max);
      if(targetsTanks.length != 0) {
         if(targetsTanks[0] != null) {
            if((double)distance >= this.weakeingData.minimumDamageRadius) {
               damage = WeaponUtils.calculateDamageFromDistance(damage, (int)this.weakeingData.minimumDamagePercent);
            }

            if(targetsTanks[0].tank.frezeeEffect == null) {
               targetsTanks[0].tank.frezeeEffect = new FrezeeEffectModel(this.entity.frezeeSpeed, targetsTanks[0].tank, this.bfModel);
               targetsTanks[0].tank.frezeeEffect.setStartSpecFromTank();
            }

            targetsTanks[0].tank.frezeeEffect.update();
            this.bfModel.tanksKillModel.damageTank(targetsTanks[0], this.player, damage, true,false);
         }
      }
   }

   public IEntity getEntity() {
      return this.entity;
   }

   public void stopFire() {
   }
}
