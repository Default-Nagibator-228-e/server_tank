package battles.tanks.weapons.frezee;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.anticheats.TickableWeaponAnticheatModel;
import battles.tanks.weapons.frezee.effects.FrezeeEffectModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FrezeeModel extends TickableWeaponAnticheatModel implements IWeapon {
   private FrezeeEntity entity;
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;

   public FrezeeModel(FrezeeEntity entity, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      super(entity.weaponTickMsec);
      this.entity = entity;
      this.bfModel = bfModel;
      this.player = player;
   }

   public void fire(String json) {
      JSONArray victims = null;
      JSONArray distances = null;

      try {
         JSONObject i = (JSONObject)(new JSONParser()).parse(json);
         victims = (JSONArray)i.get("victims");
         distances = (JSONArray)i.get("targetDistances");
         //if(!this.check((int)((Long)i.get("tickPeriod")).longValue())) {
            //this.bfModel.cheatDetected(this.player, this.getClass());
            //return;
         //}
      } catch (ParseException var8) {
         var8.printStackTrace();
      }

      for(int var9 = 0; var9 < victims.size(); ++var9) {
         String victimId = (String)victims.get(var9);
         int distance = 0;//this.getValueByObject(distances.get(var9));
         BattlefieldPlayerController victim = this.bfModel.getPlayer(victimId);
         if(victim != null && (float)((int)(victim.tank.position.distanceTo(this.player.tank.position) / 100.0D)) <= this.entity.damageAreaRange) {
            this.onTarget(new BattlefieldPlayerController[]{this.bfModel.getPlayer(victimId)}, distance);
         }
      }
   }

   public void startFire(String json) {
      this.bfModel.startFire(this.player);
   }

   public void stopFire() {
      this.bfModel.stopFire(this.player);
   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_min) / 2.0F;
      if((float)distance <= this.entity.damageAreaRange) {
         this.bfModel.tanksKillModel.damageTank(targetsTanks[0], this.player, damage, true,false);
         BattlefieldPlayerController victim = targetsTanks[0];
         if(victim != null && victim.tank != null) {
            boolean canFrezee = true;
            if(this.bfModel.battleInfo.team) {
               canFrezee = !this.player.playerTeamType.equals(victim.playerTeamType);
            }

            if(canFrezee) {
               try {
                  victim.tank.frezeeEffect.setStartSpecFromTank();
               }catch (NullPointerException e)
               {
                  victim.tank.frezeeEffect = new FrezeeEffectModel(this.entity.coolingSpeed, victim.tank, this.bfModel) ;
                  victim.tank.frezeeEffect.setStartSpecFromTank();
               }

               victim.tank.frezeeEffect.update();
            }

         }
      }
   }

   public IEntity getEntity() {
      return this.entity;
   }

   private int getValueByObject(Object obj) {
      try {
         return (int)Double.parseDouble(String.valueOf(obj));
      } catch (Exception var3) {
         return Integer.parseInt(String.valueOf(obj));
      }
   }
}
