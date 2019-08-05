package users.garage.items;

import utils.StringUtils;
import dumpers.Dumper;
import system.localization.Localization;
import system.localization.strings.LocalizedString;
import users.garage.enums.ItemType;
import users.garage.items.modification.ModificationInfo;

public class Item implements Dumper {
   public String id;
   public LocalizedString description;
   public boolean isInventory = false;
   public int index;
   public PropertyItem[] propetys;
   public ItemType itemType = ItemType.PLUGIN;
   public int modificationIndex;
   public int mod = 0;
   public LocalizedString name;
   public PropertyItem[] nextProperty;
   public int nextPrice;
   public int nextRankId;
   public int price;
   public long time = 0;
   public int rankId;
   public ModificationInfo[] modifications;
   public boolean specialItem;
   public int count;
   public Boolean XT;
   public String nXT;
   public String dXT;
   public Boolean rot;
   public Boolean mx;

   public Item(String id, LocalizedString description, boolean isInventory, int index, PropertyItem[] propetys, ItemType weapon, int modificationIndex, LocalizedString name, PropertyItem[] nextProperty, int nextPrice, int nextRankId, int price, int rankId, ModificationInfo[] modifications, boolean specialItem, int count, int mod, boolean er,String nx,String dx,Boolean xh,Boolean xho) {
      this.id = id;
      this.description = description;
      this.isInventory = isInventory;
      this.index = index;
      this.propetys = propetys;
      this.itemType = weapon;
      this.modificationIndex = modificationIndex;
      this.name = name;
      this.nextProperty = nextProperty;
      this.nextPrice = nextPrice;
      this.nextRankId = nextRankId;
      this.price = price;
      this.rankId = rankId;
      this.modifications = modifications;
      this.specialItem = specialItem;
      this.count = count;
      this.mod = mod;
      this.XT = er;
      this.nXT = nx;
      this.dXT = dx;
      this.rot = xh;
      this.mx = xho;
      if(id.equals("double_crystalls"))
      {
         this.time = System.currentTimeMillis() + (86400 * 1000);
      }
      if(id.equals("pro_battle"))
      {
         this.time = System.currentTimeMillis() + (2592000L * 1000);
      }
      if(id.equals("pro_battle_on_day"))
      {
         this.time = System.currentTimeMillis() + (86400 * 1000);
      }
   }

   public String getId() {
      return StringUtils.concatStrings(new String[]{this.id, "_m", String.valueOf(this.modificationIndex)});
   }

   public Item clone() {
      return new Item(this.id, this.description, this.isInventory, this.index, this.propetys, this.itemType, this.modificationIndex, this.name, this.nextProperty, this.nextPrice, this.nextRankId, this.price, this.rankId, this.modifications, this.specialItem, this.count, this.mod,this.XT,this.nXT,this.dXT,this.rot,this.mx);
   }

   public String dump() {
      return StringUtils.concatStrings(new String[]{"-------DUMP GARAGE ITEM------\n", "\tid: ", this.id, "\n", "\tinventoryItem: ", String.valueOf(this.isInventory), "\n", "\tindex: ", String.valueOf(this.index), "\n", "\tname: ", this.name.localizatedString(Localization.RU), "\n", "\tprice: ", String.valueOf(this.price), "\n", "\trandId: ", String.valueOf(this.rankId), "\n", "\tspecialItem: ", String.valueOf(this.specialItem), "\n", "-------------------------------", "\n"});
   }
}
