package system.dailybonus.crystalls;

import lobby.LobbyManager;
import services.TanksServices;
import services.annotations.ServicesInject;

public class CrystallsBonusModel {
   @ServicesInject(
      target = TanksServices.class
   )
   private static final TanksServices tanksServices = TanksServices.getInstance();
   private static final int[] CRYSTALLS = new int[]{0, 0, 150, 250, 350, 500, 600, 750, 850, 950, 1100, 1200, 1350, 1450, 1550, 1700, 1800, 1950, 2050, 2150, 2300, 2400, 2550, 2650, 2750, 2900, 3000, 3200, 3400, 3600};

   public void applyBonus(LobbyManager lobby) {
      int bonus = this.getBonus(lobby.getLocalUser().getRang());
      tanksServices.addCrystall(lobby, bonus);
   }

   public int getBonus(int rangIndex) {
      return CRYSTALLS[rangIndex];
   }
}
