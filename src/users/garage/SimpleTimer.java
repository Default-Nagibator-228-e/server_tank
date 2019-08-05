package users.garage;

import main.database.impl.DatabaseManagerImpl;
import users.garage.items.Item;

/** @deprecated */
@Deprecated

public class SimpleTimer implements Runnable {
   private Item r;
   private Garage g;

   public SimpleTimer(Garage gar, Item it) {
      g = gar;
      r = it;
      start1();
   }

   private void start1() {
      (new Thread(this)).start();
   }

   public void run() {
      try {
         Thread.sleep(10000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      if(System.currentTimeMillis() >= r.time) {
         //Iterator<Item> var3 = g.items.iterator();

         //while(var3.hasNext()) {
            //Item item = var3.next();
            //if(item.id.equals(r.id)) {
               g.items.remove(r);
               g.parseJSONData();
               DatabaseManagerImpl.instance().update(g);
            //}
         //}
      }
   }
}
