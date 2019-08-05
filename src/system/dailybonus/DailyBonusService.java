package system.dailybonus;

import lobby.LobbyManager;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import system.dailybonus.crystalls.CrystallsBonusModel;
import system.dailybonus.ui.DailyBonusUIModel;
import users.User;
import users.garage.Garage;
import users.garage.GarageItemsLoader;
import users.garage.items.Item;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DailyBonusService {
   private static final DailyBonusService instance = new DailyBonusService();
   @ServicesInject(
      target = DatabaseManager.class
   )
   private static final DatabaseManager databaseManager = DatabaseManagerImpl.instance();
   public static final String[] SUPPLIES_IDS = new String[]{"armor", "double_damage", "n2o"};
   private static Map waitingUsers = new HashMap();
   private static DailyBonusUIModel uiModel = new DailyBonusUIModel();
   private static CrystallsBonusModel crystallsBonus = new CrystallsBonusModel();
   private static Random random = new Random();

   public static DailyBonusService instance() {
      return instance;
   }

   public void userInited(LobbyManager lobby) {
      User user = lobby.getLocalUser();
      if(user.getRang() + 1 > 2 && this.canGetBonus(user)) {
         int fund = (int)(((double)(user.getRang() + 1) - 1.75D) * 2.4D) * 5;
         if(fund > 0) {
            DailyBonusService.Data bonusData = new DailyBonusService.Data();
            List bonusList = bonusData.bonusList;
            int rankFirstAid = (GarageItemsLoader.items.get("health")).rankId;
            int itemCrystalPrice = (GarageItemsLoader.items.get("health")).price;
            int countFirstAid = fund / itemCrystalPrice / 2;
            itemCrystalPrice = (GarageItemsLoader.items.get("mine")).price;
            int countMine = fund / itemCrystalPrice / 2;
            int rankMine = (GarageItemsLoader.items.get("mine")).rankId;
            if((double)random.nextFloat() < 0.1D) {
               bonusData.type = 1;
            } else {
               bonusData.type = 3;
               int garage;
               int item;
               Item bonus;
               int bonusItem;
               if((double)random.nextFloat() < 0.6D) {
                  bonusData.type = 4;
                  bonusList.add(new BonusListItem(GarageItemsLoader.items.get("double_crystalls").clone(), 1));
                  waitingUsers.put(lobby, bonusData);
                  Garage garage1 = user.getGarage();

                  BonusListItem item1;
                  Item bonusItem1;
                  for(Iterator bonus1 = bonusList.iterator(); bonus1.hasNext(); bonusItem1.count += item1.getCount()) {
                     item1 = (BonusListItem)bonus1.next();
                     bonusItem1 = garage1.getItemById(item1.getBonus().id);
                     if(bonusItem1 == null) {
                        bonusItem1 = (GarageItemsLoader.items.get(item1.getBonus().id)).clone();
                        garage1.items.add(bonusItem1);
                     }
                  }

                  garage1.parseJSONData();
                  databaseManager.update(garage1);
                  return;
               } else if((double)random.nextFloat() < 0.7D) {
                  bonusData.type = 5;
                  bonusList.add(new BonusListItem(GarageItemsLoader.items.get("pro_battle").clone(), 1));
                  waitingUsers.put(lobby, bonusData);
                  Garage garage1 = user.getGarage();

                  BonusListItem item1;
                  Item bonusItem1;
                  for(Iterator bonus1 = bonusList.iterator(); bonus1.hasNext(); bonusItem1.count += item1.getCount()) {
                     item1 = (BonusListItem)bonus1.next();
                     bonusItem1 = garage1.getItemById(item1.getBonus().id);
                     if(bonusItem1 == null) {
                        bonusItem1 = (GarageItemsLoader.items.get(item1.getBonus().id)).clone();
                        garage1.items.add(bonusItem1);
                     }
                  }

                  garage1.parseJSONData();
                  databaseManager.update(garage1);
                  return;
               } else if((double)random.nextFloat() < 0.3D && countFirstAid > 0 && user.getRang() >= rankFirstAid) {
                  bonus = GarageItemsLoader.items.get("health");
                  item = bonus.price;
                  garage = fund / item / 2 + 1;
               } else if((double)random.nextFloat() < 0.3D && countMine > 0 && user.getRang() >= rankMine) {
                  bonus = GarageItemsLoader.items.get("mine");
                  item = bonus.price;
                  garage = fund / item / 2 + 1;
               } else {
                  bonusItem = random.nextInt(3);
                  bonus = GarageItemsLoader.items.get(SUPPLIES_IDS[bonusItem]);
                  item = bonus.price;
                  garage = fund / item / 2;
               }

               bonusList.add(new BonusListItem(bonus, garage));
               fund -= item * garage;
               bonusItem = random.nextInt(3);
               bonus = GarageItemsLoader.items.get(SUPPLIES_IDS[bonusItem]);
               item = bonus.price;
               if(((BonusListItem)bonusList.get(0)).getBonus().equals(bonus)) {
                  ((BonusListItem)bonusList.get(0)).addCount(fund / item);
               } else {
                  bonusList.add(new BonusListItem(bonus, fund / item));
               }
            }

            waitingUsers.put(lobby, bonusData);
            Garage garage1 = user.getGarage();

            BonusListItem item1;
            Item bonusItem1;
            for(Iterator bonus1 = bonusList.iterator(); bonus1.hasNext(); bonusItem1.count += item1.getCount()) {
               item1 = (BonusListItem)bonus1.next();
               bonusItem1 = garage1.getItemById(item1.getBonus().id);
               if(bonusItem1 == null) {
                  bonusItem1 = (GarageItemsLoader.items.get(item1.getBonus().id)).clone();
                  garage1.items.add(bonusItem1);
               }
            }

            garage1.parseJSONData();
            databaseManager.update(garage1);
         }
      }

   }

   public void userLoaded(LobbyManager lobby) {
      DailyBonusService.Data data = (DailyBonusService.Data)waitingUsers.get(lobby);
      if(data != null) {
         if(data.type == 1) {
            crystallsBonus.applyBonus(lobby);
            uiModel.showCrystalls(lobby, crystallsBonus.getBonus(lobby.getLocalUser().getRang()));
         } else if(data.type == 3) {
            uiModel.showBonuses(lobby, data.bonusList);
         }else if(data.type == 4) {
            uiModel.showBonuses(lobby, data.bonusList);
         }else if(data.type == 5) {
            uiModel.showBonuses(lobby, data.bonusList);
         }

         waitingUsers.remove(lobby);
         this.saveLastDate(lobby.getLocalUser());
      }
   }

   public boolean canGetBonus(User user) {
      if(user == null) {
         return false;
      } else {
         boolean result = false;
         Date lastDate = user.getLastIssueBonus();
         Date now = new Date(System.currentTimeMillis() - 14400000L);
         Calendar nowCal = Calendar.getInstance();
         nowCal.setTime(now);
         Calendar lastCal = Calendar.getInstance();
         if(lastDate != null) {
            lastCal.setTime(lastDate);
         }

         if(lastDate == null || nowCal.get(5) > lastCal.get(5) || nowCal.get(2) > lastCal.get(2)) {
            result = true;
         }

         return result;
      }
   }

   private void saveLastDate(User user) {
      Date now = new Date(System.currentTimeMillis() - 14400000L);
      user.setLastIssueBonus(now);
      databaseManager.update(user);
   }

   private class Data {
      public int type = 0;
      public List bonusList = new ArrayList();
   }
}
