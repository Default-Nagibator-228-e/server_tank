package users.garage;

import utils.StringUtils;
import commands.Type;
import json.JSONUtils;
import lobby.LobbyManager;
import users.garage.items.kit.Kit;
import users.garage.items.kit.KitItem;
import users.garage.items.kit.loader.KitsLoader;
import users.garage.enums.ItemType;
import users.garage.items.Item;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Entity
@org.hibernate.annotations.Entity
@Table(
   name = "garages"
)
public class Garage implements Serializable {
   private static final long serialVersionUID = 2342422342L;
   @Id
   @GeneratedValue(
      strategy = GenerationType.IDENTITY
   )
   @Column(
      name = "uid",
      nullable = false,
      unique = true
   )
   private long id;
   @Column(
      name = "turrets",
      nullable = false
   )
   private String _json_turrets;
   @Column(
      name = "hulls",
      nullable = false
   )
   private String _json_hulls;
   @Column(
      name = "colormaps",
      nullable = false
   )
   private String _json_colormaps;
   @Column(
      name = "inventory",
      nullable = false
   )
   private String _json_inventory;
   @Column(
      name = "userid",
      nullable = false,
      unique = true
   )
   private String userId = "";
   @Column(
           name = "effects",
           nullable = false
   )
   private String _json_effects;
   @Transient
   public ArrayList<Item> items = new ArrayList<>();
   @Transient
   public Item mountTurret;
   @Transient
   public Item mountHull;
   @Transient
   public Item mountColormap;

   public Garage() {
      if(this.items.size() == 0) {
         this.items.add((GarageItemsLoader.items.get("smoky")).clone());
         this.items.add((GarageItemsLoader.items.get("hunter")).clone());
         this.items.add((GarageItemsLoader.items.get("green")).clone());
         this.items.add((GarageItemsLoader.items.get("holiday")).clone());
         Item var = (GarageItemsLoader.items.get("double_crystalls")).clone();
         this.items.add(var);
         Item var1 = (GarageItemsLoader.items.get("pro_battle")).clone();
         this.items.add(var1);
      }
      this.mountItem("hunter_m0");
      this.mountItem("smoky_m0");
      this.mountItem("green_m0");
   }

   public boolean containsItem(String id) {
      Iterator<Item> var3 = this.items.iterator();

      while(var3.hasNext()) {
         Item item = var3.next();
         if(item.id.equals(id)) {
            return true;
         }
      }

      return false;
   }

   public boolean containsItem1(String id) {
      Iterator<Item> var3 = this.items.iterator();

      while(var3.hasNext()) {
         Item item = var3.next();
         if(item.getId().equals(id)) {
            return true;
         }
      }

      return false;
   }

   public Item getItemById(String id) {
      Iterator<Item> var3 = this.items.iterator();
      Item item = null;
      while(var3.hasNext()) {
         item = var3.next();
         if(item.id.equals(id)) {
            return item;
         }
      }

      return null;
   }

   public boolean mountItem(String id) {
      Item item = getItemById(id.substring(0, id.length() - 3));
      if(item != null && Integer.parseInt(id.substring(id.length() - 1, id.length())) == item.modificationIndex) {
         if(item.itemType == ItemType.WEAPON) {
            this.mountTurret = item;
            return true;
         }

         if(item.itemType == ItemType.ARMOR) {
            this.mountHull = item;
            return true;
         }

         if(item.itemType == ItemType.COLOR) {
            this.mountColormap = item;
            return true;
         }
      }

      return false;
   }

   public boolean updateItem(String id) {
      Item item = getItemById(id.substring(0, id.length() - 3));
      int modificationID = Integer.parseInt(id.substring(id.length() - 1));
      if(modificationID < 3 && item.modificationIndex == modificationID) {
         ++item.modificationIndex;
         item.mod = 0;
         item.nextPrice = item.modifications[item.modificationIndex + 1 != 4?item.modificationIndex + 1:item.modificationIndex].price;
         item.nextProperty = item.modifications[item.modificationIndex + 1 != 4?item.modificationIndex + 1:item.modificationIndex].propertys;
         item.nextRankId = item.modifications[item.modificationIndex + 1 != 4?item.modificationIndex + 1:item.modificationIndex].rank;
         this.replaceItems(getItemById(id.substring(0, id.length() - 3)), item);
         return true;
      } else {
         return false;
      }
   }

   public Item buyItem(String id, int count, LobbyManager nul) {
      id = id.substring(0, id.length() - 3);
      Item item = (GarageItemsLoader.items.get(id)).clone();
      if(item.specialItem) {
         return null;
      } else {
         if(!this.items.contains(getItemById(id))&&item.itemType != ItemType.KIT) {
            if(item.itemType == ItemType.INVENTORY) {
               item.count += count;
            }
            this.items.add(item);
            nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { item.id, "_m", String.valueOf(item.modificationIndex) }), JSONUtils.parseItemInfo(item));
            return item;
         } else if(item.itemType == ItemType.KIT) {
            List df = ((Kit)KitsLoader.kits.get(item.id)).getKitItems();
            for(int fg = 0; fg<df.size();fg++)
            {
               KitItem vard = (KitItem) df.get(fg);
               Item jk = getItemById(vard.getItemId());
               if(jk != null && containsItem(vard.getItemId()))
               {
                  Item ds = (GarageItemsLoader.items.get(vard.getItemId())).clone();
                  ds.modificationIndex = vard.getmodif();
                  ds.count = vard.getCount();
                  if(jk.itemType == ItemType.INVENTORY)
                  {
                     ds.count += jk.count;
                     this.items.remove(jk);
                     this.items.add(ds);
                     item = ds;
                     nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { item.id, "_m", String.valueOf(item.modificationIndex) }), JSONUtils.parseItemInfo(item));
                  }
                  if (jk.modificationIndex >= ds.modificationIndex) {

                  } else if (ds.modificationIndex > jk.modificationIndex) {
                     while(ds.modificationIndex > jk.modificationIndex) {
                        nul.send(Type.GARAGE, "update_item", vard.getItemId() + "_m" + jk.modificationIndex);
                        ++jk.modificationIndex;
                     }
                  } else {
                     this.items.remove(jk);
                     this.items.add(ds);
                     item = ds;
                     nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { item.id, "_m", String.valueOf(item.modificationIndex) }), JSONUtils.parseItemInfo(item));
                  }
               }else{
                  Item ds = (GarageItemsLoader.items.get(vard.getItemId())).clone();
                  ds.modificationIndex = vard.getmodif();
                  ds.count = vard.getCount();
                  this.items.add(ds);
                  item = ds;
                  if(ds.modificationIndex>0)
                  {
                     int yu = 0;
                     item.modificationIndex = 0;
                     nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { item.id, "_m", String.valueOf(0) }), JSONUtils.parseItemInfo(item));
                     item.modificationIndex = vard.getmodif();
                     while(yu < item.modificationIndex) {
                        nul.send(Type.GARAGE, "update_item", vard.getItemId() + "_m" + yu);
                        ++yu;
                     }
                  }else{
                     nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { item.id, "_m", String.valueOf(item.modificationIndex) }), JSONUtils.parseItemInfo(item));
                  }
               }
            }
            nul.sendGarage();
            return item;
         }else if(item.itemType == ItemType.INVENTORY) {
            Item fromUser = getItemById(id);
            fromUser.count += count;
            nul.send(Type.GARAGE, "buy_item", StringUtils.concatStrings(new String[] { fromUser.id, "_m", String.valueOf(fromUser.modificationIndex) }), JSONUtils.parseItemInfo(fromUser));
            return fromUser;
         } else {
            return null;
         }
      }
   }

   private void replaceItems(Item old, Item newItem) {
      if(this.items.contains(old)) {
         this.items.set(this.items.indexOf(old), newItem);
      }

   }

   public ArrayList<Item> getInventoryItems() {
      ArrayList<Item> _items = new ArrayList<Item>();
      Iterator<Item> var3 = this.items.iterator();

      while(var3.hasNext()) {
         Item item = var3.next();
         if(item.itemType == ItemType.INVENTORY) {
            _items.add(item);
         }
      }

      return _items;
   }

   public void parseJSONData() {
      JSONObject hulls = new JSONObject();
      JSONArray _hulls = new JSONArray();
      JSONObject colormaps = new JSONObject();
      JSONArray _colormaps = new JSONArray();
      JSONObject turrets = new JSONObject();
      JSONArray _turrets = new JSONArray();
      JSONObject inventory_items = new JSONObject();
      JSONArray _inventory = new JSONArray();
      JSONObject effects_items = new JSONObject();
      JSONArray _effects= new JSONArray();
      Iterator<Item> var10 = this.items.iterator();
      //Item var = null;

      while(var10.hasNext()) {
         Item item = var10.next();
         try {
            if (item.itemType == ItemType.ARMOR) {
               HashMap<String, Object> rt = new HashMap<>();
               rt.put("id", item.id);
               rt.put("modification", item.modificationIndex);
               rt.put("mounted", item == this.mountHull);
               rt.put("mod", item.mod);
               rt.put("rot", item.rot);
               rt.put("mx", item.mx);
               _hulls.add(new JSONObject(rt));
            }

            if (item.itemType == ItemType.COLOR) {
               HashMap<String, Object> rt = new HashMap<>();
               rt.put("id", item.id);
               rt.put("modification", item.modificationIndex);
               rt.put("mounted", item == this.mountColormap);
               rt.put("mod", item.mod);
               _colormaps.add(new JSONObject(rt));
            }

            if (item.itemType == ItemType.WEAPON) {
               HashMap<String, Object> rt = new HashMap<>();
               rt.put("id", item.id);
               rt.put("modification", item.modificationIndex);
               rt.put("mounted", item == this.mountTurret);
               rt.put("mod", item.mod);
               rt.put("rot", item.rot);
               rt.put("mx", item.mx);
               _turrets.add(new JSONObject(rt));
            }

            if (item.itemType == ItemType.INVENTORY) {
               HashMap<String, Object> rt = new HashMap<>();
               rt.put("id", item.id + "");
               rt.put("count", item.count);
               _inventory.add(new JSONObject(rt));
            }

            if (item.itemType == ItemType.PLUGIN) {
               HashMap<String, Object> rt = new HashMap<>();
               rt.put("id", item.id + "");
               rt.put("time", item.time);
               _effects.add(new JSONObject(rt));
            }
         }catch(NullPointerException iu){
            iu.printStackTrace();
         }
         }
         hulls.put("hulls", _hulls);
         colormaps.put("colormaps", _colormaps);
         turrets.put("turrets", _turrets);
         inventory_items.put("inventory", _inventory);
         effects_items.put("effects", _effects);
         this._json_colormaps = colormaps.toJSONString();
         this._json_hulls = hulls.toJSONString();
         this._json_turrets = turrets.toJSONString();
         this._json_inventory = inventory_items.toJSONString();
         this._json_effects = effects_items.toJSONString();
      //}
   }

   public void unparseJSONData() throws ParseException {
      this.items.clear();
      JSONParser parser = new JSONParser();
      JSONObject turrets = (JSONObject)parser.parse(this._json_turrets);
      JSONObject colormaps = (JSONObject)parser.parse(this._json_colormaps);
      JSONObject hulls = (JSONObject)parser.parse(this._json_hulls);
      JSONObject inventory;
      if(this._json_inventory != null && !this._json_inventory.isEmpty()) {
         inventory = (JSONObject)parser.parse(this._json_inventory);
      } else {
         inventory = null;
      }

      JSONObject effects;
      if(this._json_effects != null && !this._json_effects.isEmpty()) {
         effects = (JSONObject)parser.parse(this._json_effects);
      } else {
         effects = null;
      }

      Iterator var7 = ((JSONArray)turrets.get("turrets")).iterator();

      Object inventory_item;
      JSONObject _item;
      Item item;
      while(var7.hasNext()) {
         inventory_item = var7.next();
         _item = (JSONObject)inventory_item;
         item = ((Item)GarageItemsLoader.items.get(_item.get("id"))).clone();
         item.modificationIndex = (int)((Long)_item.get("modification")).longValue();
         item.mod = (int)((Long)_item.get("mod")).longValue();
         item.nextRankId = item.modifications[item.modificationIndex == 3?3:item.modificationIndex + 1].rank;
         item.nextPrice = item.modifications[item.modificationIndex == 3?3:item.modificationIndex + 1].price;
         item.rot = _item.get("rot") == null?false:(Boolean) _item.get("rot");
         item.mx = _item.get("mx") == null?false:(Boolean) _item.get("mx");
         this.items.add(item);
         if((Boolean) _item.get("mounted")) {
            this.mountTurret = item;
         }
      }

      var7 = ((JSONArray)colormaps.get("colormaps")).iterator();

      while(var7.hasNext()) {
         inventory_item = var7.next();
         _item = (JSONObject)inventory_item;
         item = ((Item)GarageItemsLoader.items.get(_item.get("id"))).clone();
         item.modificationIndex = (int)((Long)_item.get("modification")).longValue();
         item.mod = (int)((Long)_item.get("mod")).longValue();
         this.items.add(item);
         if((Boolean) _item.get("mounted")) {
            this.mountColormap = item;
         }
      }

      var7 = ((JSONArray)hulls.get("hulls")).iterator();

      while(var7.hasNext()) {
         inventory_item = var7.next();
         _item = (JSONObject)inventory_item;
         item = ((Item)GarageItemsLoader.items.get(_item.get("id"))).clone();
         item.modificationIndex = (int)((Long)_item.get("modification")).longValue();
         item.mod = (int)((Long)_item.get("mod")).longValue();
         item.nextRankId = item.modifications[item.modificationIndex == 3?3:item.modificationIndex + 1].rank;
         item.nextPrice = item.modifications[item.modificationIndex == 3?3:item.modificationIndex + 1].price;
         item.rot = _item.get("rot") == null?false:(Boolean) _item.get("rot");
         item.mx = _item.get("mx") == null?false:(Boolean) _item.get("mx");
         this.items.add(item);
         if((Boolean) _item.get("mounted")) {
            this.mountHull = item;
         }
      }

      if(inventory != null) {
         var7 = ((JSONArray)inventory.get("inventory")).iterator();

         while(var7.hasNext()) {
            inventory_item = var7.next();
            _item = (JSONObject)inventory_item;
            item = ((Item)GarageItemsLoader.items.get(_item.get("id"))).clone();
            item.modificationIndex = 0;
            item.count = (int)((Long)_item.get("count")).longValue();
            if(item.itemType == ItemType.INVENTORY) {
               this.items.add(item);
            }
         }
      }

      if(effects != null) {
         var7 = ((JSONArray)effects.get("effects")).iterator();

         while(var7.hasNext()) {
            inventory_item = var7.next();
            _item = (JSONObject)inventory_item;
            item = ((Item)GarageItemsLoader.items.get(_item.get("id"))).clone();
            item.modificationIndex = 0;
            item.time = (Long) _item.get("time");
            new SimpleTimer(this,item);
            if(item.itemType == ItemType.PLUGIN) {
               this.items.add(item);
            }
         }
      }

   }

   public String get_json_turrets() {
      return this._json_turrets;
   }

   public void set_json_turrets(String _json_turrets) {
      this._json_turrets = _json_turrets;
   }

   public String get_json_hulls() {
      return this._json_hulls;
   }

   public void set_json_hulls(String _json_hulls) {
      this._json_hulls = _json_hulls;
   }

   public String get_json_colormaps() {
      return this._json_colormaps;
   }

   public void set_json_colormaps(String _json_colormaps) {
      this._json_colormaps = _json_colormaps;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String get_json_inventory() {
      return this._json_inventory;
   }

   public void set_json_inventory(String _json_inventory) {
      this._json_inventory = _json_inventory;
   }
}
