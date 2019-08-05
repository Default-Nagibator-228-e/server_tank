package system.dailybonus;

import users.garage.items.Item;

public class BonusListItem {
   private Item bonus;
   private int count;

   public BonusListItem() {
   }

   public BonusListItem(Item bonus, int count) {
      this.bonus = bonus;
      this.count = count;
   }

   public Item getBonus() {
      return this.bonus;
   }

   public void setBonus(Item bonus) {
      this.bonus = bonus;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public void addCount(int count) {
      this.count += count;
   }
}
