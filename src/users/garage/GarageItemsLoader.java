package users.garage;

import battles.tanks.colormaps.Colormap;
import battles.tanks.colormaps.ColormapsFactory;
import system.localization.strings.LocalizedString;
import system.localization.strings.StringsLocalizationBundle;
import users.garage.enums.ItemType;
import users.garage.enums.PropertyType;
import users.garage.items.Item;
import users.garage.items.PropertyItem;
import users.garage.items.modification.ModificationInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GarageItemsLoader {
   public static HashMap<String,Item> items = new HashMap();
   public static int index = 1;
   public static Vector<Item> items1 = new Vector<Item>();
   public static Vector<Item> invent = new Vector<Item>();
   public static Vector<Item> tan = new Vector<Item>();
   public static Vector<Item> ta = new Vector<Item>();
   public static Vector<Item> tu = new Vector<Item>();
   public static Vector<Item> co = new Vector<Item>();
   public static Vector<Item> colormaps = new Vector<Item>();

   public static void loadFromConfig(String turrets, String hulls, String colormaps, String inventory, String subscription, int in) {
      index = in;
      for(int i = 0; i < 5; ++i) {
         StringBuilder builder = new StringBuilder();

         Throwable e = null;
         Object var8 = null;

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(i == 0?inventory:(i == 1?turrets:(i == 2?hulls:i == 3?colormaps:subscription)))), StandardCharsets.UTF_8));

            String line;
            try {
               while((line = reader.readLine()) != null) {
                  builder.append(line);
               }
            } finally {
               if(reader != null) {
                  reader.close();
               }

            }
         } catch (Throwable var18) {
            if(e == null) {
               e = var18;
            } else if(e != var18) {
               e.addSuppressed(var18);
            }

            //throw e;
         }

         parseAndInitItems(builder.toString(), i == 0?ItemType.INVENTORY:(i == 1?ItemType.WEAPON:(i == 2?ItemType.ARMOR:i==3?ItemType.COLOR:ItemType.PLUGIN)));
      }

   }

   private static void parseAndInitItems(String json, ItemType typeItem) {
      JSONParser parser = new JSONParser();
      try {
         Object e = parser.parse(json);
         JSONObject jparser = (JSONObject)e;
         JSONArray jarray = (JSONArray)jparser.get("items");
         for(int i = 0; i < jarray.size(); ++i) {
            JSONObject item = (JSONObject)jarray.get(i);
            LocalizedString name = StringsLocalizationBundle.registerString((String)item.get("name_ru"), (String)item.get("name_en"));
            LocalizedString description = StringsLocalizationBundle.registerString((String)item.get("description_ru"), (String)item.get("description_en"));
            String id = (String)item.get("id");
            int priceM0 = Integer.parseInt((String)item.get("price_m0"));
            int priceM1 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("price_m1")):priceM0;
            int priceM2 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("price_m2")):priceM0;
            int priceM3 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("price_m3")):priceM0;
            int[] l = new int[5];
            try {
               l[0] = priceM0;
            }catch (NullPointerException oi)
            {
               l[0] = 0;
            }
            try {
               l[1] = priceM1;
            }catch (NullPointerException oi)
            {
               l[1] = 0;
            }
            try {
               l[2] = priceM2;
            }catch (NullPointerException oi)
            {
               l[2] = 0;
            }
            try {
               l[3] = priceM3;
            }catch (NullPointerException oi)
            {
               l[3] = 0;
            }
            l[4] = 0;
            int rangM0 = Integer.parseInt((String)item.get("rang_m0"));
            int rangM1 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("rang_m1")):rangM0;
            int rangM2 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("rang_m2")):rangM0;
            int rangM3 = typeItem != ItemType.COLOR && typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?Integer.parseInt((String)item.get("rang_m3")):rangM0;
            PropertyItem[] propertysItemM0 = null;
            PropertyItem[] propertysItemM1 = null;
            PropertyItem[] propertysItemM2 = null;
            PropertyItem[] propertysItemM3 = null;
            int countModification = typeItem == ItemType.COLOR?1:(typeItem != ItemType.INVENTORY && typeItem != ItemType.PLUGIN?4:(int)((Long)item.get("count_modifications")).longValue());
            Vector<PropertyItem[]> nu = new Vector<PropertyItem[]>();
            for(int mods = 0; mods < countModification; ++mods) {
               JSONArray specialItem = (JSONArray)item.get("propertys_m" + mods);
               PropertyItem[] property = new PropertyItem[specialItem.size()];

               for(int p = 0; p < specialItem.size(); ++p) {
                  JSONObject prop = (JSONObject) specialItem.get(p);
                  property[p] = new PropertyItem(getType((String) prop.get("type")), (String) prop.get("value"));
               }
               switch(mods) {
               case 0:
                  propertysItemM0 = property;
                  nu.add(mods,property);
                  break;
               case 1:
                  propertysItemM1 = property;
                  nu.add(mods,property);
                  break;
               case 2:
                  propertysItemM2 = property;
                  nu.add(mods,property);
                  break;
               case 3:
                  propertysItemM3 = property;
                  nu.add(mods,property);
               }
            }

            if(typeItem == ItemType.COLOR || typeItem == ItemType.INVENTORY || typeItem == ItemType.PLUGIN) {
               propertysItemM1 = propertysItemM0;
               propertysItemM2 = propertysItemM0;
               propertysItemM3 = propertysItemM0;
            }

            final ModificationInfo[] var30 = new ModificationInfo[4];
            var30[0] = new ModificationInfo(id + "_m0", priceM0, rangM0);
            var30[0].propertys = propertysItemM0;
            var30[1] = new ModificationInfo(id + "_m1", priceM1, rangM1);
            var30[1].propertys = propertysItemM1;
            var30[2] = new ModificationInfo(id + "_m2", priceM2, rangM2);
            var30[2].propertys = propertysItemM2;
            var30[3] = new ModificationInfo(id + "_m3", priceM3, rangM3);
            var30[3].propertys = propertysItemM3;
            boolean var31 = item.get("special_item") == null?false:((Boolean)item.get("special_item")).booleanValue();
            boolean var32 = item.get("XT") == null?false:((Boolean)item.get("XT")).booleanValue();
            String var33 = item.get("nXT") == null?"g":item.get("nXT") + "";
            String var34 = item.get("dXT") == null?"g":item.get("dXT") + "";
            Item jju = new Item(id, description, typeItem == ItemType.INVENTORY || typeItem == ItemType.PLUGIN, index, propertysItemM0, typeItem, 0, name, propertysItemM1, priceM1, rangM1, priceM0, rangM0, var30, var31, 0,0,var32,var33,var34,false,false);
            items1.add(jju);
            if(typeItem == ItemType.INVENTORY && !var31)
            {
               invent.add(jju);
            }
            if(typeItem == ItemType.COLOR && !var31)
            {
               colormaps.add(jju);
            }
            if(typeItem == ItemType.WEAPON || typeItem == ItemType.ARMOR)
            {
                ta.add(jju);
                if(!var31 && var32)
                {
                    tan.add(jju);
                }
            }
             if(typeItem == ItemType.WEAPON)
             {
                 co.add(jju);
             }
             if(typeItem == ItemType.ARMOR)
             {
                 tu.add(jju);
             }
            items.put(id, jju);
            ++index;
            if(typeItem == ItemType.COLOR) {
               ColormapsFactory.addColormap(id + "_m0", new Colormap() {
                  {
                     PropertyItem[] var5 = var30[0].propertys;
                     int var4 = var30[0].propertys.length;

                     for(int var3 = 0; var3 < var4; ++var3) {
                        PropertyItem _property = var5[var3];
                        this.addResistance(ColormapsFactory.getResistanceType(_property.property), GarageItemsLoader.getInt(_property.value.replace("%", "")));
                     }

                  }
               });
            }
         }
      } catch (ParseException var29) {
         var29.printStackTrace();
      }

   }

   private static int getInt(String str) {
      try {
         return Integer.parseInt(str);
      } catch (Exception var2) {
         return 0;
      }
   }

   private static PropertyType getType(String s) {
      PropertyType[] var4;
      int var3 = (var4 = PropertyType.values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         PropertyType type = var4[var2];
         if(type.toString().equals(s)) {
            return type;
         }
      }

      return null;
   }
}
