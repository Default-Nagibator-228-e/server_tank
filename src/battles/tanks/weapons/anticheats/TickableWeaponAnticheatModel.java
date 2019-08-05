package battles.tanks.weapons.anticheats;

import battles.anticheats.AnticheatModel;

@AnticheatModel(
   name = "TickableWeaponAnticheatModel",
   actionInfo = "Сравнивает период тиков таких пушек как: Огнёмет, Фриз, Изида"
)
public class TickableWeaponAnticheatModel {
   private int normalTickTime;

   public TickableWeaponAnticheatModel(int normalTickTime) {
      this.normalTickTime = normalTickTime;
   }

   public boolean check(int timeFromClient) {
      return this.normalTickTime == timeFromClient;
   }
}
