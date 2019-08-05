package battles.tanks.loaders;

import battles.tanks.hulls.Hull;
import java.util.HashMap;
import java.util.Iterator;
import main.database.impl.DatabaseManagerImpl;
import users.garage.items.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HullsFactory {
   private static HashMap hulls = new HashMap();
   private static String[] yt = new String[]{"wasp","viking","titan","mamont","hunter","hornet","dictator","terminate"};

   public static void init() {
      hulls.clear();
      try {
         for(int var3 = 0; var3 < yt.length; ++var3) {
            JSONParser par = new JSONParser();
            JSONObject jobj = (JSONObject)par.parse(DatabaseManagerImpl.instance().getPrByName(yt[var3]).get_val());
            String type = (String)jobj.get("type");
            Object[] var4 = ((JSONArray)jobj.get("modifications")).toArray();
            for (Object obj:var4) {
                JSONObject jt = (JSONObject)obj;
                float mass = (float)getDouble(jt.get("mass") + "");
                float power = (float)getDouble(jt.get("power") + "");
                float maxForwardSpeed = (float)getDouble(jt.get("maxForwardSpeed") + "");
                float maxBackwardSpeed = (float)getDouble(jt.get("maxBackwardSpeed") + "");
                float maxTurnSpeed = (float)getDouble(jt.get("maxTurnSpeed") + "");
                float springDamping = (float)getDouble(jt.get("springDamping") + "");
                float drivingForceOffsetZ = (float)getDouble(jt.get("drivingForceOffsetZ") + "");
                float smallVelocity = (float)getDouble(jt.get("smallVelocity") + "");
                float rayRestLengthCoeff = (float)getDouble(jt.get("rayRestLengthCoeff") + "");
                float dynamicFriction = (float)getDouble(jt.get("dynamicFriction") + "");
                float brakeFriction = (float)getDouble(jt.get("brakeFriction") + "");
                float sideFriction = (float)getDouble(jt.get("sideFriction") + "");
                float spotTurnPowerCoeff = (float)getDouble(jt.get("spotTurnPowerCoeff") + "");
                float spotTurnDynamicFriction = (float)getDouble(jt.get("spotTurnDynamicFriction") + "");
                float spotTurnSideFriction = (float)getDouble(jt.get("spotTurnSideFriction") + "");
                float moveTurnPowerCoeffOuter = (float)getDouble(jt.get("moveTurnPowerCoeffOuter") + "");
                float moveTurnPowerCoeffInner = (float)getDouble(jt.get("moveTurnPowerCoeffInner") + "");
                float moveTurnDynamicFrictionInner = (float)getDouble(jt.get("moveTurnDynamicFrictionInner") + "");
                float moveTurnDynamicFrictionOuter = (float)getDouble(jt.get("moveTurnDynamicFrictionOuter") + "");
                float moveTurnSideFriction = (float)getDouble(jt.get("moveTurnSideFriction") + "");
                float moveTurnSpeedCoeffInner = (float)getDouble(jt.get("moveTurnSpeedCoeffInner") + "");
                float moveTurnSpeedCoeffOuter = (float)getDouble(jt.get("moveTurnSpeedCoeffOuter") + "");
                float hp = (float)getDouble(jt.get("hp") + "");
               Hull hull = new Hull(mass,power,maxForwardSpeed,maxBackwardSpeed,maxTurnSpeed,springDamping,drivingForceOffsetZ,smallVelocity,rayRestLengthCoeff,dynamicFriction,brakeFriction,sideFriction,spotTurnPowerCoeff,spotTurnDynamicFriction,spotTurnSideFriction,moveTurnPowerCoeffOuter,moveTurnPowerCoeffInner,moveTurnDynamicFrictionInner,moveTurnDynamicFrictionOuter,moveTurnSideFriction,moveTurnSpeedCoeffInner,moveTurnSpeedCoeffOuter,hp);
               hulls.put(type + "_" + jt.get("modification"), hull);
            }
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }

   }

   public static void init(String va,String va1) {
      try {
         for(int var3 = 0; var3 < yt.length; ++var3) {
            JSONParser par = new JSONParser();
            String type = va1;
            Object[] var4 = ((JSONArray)par.parse(va)).toArray();
             for (Object obj:var4) {
               JSONObject jt = (JSONObject)obj;
                hulls.remove(type + "_" + jt.get("modification"));
                float mass = (float)getDouble(jt.get("mass") + "");
                float power = (float)getDouble(jt.get("power") + "");
                float maxForwardSpeed = (float)getDouble(jt.get("maxForwardSpeed") + "");
                float maxBackwardSpeed = (float)getDouble(jt.get("maxBackwardSpeed") + "");
                float maxTurnSpeed = (float)getDouble(jt.get("maxTurnSpeed") + "");
                float springDamping = (float)getDouble(jt.get("springDamping") + "");
                float drivingForceOffsetZ = (float)getDouble(jt.get("drivingForceOffsetZ") + "");
                float smallVelocity = (float)getDouble(jt.get("smallVelocity") + "");
                float rayRestLengthCoeff = (float)getDouble(jt.get("rayRestLengthCoeff") + "");
                float dynamicFriction = (float)getDouble(jt.get("dynamicFriction") + "");
                float brakeFriction = (float)getDouble(jt.get("brakeFriction") + "");
                float sideFriction = (float)getDouble(jt.get("sideFriction") + "");
                float spotTurnPowerCoeff = (float)getDouble(jt.get("spotTurnPowerCoeff") + "");
                float spotTurnDynamicFriction = (float)getDouble(jt.get("spotTurnDynamicFriction") + "");
                float spotTurnSideFriction = (float)getDouble(jt.get("spotTurnSideFriction") + "");
                float moveTurnPowerCoeffOuter = (float)getDouble(jt.get("moveTurnPowerCoeffOuter") + "");
                float moveTurnPowerCoeffInner = (float)getDouble(jt.get("moveTurnPowerCoeffInner") + "");
                float moveTurnDynamicFrictionInner = (float)getDouble(jt.get("moveTurnDynamicFrictionInner") + "");
                float moveTurnDynamicFrictionOuter = (float)getDouble(jt.get("moveTurnDynamicFrictionOuter") + "");
                float moveTurnSideFriction = (float)getDouble(jt.get("moveTurnSideFriction") + "");
                float moveTurnSpeedCoeffInner = (float)getDouble(jt.get("moveTurnSpeedCoeffInner") + "");
                float moveTurnSpeedCoeffOuter = (float)getDouble(jt.get("moveTurnSpeedCoeffOuter") + "");
                float hp = (float)getDouble(jt.get("hp") + "");
                Hull hull = new Hull(mass,power,maxForwardSpeed,maxBackwardSpeed,maxTurnSpeed,springDamping,drivingForceOffsetZ,smallVelocity,rayRestLengthCoeff,dynamicFriction,brakeFriction,sideFriction,spotTurnPowerCoeff,spotTurnDynamicFriction,spotTurnSideFriction,moveTurnPowerCoeffOuter,moveTurnPowerCoeffInner,moveTurnDynamicFrictionInner,moveTurnDynamicFrictionOuter,moveTurnSideFriction,moveTurnSpeedCoeffInner,moveTurnSpeedCoeffOuter,hp);
               hulls.put(type + "_" + jt.get("modification"), hull);
            }
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }

   }

   private static double getDouble(Object obj) {
      try {
         Double vr = new Double(obj+"");
         if(obj == "")
         {
            vr = new Double("1");
         }
         return vr;
      } catch (Exception var2) {
         return new Double("1");
      }
   }

   public static Hull getHull(Item id) {
      return (Hull)hulls.get(id.getId());
   }
}
