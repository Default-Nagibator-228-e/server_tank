package battles.tanks.weapons.flamethrower;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.anticheats.TickableWeaponAnticheatModel;
import battles.tanks.weapons.flamethrower.effects.FlamethrowerEffectModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FlamethrowerModel extends TickableWeaponAnticheatModel implements IWeapon {
   private FlamethrowerEntity entity;
   public BattlefieldModel bfModel;
   public BattlefieldPlayerController player;

   public FlamethrowerModel(FlamethrowerEntity entity, BattlefieldModel bfModel, BattlefieldPlayerController player) {
      super(entity.targetDetectionInterval);
      this.entity = entity;
      this.bfModel = bfModel;
      this.player = player;
   }

   public void startFire(String json) {
      this.bfModel.startFire(this.player);
   }

   public void stopFire() {
      this.bfModel.stopFire(this.player);
   }

   public void fire(String json) {
      try {
         JSONObject e = (JSONObject)(new JSONParser()).parse(json);
         JSONArray arrayTanks = (JSONArray)e.get("targetsIds");
        // if(!this.check((int)((Long)e.get("tickPeriod")).longValue())) {
            //this.bfModel.cheatDetected(this.player, this.getClass());
            //return;
         //}

         if(arrayTanks.size() == 0) {
            return;
         }

         BattlefieldPlayerController[] targetVictim = new BattlefieldPlayerController[arrayTanks.size()];

         for(int i = 0; i < arrayTanks.size(); ++i) {
            BattlefieldPlayerController target = this.bfModel.getPlayer((String)arrayTanks.get(i));
            if(target != null && (float)((int)(target.tank.position.distanceTo(this.player.tank.position) / 100.0D)) <= this.entity.range) {
               targetVictim[i] = target;
            }
         }

         this.onTarget(targetVictim, 0);
      } catch (ParseException var7) {
         var7.printStackTrace();
      }

   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      BattlefieldPlayerController[] var6 = targetsTanks;
      int var5 = targetsTanks.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         BattlefieldPlayerController victim = var6[var4];
         this.bfModel.tanksKillModel.damageTank(victim, this.player, this.entity.damage_max, true,false);
         boolean canFlame = true;
         if(this.bfModel.battleInfo.team) {
            try {
               canFlame = !this.player.playerTeamType.equals(((BattlefieldPlayerController)bfModel.players.get(victim.getUser().getNickname())).playerTeamType);
            }catch(NullPointerException n){

            }
         }

         if(canFlame) {
            try {
               victim.tank.flameEffect.setStartSpecFromTank();
            }catch (NullPointerException e)
            {
               try {
                  victim.tank.flameEffect = new FlamethrowerEffectModel(this.entity.coolingSpeed, victim.tank, this.bfModel,this.entity.damage_max,victim, this.player) ;
                  victim.tank.flameEffect.setStartSpecFromTank();
                  victim.tank.flameEffect.update();
               }catch (NullPointerException e1)
               {
               }
            }
         }
      }

   }

   public IEntity getEntity() {
      return this.entity;
   }
}
