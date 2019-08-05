package battles.tanks.weapons.flamethrower.effects;

public class TemperatureCalc {

   public static double getTemperature(double currState) {
      double temperature = currState;
      if(temperature > 0.5D) {
         temperature = 0.5D;
      }

      if(temperature < 0.0D) {
         temperature = 0.0D;
      }

      return temperature;
   }
}
