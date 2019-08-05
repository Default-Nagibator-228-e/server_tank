package lobby.top;

import logger.Logger;
import users.User;
import java.util.ArrayList;
import java.util.Collection;

public class HallOfFame {
   private static HallOfFame instance = new HallOfFame();
   private ArrayList top = new ArrayList(100);

   public static HallOfFame getInstance() {
      return instance;
   }

   public void addUser(User user) {
      Logger.log("User " + user.getNickname() + " has been added to top. " + (this.top.add(user)?"DONE":"ERROR"));
   }

   public void removeUser(User user) {
      Logger.log("User " + user.getNickname() + " has been removed of top. " + (this.top.remove(user)?"DONE":"ERROR"));
   }

   public void initHallFromCollection(Collection collection) {
      this.top = new ArrayList(collection);
   }

   public ArrayList getData() {
      return this.top;
   }
}
