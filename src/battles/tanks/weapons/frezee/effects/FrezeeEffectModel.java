package battles.tanks.weapons.frezee.effects;

import battles.BattlefieldModel;
import battles.tanks.Tank;
import battles.tanks.weapons.effects.IEffect;
import commands.Type;
import json.JSONUtils;

public class FrezeeEffectModel implements IEffect {
   private static final float MIN_VALUE = 0.4F;
   public float speed;
   public float turnSpeed;
   public float turretRotationSpeed;
   private float power;
   private Tank tank;
   private BattlefieldModel bfModel;
   private FrezeeEffectModel.FrezeeTimer currFrezeeTimer;

   public FrezeeEffectModel(float power, Tank tank, BattlefieldModel bfModel) {
      this.power = power;
      this.tank = tank;
      this.bfModel = bfModel;
   }

   public void setStartSpecFromTank() {
      this.speed = this.tank.speed;
      this.turnSpeed = this.tank.turnSpeed;
      this.turretRotationSpeed = this.tank.turretRotationSpeed;
   }

   public void update() {
      this.tank.speed -= this.power * this.speed / 100.0F * this.power;
      this.tank.turnSpeed -= this.power * this.turnSpeed / 100.0F * this.power;
      this.tank.turretRotationSpeed -= this.power * this.turretRotationSpeed / 100.0F * this.power;
      if(this.tank.speed < 0.4F) {
         this.tank.speed = 0.4F;
      }

      if(this.tank.turnSpeed < 0.4F) {
         this.tank.turnSpeed = 0.4F;
      }

      if(this.tank.turretRotationSpeed < 0.4F) {
         this.tank.turretRotationSpeed = 0.4F;
      }

      if(this.currFrezeeTimer != null) {
         this.currFrezeeTimer.stoped = true;
      }

      this.currFrezeeTimer = new FrezeeEffectModel.FrezeeTimer();
      this.currFrezeeTimer.start();
      this.sendSpecData();
      this.sendChangeTemperature(TemperatureCalc.getTemperature(this.tank, this.speed, this.turnSpeed, this.turretRotationSpeed));
   }

   private void sendSpecData() {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_spec_tank", this.tank.id, JSONUtils.parseTankSpec(this.tank, false)});
   }

   private void sendChangeTemperature(double value) {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_temperature_tank", this.tank.id, String.valueOf(value)});
   }

   class FrezeeTimer extends Thread {
      public boolean stoped = false;

      public void run() {
         this.setName("FREZEE TIMER THREAD " + FrezeeEffectModel.this.tank);

         try {
            sleep(3500L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         if(!this.stoped) {
            FrezeeEffectModel.this.tank.speed = FrezeeEffectModel.this.speed;
            FrezeeEffectModel.this.tank.turnSpeed = FrezeeEffectModel.this.turnSpeed;
            FrezeeEffectModel.this.tank.turretRotationSpeed = FrezeeEffectModel.this.turretRotationSpeed;
            FrezeeEffectModel.this.sendSpecData();
            FrezeeEffectModel.this.sendChangeTemperature(0.0D);
         }
      }
   }
}
