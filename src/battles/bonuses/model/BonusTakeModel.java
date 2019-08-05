package battles.bonuses.model;

import battles.effects.EffectType;
import battles.effects.impl.NitroEffect;
import battles.effects.impl.HealthEffect;
import battles.effects.impl.DamageEffect;
import battles.effects.Effect;
import battles.effects.impl.ArmorEffect;
import commands.Type;
import battles.BattlefieldPlayerController;
import battles.tanks.math.Vector3;
import battles.bonuses.Bonus;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import main.database.DatabaseManager;
import battles.BattlefieldModel;

public class BonusTakeModel
{
   private BattlefieldModel bfModel;
   @ServicesInject(target = DatabaseManagerImpl.class)
   private DatabaseManager database;

   public BonusTakeModel(final BattlefieldModel bfModel) {
      this.database = DatabaseManagerImpl.instance();
      this.bfModel = bfModel;
   }

   public boolean onTakeBonus(final Bonus bonus, final Vector3 realtimePosTank, final BattlefieldPlayerController player) {
      switch (bonus.type) {
         case CRYSTALL: {
            player.parentLobby.getLocalUser().addCrystall(1);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case CRYSTALL5: {
            player.parentLobby.getLocalUser().addCrystall(5);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case CRYSTALL10: {
            player.parentLobby.getLocalUser().addCrystall(10);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case CRYSTALL20: {
            player.parentLobby.getLocalUser().addCrystall(20);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case CRYSTALL50: {
            player.parentLobby.getLocalUser().addCrystall(50);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case CRYSTALL100: {
            player.parentLobby.getLocalUser().addCrystall(100);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case GOLD: {
            this.bfModel.sendUserLogMessage(player.parentLobby.getLocalUser().getNickname(), "взял золотой ящик");
            player.parentLobby.getLocalUser().addCrystall(1000);
            player.send(Type.BATTLE, new String[] { "set_cry", String.valueOf(player.parentLobby.getLocalUser().getCrystall()) });
            this.database.update(player.getUser());
            break;
         }
         case ARMOR: {
            this.activateDrop(new ArmorEffect(), player);
            break;
         }
         case DAMAGE: {
            this.activateDrop(new DamageEffect(), player);
            break;
         }
         case HEALTH: {
            this.activateDrop(new HealthEffect(), player);
            break;
         }
         case NITRO: {
            this.activateDrop(new NitroEffect(), player);
            break;
         }
      }
      return true;
   }

   private void activateDrop(final Effect effect, final BattlefieldPlayerController player) {
      if (!player.tank.isUsedEffect(effect.getEffectType())) {
         String var = "";
         if(effect.getEffectType() == EffectType.HEALTH)
         {
            var = "health";
         }
         if(effect.getEffectType() == EffectType.ARMOR)
         {
            var = "armor";
         }
         if(effect.getEffectType() == EffectType.DAMAGE)
         {
            var = "double_damage";
         }
         if(effect.getEffectType() == EffectType.NITRO)
         {
            var = "n2o";
         }
         effect.activate(player, false, player.tank.position);
         player.send(commands.Type.BATTLE, new String[]{"activate_itemb", var});
         player.battle.sendToAllPlayers(Type.BATTLE, new String[] { "enable_effect", player.getUser().getNickname(), String.valueOf(effect.getID()), (effect.getEffectType() == EffectType.HEALTH) ? String.valueOf(10000) : String.valueOf(40000) });
      }
   }
}