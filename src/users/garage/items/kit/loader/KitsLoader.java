package users.garage.items.kit.loader;

import users.garage.items.kit.Kit;
import users.garage.items.kit.KitItem;
import users.garage.items.kit.KitItemType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import system.localization.strings.LocalizedString;
import system.localization.strings.StringsLocalizationBundle;
import users.garage.GarageItemsLoader;
import users.garage.enums.ItemType;
import users.garage.items.Item;
import users.garage.items.modification.ModificationInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class KitsLoader {
   public static final Map kits = new HashMap();
   public static final Map kit = new HashMap();

   public static int load(String config) throws FileNotFoundException, IOException, ParseException {
      File file = new File(config);
      JSONParser parser = new JSONParser();
      JSONObject json = (JSONObject)parser.parse((new FileReader(file)));
      Iterator var4 = ((JSONArray)json.get("kits")).iterator();
      int sew = GarageItemsLoader.index;
      while(var4.hasNext()) {
         Object _kit = var4.next();
         JSONObject kit = (JSONObject)_kit;
         LocalizedString description = StringsLocalizationBundle.registerString(kit.get("description_ru") + "", kit.get("description_en") + "");
         LocalizedString name = StringsLocalizationBundle.registerString(kit.get("name") + "", kit.get("name") + "");
         String kitId = (String)kit.get("kit_id");
         ArrayList kitItems = new ArrayList();
         Iterator var9 = ((JSONArray)kit.get("items")).iterator();

         while(var9.hasNext()) {
            Object _item = var9.next();
            JSONObject item = (JSONObject)_item;
            KitItemType type = KitItemType.valueOf((String)item.get("type"));
            String itemId = (String)item.get("item_id");
            int count = (int)((Long)item.get("count")).longValue();
            int modif = (int)((Long)item.get("modif")).longValue();
            kitItems.add(new KitItem(type, itemId, count, modif));
         }

         int pr = (int)((Long)kit.get("price")).longValue();
         int r = (int)((Long)kit.get("ranki")).longValue();

         final ModificationInfo[] var30 = new ModificationInfo[4];
         var30[0] = new ModificationInfo(kitId + "_m0", pr, r);
         var30[0].propertys = null;
         var30[1] = new ModificationInfo(kitId + "_m0", pr, r);
         var30[1].propertys = null;
         var30[2] = new ModificationInfo(kitId + "_m0", pr, r);
         var30[2].propertys = null;
         var30[3] = new ModificationInfo(kitId + "_m0", pr, r);
         var30[3].propertys = null;

         kits.put(kitId, new Kit(kitItems, pr));
         Item jju = new Item(kitId, description, true, sew, null, ItemType.KIT, 0, name, null, pr+1, r+1, pr, r, var30, false, 0,0,false,"g","g",false,false);
         GarageItemsLoader.items1.add(jju);
         kit.put(kitId, jju);
         GarageItemsLoader.items.put(kitId,jju);
         ++sew;
      }
      return sew;
   }
}
