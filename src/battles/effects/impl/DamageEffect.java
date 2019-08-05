package battles.effects.impl;

import battles.BattlefieldPlayerController;
import battles.effects.Effect;
import battles.effects.EffectType;
import battles.effects.activator.EffectActivatorService;
import battles.tanks.math.Vector3;
import commands.Type;
import services.annotations.ServicesInject;
import java.util.TimerTask;

public class DamageEffect extends TimerTask implements Effect {
   private static final long INVENTORY_TIME_ACTION = 60000L;
   private static final long DROP_TIME_ACTION = 40000L;
   @ServicesInject(
      target = EffectActivatorService.class
   )
   private EffectActivatorService effectActivatorService = EffectActivatorService.getInstance();
   private BattlefieldPlayerController player;
   private boolean fromInventory;
   private boolean deactivated;

   public void activate(BattlefieldPlayerController player, boolean fromInventory, Vector3 tankPos) {
      this.fromInventory = fromInventory;
      this.player = player;
      player.tank.activeEffects.add(this);
      this.effectActivatorService.activateEffect(this, (long)this.getDurationTime());
   }

   public void deactivate() {
      this.deactivated = true;
      this.player.tank.activeEffects.remove(this);
      this.player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"disnable_effect", this.player.getUser().getNickname(), String.valueOf(this.getID())});
   }

   public void run() {
      if(!this.deactivated) {
         this.deactivate();
      }

   }

   public EffectType getEffectType() {
      return EffectType.DAMAGE;
   }

   public int getID() {
      return 3;
   }

   public int getDurationTime() {
      return 30000;
   }
}
