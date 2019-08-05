package users.garage.items.modification;

import users.garage.items.PropertyItem;

public class ModificationInfo {
   public String previewId;
   public int price;
   public int rank;
   public PropertyItem[] propertys;

   public ModificationInfo(String previewId, int price, int rank) {
      this.previewId = previewId;
      this.price = price;
      this.rank = rank;
      this.propertys = new PropertyItem[1];
   }
}
