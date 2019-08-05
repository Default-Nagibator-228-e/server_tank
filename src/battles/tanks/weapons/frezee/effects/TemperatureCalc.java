package battles.tanks.weapons.frezee.effects;

import battles.tanks.Tank;

public class TemperatureCalc {
   private static final double MIN_TEMPERATURE = -2.0D;

   public static double getTemperature(Tank currState, float speed, float turnSpeed, float turretRotationSpeed) {
      double temperature_speed = (double)((speed - currState.speed) * (speed / 30.0F));
      double temperature_turn = (double)((turnSpeed - currState.turnSpeed) * (turnSpeed / 10.0F));
      double temperature_turret = (double)((turretRotationSpeed - currState.turretRotationSpeed) * (turretRotationSpeed / 10.0F));
      double temperature = -(temperature_speed + temperature_turn + temperature_turret);
      if(temperature < -2.0D) {
         temperature = -2.0D;
      }

      if(temperature > 0.0D) {
         temperature = 0.0D;
      }

      return temperature;
   }
}
