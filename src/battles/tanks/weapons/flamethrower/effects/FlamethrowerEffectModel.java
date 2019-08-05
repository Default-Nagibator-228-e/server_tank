package battles.tanks.weapons.flamethrower.effects;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.Tank;
import battles.tanks.weapons.effects.IEffect;
import commands.Type;

import java.util.Timer;
import java.util.TimerTask;

public class FlamethrowerEffectModel implements IEffect {
   private static final float MIN_VALUE = 0.4F;
   private float power;
   private Double emc = 0.0;
   private float fg;
   private Tank tank;
   private BattlefieldModel bfModel;
   private BattlefieldPlayerController bfModel1;
   private BattlefieldPlayerController bfModel2;
   private FlamethrowerEffectModel.FlamethrowerTimer currFlameTimer = new FlamethrowerEffectModel.FlamethrowerTimer();
   private Timer t = new Timer();

   public FlamethrowerEffectModel(float power, Tank tank, BattlefieldModel bfModel, float fg, BattlefieldPlayerController bfModel1, BattlefieldPlayerController bfModel2) {
      this.power = power;
      this.tank = tank;
      this.bfModel = bfModel;
      this.bfModel1 = bfModel1;
      this.bfModel2 = bfModel2;
      this.fg = fg;
   }

   public void setStartSpecFromTank() {

   }

   public void update() {
      emc += 0.2;
      if(emc <= 0.1) {
         t = null;
         this.currFlameTimer = null;
         t = new Timer();
         this.currFlameTimer = new FlamethrowerTimer();
         t.schedule(this.currFlameTimer,0,1000);
      }
      this.sendChangeTemperature(TemperatureCalc.getTemperature(emc));
   }

   private void sendChangeTemperature(double value) {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"change_temperature_tank", this.tank.id, String.valueOf(value)});
   }

   class FlamethrowerTimer extends TimerTask {

      public void run() {
         emc -= 0.03;
         try {
            bfModel.tanksKillModel.damageTank(bfModel1, bfModel2, fg * 0.1f, true,false);
            FlamethrowerEffectModel.this.sendChangeTemperature(emc);
            if (emc <= 0.0) {
               this.cancel();
               //this.stop();
            }
         }catch (NullPointerException e)
         {
            this.cancel();
         }
      }
   }
}
