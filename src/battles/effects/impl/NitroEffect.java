package battles.effects.impl;

import battles.BattlefieldPlayerController;
import battles.effects.Effect;
import battles.effects.EffectType;
import battles.effects.activator.EffectActivatorService;
import battles.tanks.math.Vector3;
import commands.Type;
import json.JSONUtils;
import services.annotations.ServicesInject;
import java.util.ArrayList;
import java.util.TimerTask;

public class NitroEffect extends TimerTask implements Effect {
   private static final String CHANGE_TANK_SPEC_COMAND = "change_spec_tank";
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
      ArrayList var4 = player.tank.activeEffects;
      synchronized(player.tank.activeEffects) {
         player.tank.activeEffects.add(this);
      }

      player.tank.speed = this.addPercent(player.tank.speed, 30);
      player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"change_spec_tank", player.tank.id, JSONUtils.parseTankSpec(player.tank, true)});
      this.effectActivatorService.activateEffect(this, (long)this.getDurationTime());
   }

   public void deactivate() {
      this.deactivated = true;
      this.player.tank.activeEffects.remove(this);
      this.player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"disnable_effect", this.player.getUser().getNickname(), String.valueOf(this.getID())});
      this.player.tank.speed = this.player.tank.getHull().maxForwardSpeed;
      this.player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"change_spec_tank", this.player.tank.id, JSONUtils.parseTankSpec(this.player.tank, true)});
   }

   public void run() {
      if(!this.deactivated) {
         this.deactivate();
      }

   }

   public EffectType getEffectType() {
      return EffectType.NITRO;
   }

   public int getID() {
      return 4;
   }

   private float addPercent(float value, int percent) {
      return value / 100.0F * (float)percent + value;
   }

   public int getDurationTime() {
      return 30000;
   }
}
