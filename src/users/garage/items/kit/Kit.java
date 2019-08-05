package users.garage.items.kit;

import java.util.List;

public class Kit {
   private List kitItems;
   private int price;

   public Kit(List kitItems, int price) {
      this.kitItems = kitItems;
      this.setPrice(price);
   }

   public List getKitItems() {
      return this.kitItems;
   }

   public int getPrice() {
      return this.price;
   }

   public void setPrice(int price) {
      this.price = price;
   }
}
