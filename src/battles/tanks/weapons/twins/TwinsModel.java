package battles.tanks.weapons.twins;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.WeaponUtils;
import battles.tanks.weapons.WeaponWeakeningData;
import battles.tanks.weapons.anticheats.FireableWeaponAnticheatModel;
import commands.Type;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.RandomUtils;

@AnticheatModel(
   name = "TwinsModel",
   actionInfo = "Child FireableWeaponAnticheatModel"
)
public class TwinsModel extends FireableWeaponAnticheatModel implements IWeapon {
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private WeaponWeakeningData weakeingData;
   private TwinsEntity entity;

   public TwinsModel(TwinsEntity twinsEntity, WeaponWeakeningData weakeingData, BattlefieldPlayerController tank, BattlefieldModel battle) {
      super(twinsEntity.getShotData().reloadMsec);
      this.bfModel = battle;
      this.player = tank;
      this.entity = twinsEntity;
      this.weakeingData = weakeingData;
   }

   public void startFire(String json) {
      this.bfModel.sendToAllPlayers(this.player, Type.BATTLE, new String[]{"start_fire_twins", this.player.tank.id, json});
   }

   public void fire(String json) {
      this.bfModel.fire(this.player, json);

      try {
         JSONObject e = (JSONObject)(new JSONParser()).parse(json);
         //if(!this.check((int)((Long)e.get("reloadTime")).longValue())) {
            //this.bfModel.cheatDetected(this.player, this.getClass());
            //return;
         //}

         BattlefieldPlayerController victim = this.bfModel.getPlayer((String)e.get("victimId"));
         this.onTarget(new BattlefieldPlayerController[]{victim}, (int)((Long)e.get("distance")).longValue());
      } catch (ParseException var4) {
         var4.printStackTrace();
      }

   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_max);
      if(targetsTanks.length != 0) {
         if(targetsTanks[0] != null) {
            if((double)distance >= this.weakeingData.minimumDamageRadius) {
               damage = WeaponUtils.calculateDamageFromDistance(damage, (int)this.weakeingData.minimumDamagePercent);
            }

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
