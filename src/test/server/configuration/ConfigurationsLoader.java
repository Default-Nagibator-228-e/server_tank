package test.server.configuration;

import main.Main;
import test.osgi.OSGi;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigurationsLoader {
   private static final String DEFAULT_PATH = "configurations/runner/";
   private static final String FORMAT_CONFIG = ".cfg";
   private static final String PARSER_CLASS_NAME = "class_name";
   private static final String PARSER_PARAMS_ARRAY = "params";
   private static final String PARSER_VALUE = "value";
   private static final String PARSER_VAR_NAME = "var";
   private static final JSONParser jsonParser = new JSONParser();

   public static void load(String pathToAllConfigs) {
      //if(pathToAllConfigs == null || pathToAllConfigs.isEmpty()) {
         pathToAllConfigs = Main.pat + DEFAULT_PATH;
      //}

      try {
         File path = new File(pathToAllConfigs);
         File[] var5;
         int var4 = (var5 = path.listFiles()).length;

         for (int var3 = 0; var3 < var4; ++var3) {
            File file = var5[var3];
            if (file.getPath().endsWith(FORMAT_CONFIG)) {
               parseAndLoadClass(file);
            }
         }
      }catch (NullPointerException hhy){
         File path = new File(pathToAllConfigs);
         File[] var5;
         int var4 = (var5 = path.listFiles()).length;

         for (int var3 = 0; var3 < var4; ++var3) {
            File file = var5[var3];
            if (file.getPath().endsWith(FORMAT_CONFIG)) {
               parseAndLoadClass(file);
            }
         }
      }

   }

   private static void parseAndLoadClass(File config) {
      try {
         JSONObject e = (JSONObject)jsonParser.parse((new FileReader(config)));
         String className = (String)e.get(PARSER_CLASS_NAME);
         JSONArray params = (JSONArray)e.get(PARSER_PARAMS_ARRAY);
         Class clazz = Class.forName(className);
         Object entity = clazz.newInstance();
         Iterator var7 = params.iterator();

         while(var7.hasNext()) {
            Object param = var7.next();
            JSONObject _param = (JSONObject)param;
            Field field = clazz.getDeclaredField((String)_param.get(PARSER_VAR_NAME));
            field.setAccessible(true);
            ConfigurationsLoader.Param conf_param = new ConfigurationsLoader.Param();
            conf_param.paramClassName = (String)_param.get(PARSER_CLASS_NAME);
            conf_param.value = _param.get(PARSER_VALUE);
            field.set(entity, conf_param.getValue());
         }

         OSGi.registerModel(entity);
      } catch (ParseException | IOException var11) {
         var11.printStackTrace();
      } catch (ClassNotFoundException var12) {
         var12.printStackTrace();
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException var13) {
         var13.printStackTrace();
      } catch (NoSuchFieldException var14) {
         var14.printStackTrace();
      }

   }

   static class Param {
      public String paramClassName;
      public Object value;

      public Object getValue() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
         Object returnValue = null;
         String var2 = this.paramClassName;
         switch(this.paramClassName.hashCode()) {
         case -2056817302:
            if(var2.equals("java.lang.Integer")) {
               if(this.value instanceof Long) {
                  returnValue = Integer.valueOf((int)((Long)this.value).longValue());
               } else {
                  returnValue = Integer.valueOf(((Integer)this.value).intValue());
               }

               return returnValue;
            }
            break;
         case -527879800:
            if(var2.equals("java.lang.Float")) {
               returnValue = Float.valueOf(((Float)this.value).floatValue());
               return returnValue;
            }
            break;
         case 398507100:
            if(var2.equals("java.lang.Byte")) {
               returnValue = Byte.valueOf(((Byte)this.value).byteValue());
               return returnValue;
            }
            break;
         case 398795216:
            if(var2.equals("java.lang.Long")) {
               returnValue = Long.valueOf(((Long)this.value).longValue());
               return returnValue;
            }
            break;
         case 761287205:
            if(var2.equals("java.lang.Double")) {
               returnValue = Double.valueOf(((Double)this.value).doubleValue());
               return returnValue;
            }
            break;
         case 1195259493:
            if(var2.equals("java.lang.String")) {
               returnValue = (String)this.value;
               return returnValue;
            }
         }

         System.out.println("Dont primitive type! " + this.paramClassName);
         return returnValue;
      }
   }
}
