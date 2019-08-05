package battles.tanks.weapons.railgun;

import utils.RandomUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.anticheats.AnticheatModel;
import battles.tanks.weapons.IEntity;
import battles.tanks.weapons.IWeapon;
import battles.tanks.weapons.anticheats.FireableWeaponAnticheatModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@AnticheatModel(
   name = "RailgunModel",
   actionInfo = "Child FireableWeaponAnticheatModel"
)
public class RailgunModel extends FireableWeaponAnticheatModel implements IWeapon {
   private RailgunEntity entity;
   private BattlefieldModel battle;
   private BattlefieldPlayerController tank;

   public RailgunModel(RailgunEntity entity, BattlefieldPlayerController tank, BattlefieldModel battle) {
      super(entity.getShotData().reloadMsec);
      this.entity = entity;
      this.battle = battle;
      this.tank = tank;
   }

   public void startFire(String json) {
      this.battle.startFire(this.tank);
   }

   public void fire(String json_info) {
      this.battle.fire(this.tank, json_info);

      try {
         JSONParser e = new JSONParser();
         JSONObject json = (JSONObject)e.parse(json_info);
         JSONArray tanks = (JSONArray)json.get("targets");
        // if(!this.check((int)((Long)json.get("reloadTime")).longValue())) {
            //this.battle.cheatDetected(this.tank, this.getClass());
            //return;
        // }

         if(tanks == null) {
            return;
         }

         BattlefieldPlayerController[] tanks_array = new BattlefieldPlayerController[tanks.size()];

         for(int i = 0; i < tanks.size(); ++i) {
            tanks_array[i] = (BattlefieldPlayerController)this.battle.players.get((String)tanks.get(i));
         }

         this.onTarget(tanks_array, 0);
      } catch (ParseException var7) {
         var7.printStackTrace();
      }

   }

   public void onTarget(BattlefieldPlayerController[] targetsTanks, int distance) {
      if(targetsTanks.length != 0) {
         float damage = RandomUtils.getRandom(this.entity.damage_min, this.entity.damage_max);

         for(int i = 0; i < targetsTanks.length; ++i) {
            this.battle.tanksKillModel.damageTank(targetsTanks[i], this.tank, damage, true,false);
            damage /= 2.0F;
         }

      }
   }

   public IEntity getEntity() {
      return this.entity;
   }

   public void stopFire() {
   }
}
