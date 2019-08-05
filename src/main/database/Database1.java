package main.database;

import lobby.top.HallOfFame;
import logger.Logger;
import services.annotations.ServicesInject;
import users.TypeUser;
import users.User;
import users.garage.GarageItemsLoader;
import users.garage.items.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/** @deprecated */
@Deprecated
public class Database1 {
   private static Database1 instance = new Database1();
   private HashMap users = new HashMap();
   private int temp = 0;
   @ServicesInject(
      target = HallOfFame.class
   )
   private HallOfFame top = HallOfFame.getInstance();
   private ArrayList moders = new ArrayList() {
      {
         this.add("Хуй тебе а не лопасть");
      }
   };

   public static Database1 getInstance() {
      return instance;
   }

   private Database1() {
      Iterator var2 = this.moders.iterator();

      while(var2.hasNext()) {
         String nickname = (String)var2.next();
         //this.registerUser(new User(nickname, !nickname.equals("Golubev")?"999adminsonly666":"ff19afeb0d521c323285c58380f967a4"));
      }

   }

   public synchronized void registerUser(User user) {
      if(!this.users.containsKey(user.getNickname())) {
         Logger.log("User [" + user.getNickname() + ": " + user.getPassword() + "] has been registered");
         this.users.put(user.getNickname(), user);
         user.setPlace(this.temp);
         if(this.moders.contains(user.getNickname())) {
            user.setType(TypeUser.ADMIN);
         }

         user.addCrystall(400000);
         user.setRang(16);
         user.setScore(250000);
         user.setNextScore(280000);
         this.top.addUser(user);
         ++this.temp;
         if(user.getNickname().equals("Golubev")) {
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("besthelper")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("champion")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("helios")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("phenix")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("vlastelin")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("tot")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("spectator")).clone());
         }

         if(user.getType() == TypeUser.ADMIN) {
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("besthelper")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("champion")).clone());
            user.getGarage().items.add(((Item)GarageItemsLoader.items.get("vlastelin")).clone());
         }
      }

   }

   public boolean containsUserInDataBase(User user) {
      return this.users.containsValue(user);
   }

   public boolean containsUserInDataBase(String nickname) {
      return this.users.containsKey(nickname);
   }

   public User getUserByNameFromDataBase(String nickname) {
      return (User)this.users.get(nickname);
   }

   public int getTotalUsers() {
      return this.users.size();
   }
}
