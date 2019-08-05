package users.garage.items.kit;

public class KitItem {
   private KitItemType type;
   private String itemId;
   private int count;
   private int modif;

   public KitItem(KitItemType type, String itemId, int count, int modif) {
      this.type = type;
      this.itemId = itemId;
      this.count = count;
      this.modif = modif;
   }

   public KitItemType getType() {
      return this.type;
   }

   public void setType(KitItemType type) {
      this.type = type;
   }

   public String getItemId() {
      return this.itemId;
   }

   public int getmodif() {
      return this.modif;
   }

   public void setItemId(String itemId) {
      this.itemId = itemId;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }
}
