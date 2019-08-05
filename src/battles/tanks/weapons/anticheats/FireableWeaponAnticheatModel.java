package battles.tanks.weapons.anticheats;

import battles.anticheats.AnticheatModel;

@AnticheatModel(
   name = "FireableWeaponAnticheatModel",
   actionInfo = "Сравнивает время перезарядки таких пушек как: Смоки, Твинс, Рельса, Гром, Рикошет"
)
public class FireableWeaponAnticheatModel {
   private int normalReloadTime;

   public FireableWeaponAnticheatModel(int normalReloadTime) {
      this.normalReloadTime = normalReloadTime;
   }

   public boolean check(int reloadTimeFromClient) {
      return this.normalReloadTime == reloadTimeFromClient;
   }
}
