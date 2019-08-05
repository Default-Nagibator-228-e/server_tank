package battles.effects.impl;

import battles.effects.EffectType;
import battles.tanks.weapons.WeaponUtils;
import commands.Type;
import battles.tanks.math.Vector3;
import battles.BattlefieldPlayerController;
import services.annotations.ServicesInject;
import battles.effects.activator.EffectActivatorService;
import battles.effects.Effect;

public class HealthEffect extends Thread implements Effect
{
   private int resource;
   private int resource1;
   @ServicesInject(target = EffectActivatorService.class)
   private EffectActivatorService effectActivatorService;
   private BattlefieldPlayerController player;
   private boolean fromInventory;
   private boolean deactivated;
   private int res = 10000;

   public HealthEffect() {
      this.effectActivatorService = EffectActivatorService.getInstance();
   }

   public void activate(final BattlefieldPlayerController player, final boolean fromInventory, final Vector3 tankPos) {
      this.fromInventory = fromInventory;
      this.player = player;
      this.resource = WeaponUtils.calculateHealth(player.tank, 150);
      this.resource1 = WeaponUtils.calculateHealth(player.tank, 1000);
      player.tank.activeEffects.add(this);
      this.start();
   }

   public void deactivate() {
      try {
         this.deactivated = true;
         this.player.tank.activeEffects.remove(this);
         this.player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"disnable_effect", this.player.getUser().getNickname(), String.valueOf(this.getID())});
      }catch (Exception e){}
   }

   @Override
   public void run() {
      try {
            res = 10000;
            if(player.tank.health == 10000)
            {
               Thread.sleep(2000L);
               if (!this.deactivated) {
                  this.deactivate();
               }
            }else{
               if (player.tank.health + this.resource1 >= 10000) {
                  int d = 10000 - player.tank.health;
                  this.healTank(d,1000);
                  if (!this.deactivated) {
                     this.deactivate();
                  }
               }else{
                  this.healTank(this.resource1,1000);
               }
            }
            try {
               while (player.tank.health < 10000 && res > 0) {
                  if (player.tank.health + this.resource >= 10000) {
                     int d = 10000 - player.tank.health;
                     this.healTank(d, 50);
                     if (!this.deactivated) {
                        this.deactivate();
                     }
                  } else {
                     this.healTank(this.resource, 150);
                     Thread.sleep(250L);
                  }
               }
            }catch (NullPointerException n){}
            if (!this.deactivated) {
               this.deactivate();
            }
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   private void healTank(final int hp,int ds) {
         player.send(Type.BATTLE, new String[]{"hp", player.tank.id, String.valueOf(ds), String.valueOf(2)});
         player.tank.health += hp;
         res -= hp;
         player.battle.sendToAllPlayers(Type.BATTLE, new String[]{"change_health", player.tank.id, String.valueOf(player.tank.health)});
   }

   public EffectType getEffectType() {
      return EffectType.HEALTH;
   }

   public int getID() {
      return 1;
   }

   public int getDurationTime() {
      return 5000;
   }
}