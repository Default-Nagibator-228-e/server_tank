package battles.tanks.statistic.prizes;

import battles.BattlefieldPlayerController;
import services.TanksServices;
import services.annotations.ServicesInject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BattlePrizeCalculate {
   @ServicesInject(
           target = TanksServices.class
   )
   private static TanksServices tankServices = TanksServices.getInstance();

   public BattlePrizeCalculate() {
   }

   public static void calc(List<BattlefieldPlayerController> users, int fund, boolean comm) {
      if (users != null && users.size() != 0) {
            Collections.sort(users, new Comparator<BattlefieldPlayerController>() {
               public int compare(BattlefieldPlayerController o1, BattlefieldPlayerController o2) {
                  return (int) (o1.statistic.getScore() - o2.statistic.getScore());
               }
            });
         if (!comm) {
            if(users.size() > 2) {
               for (int fds = 0; fds < 3; fds++) {
                  BattlefieldPlayerController user1 = users.get(fds);
                  user1.statistic.setLPrize((int)((fund * 0.6) / (fds + 1)));
                  fds++;
               }
            }else{
               for (int fds = 0; fds < users.size(); fds++) {
                  BattlefieldPlayerController user1 = users.get(fds);
                  user1.statistic.setLPrize((int)((fund * 0.6) / (fds + 1)));
                  fds++;
               }
            }
         }
         BattlefieldPlayerController ok = users.get(0);
         Iterator var15 = users.iterator();
         while (var15.hasNext()) {
            BattlefieldPlayerController user = (BattlefieldPlayerController) var15.next();
            int prize = (int)((user.statistic.getKills() * 10) + (comm ? (fund/users.size()) : user.statistic.getLPrize()));
            int zv = (int) (3 * (user.statistic.getScore() == 0 ? 0:(ok.statistic.getScore() / user.statistic.getScore())));
            if (prize < 0) {
               prize = 0;
            }
            if (zv < 0) {
               zv = 0;
            }

            user.statistic.setPrize(prize);
            user.statistic.sz(user.parentLobby.getLocalUser().bp ? zv * 2 : zv);
            tankServices.addZv(user.parentLobby, zv);
            tankServices.addCrystall(user.parentLobby, prize);
         }
      }
   }

   public static void calculateForTeam(ArrayList<BattlefieldPlayerController> redUsers, ArrayList<BattlefieldPlayerController> blueUsers, int scoreRed, int scoreBlue, double looseKoeff, int fund) {
      ArrayList usersWin;
      ArrayList usersLoose;
      if (scoreRed != scoreBlue) {
         usersWin = scoreRed > scoreBlue ? redUsers : blueUsers;
         usersLoose = scoreRed > scoreBlue ? blueUsers : redUsers;
      } else {
         usersWin = redUsers;
         usersLoose = blueUsers;
      }

      calc(usersWin, fund,true);
      calc(usersLoose, 0,true);
   }
}
