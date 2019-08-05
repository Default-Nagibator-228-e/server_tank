package battles.tanks.weapons.isida;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.anticheats.TickableWeaponAnticheatModel;
import commands.Type;
import services.TanksServices;
import services.annotations.ServicesInject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IsidaModel extends TickableWeaponAnticheatModel implements IWeapon {
   @ServicesInject(
      target = TanksServices.class
   )
   private static TanksServices tanksServices = TanksServices.getInstance();
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController player;
   private IsidaEntity entity;
   private double accumulatedPointsForHealing = 0.0D;
   private static final double HEALER_POINTS_KOEFF = 90.0D;

   public IsidaModel(IsidaEntity entity, BattlefieldPlayerController player, BattlefieldModel bfModel) {
      super(entity.tickPeriod);
      this.bfModel = bfModel;
      this.player = player;
      this.entity = entity;
   }

   public void startFire(String json) {
      JSONObject obj = new JSONObject();
      JSONObject parser = null;

      try {
         parser = (JSONObject)(new JSONParser()).parse(json);
      } catch (ParseException var7) {
         var7.printStackTrace();
      }

      String shotType = "";
      String victimId = (String)parser.get("victimId");
      BattlefieldPlayerController victim = this.bfModel.getPlayer(victimId);
      if(victimId != null && !victimId.isEmpty()) {
         if(this.bfModel.battleInfo.team && this.player.playerTeamType.equals(victim.playerTeamType)) {
            shotType = "heal";
         } else {
            shotType = "damage";
         }
      } else {
         shotType = "idle";
      }

      obj.put("type", shotType);
      obj.put("shooterId", this.player.getUser().getNickname());
      obj.put("targetId", victimId);
      this.bfModel.sendToAllPlayers(this.player, Type.BATTLE, new String[]{"start_fire", this.player.getUser().getNickname(), obj.toJSONString()});
   }

   public void fire(String json) {
      JSONObject parser = null;

      try {
         parser = (JSONObject)(new JSONParser()).parse(json);
      } catch (ParseException var5) {
         var5.printStackTrace();
      }

      //this.check((int)((Long)parser.get("tickPeriod")).longValue());
      String victimId = (String)parser.get("victimId");
      if(victimId != null && !victimId.isEmpty()) {
         BattlefieldPlayerController target = this.bfModel.getPlayer(victimId);
         if(target != null) {
            if((float)((int)(target.tank.position.distanceTo(this.player.tank.position) / 100.0D)) <= this.entity.maxRadius) {
               this.onTarget(new BattlefieldPlayerController[]{target}, (int)((Long)parser.get("distance")).longValue());
            }
         }
      }
   }

   public void stopFire() {
      this.bfModel.stopFire(this.player);
      this.calculateHealedScore();
   }

   private void calculateHealedScore() {
      if(this.accumulatedPointsForHealing > 0.0D) {
         tanksServices.addScore(this.player.parentLobby, (int)this.accumulatedPointsForHealing);
      }

      this.player.statistic.addScore((int)this.accumulatedPointsForHealing);
      this.bfModel.statistics.changeStatistic(this.player);
      this.accumulatedPointsForHealing = 0.0D;
   }

   private void addScoreForHealing(float healedPoint, BattlefieldPlayerController patient) {
      int patientRating = patient.getUser().getRang();
      int healerRating = this.player.getUser().getRang();
      double scorePoints = Math.atan((double)((patientRating - healerRating) / (healerRating + 1) + 1)) / 3.141592653589793D * 90.0D * (double)healedPoint / 100.0D;
      this.accumulatedPointsForHealing += scorePoints;
   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      //if(distance != 1500) {
         //this.bfModel.cheatDetected(this.player, this.getClass());
      //}

      float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_min) / 2.0F;
      if(this.bfModel.battleInfo.team && this.player.playerTeamType.equals(targetsTanks[0].playerTeamType)) {
         if(this.bfModel.tanksKillModel.healPlayer(this.player, targetsTanks[0], damage)) {
            this.addScoreForHealing(damage, targetsTanks[0]);
         }
      } else {
         this.bfModel.tanksKillModel.damageTank(targetsTanks[0], this.player, damage, true,false);
         this.bfModel.tanksKillModel.healPlayer(this.player, this.player, damage / 2.0F);
      }

   }

   public IEntity getEntity() {
      return this.entity;
   }
}
