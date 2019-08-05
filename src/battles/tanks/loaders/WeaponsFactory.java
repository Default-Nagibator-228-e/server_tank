package battles.tanks.loaders;

import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.tanks.weapons.*;
import battles.tanks.weapons.flamethrower.FlamethrowerEntity;
import battles.tanks.weapons.flamethrower.FlamethrowerModel;
import battles.tanks.weapons.frezee.FrezeeEntity;
import battles.tanks.weapons.frezee.FrezeeModel;
import battles.tanks.weapons.hammer.HammerEntity;
import battles.tanks.weapons.hammer.HammerModel;
import battles.tanks.weapons.isida.IsidaEntity;
import battles.tanks.weapons.isida.IsidaModel;
import battles.tanks.weapons.railgun.RailgunEntity;
import battles.tanks.weapons.railgun.RailgunModel;
import battles.tanks.weapons.ricochet.RicochetEntity;
import battles.tanks.weapons.ricochet.RicochetModel;
import battles.tanks.weapons.shaft.ShaftEntity;
import battles.tanks.weapons.shaft.ShaftModel;
import battles.tanks.weapons.smoky.SmokyEntity;
import battles.tanks.weapons.smoky.SmokyModel;
import battles.tanks.weapons.annihilat.AnnihilatorEntity;
import battles.tanks.weapons.annihilat.AnnihilatorModel;
import battles.tanks.weapons.thunder.ThunderEntity;
import battles.tanks.weapons.thunder.ThunderModel;
import battles.tanks.weapons.twins.TwinsEntity;
import battles.tanks.weapons.twins.TwinsModel;
import battles.tanks.weapons.vulcan.VulcanEntity;
import battles.tanks.weapons.vulcan.VulcanModel;
import json.JSONUtils;
import logger.Logger;
import logger.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import main.database.impl.DatabaseManagerImpl;
import users.garage.items.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeaponsFactory {
   private static HashMap weapons = new HashMap();
   private static HashMap wwd = new HashMap();
   private static String jsonListWeapons;
   private static String[] yt = new String[]{"flamethrower","frezee","isida","railgun","ricochet","shaft","smoky","аnnihil","vulcan","hammer","thunder","twins"};

   public static IWeapon getWeapon(Item turretId1, BattlefieldPlayerController tank, BattlefieldModel battle) {
      String turretId = turretId1.getId();
      String turret = turretId.split("_")[0];
      switch(turret) {
         case "hammer":
            return new HammerModel((HammerEntity) getEntity(turretId1), getWwd(turretId), tank, battle);
         case "vulcan":
            return new VulcanModel((VulcanEntity)getEntity(turretId1), getWwd(turretId), tank, battle);
         case "аnnihil":
               return new AnnihilatorModel((AnnihilatorEntity)getEntity(turretId1), getWwd(turretId), tank, battle);
         case "ricochet":
               return new RicochetModel((RicochetEntity)getEntity(turretId1), battle, tank);
         case "thunder":
               return new ThunderModel((ThunderEntity)getEntity(turretId1), battle, tank);
         case "frezee":
               return new FrezeeModel((FrezeeEntity)getEntity(turretId1), battle, tank);
         case "isida":
               return new IsidaModel((IsidaEntity)getEntity(turretId1), tank, battle);
         case "shaft":
               return new ShaftModel((ShaftEntity)getEntity(turretId1), getWwd(turretId), battle, tank);
         case "smoky":
               return new SmokyModel((SmokyEntity)getEntity(turretId1), getWwd(turretId), battle, tank);
         case "twins":
               return new TwinsModel((TwinsEntity)getEntity(turretId1), getWwd(turretId), tank, battle);
         case "railgun":
               return new RailgunModel((RailgunEntity)getEntity(turretId1), tank, battle);
         case "flamethrower":
               return new FlamethrowerModel((FlamethrowerEntity)getEntity(turretId1), battle, tank);
      }
      return null;
   }

   public static void init(String va,String va1) {
      try {
         for(int var3 = 0; var3 < yt.length; ++var3) {
            JSONParser parser = new JSONParser();
            String type = va1;
            String id;
            Object[] var4 = ((JSONArray)parser.parse(va)).toArray();
            for(Object item:var4) {
               JSONObject jitem = (JSONObject)item;
               String modification = (String)jitem.get("modification");
               id = type + "_" + modification;
               weapons.remove(id);
               ShotData shotData = new ShotData(id, getDouble(jitem.get("autoAimingAngleDown"+"")), getDouble(jitem.get("autoAimingAngleUp"+"")), Integer.parseInt(jitem.get("numRaysDown")+""), Integer.parseInt(jitem.get("numRaysUp")+""), Integer.parseInt(jitem.get("reloadMsec")+""), (float)getDouble(jitem.get("impactCoeff"+"")), (float)getDouble(jitem.get("kickback"+"")), (float)getDouble(jitem.get("turretRotationAccel"+"")), (float)getDouble(jitem.get("turretRotationSpeed"+"")));
               switch(type) {
                  case "hammer":
                        weapons.put(id, new HammerEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "vulcan":
                        weapons.put(id, new VulcanEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "аnnihil":
                        weapons.put(id, new AnnihilatorEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "ricochet":
                        weapons.put(id, new RicochetEntity((float)getDouble(jitem.get("shotRadius"+"")), (float)getDouble(jitem.get("shotSpeed"+"")), (int)(Long.parseLong(jitem.get("energyCapacity")+""+"")), (int)(Long.parseLong(jitem.get("energyPerShot")+""+"")), (float)getDouble(jitem.get("energyRechargeSpeed"+"")), (float)getDouble(jitem.get("shotDistance"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "thunder":
                        WeaponWeakeningData wwdt = new WeaponWeakeningData(getDouble(jitem.get("maxSplashDamageRadius"+"")), getDouble(jitem.get("minSplashDamageRadius"+"")), getDouble(jitem.get("minSplashDamagePercent"+"")));
                        weapons.put(id, new ThunderEntity((float)getDouble(jitem.get("maxSplashDamageRadius"+"")), (float)getDouble(jitem.get("minSplashDamageRadius"+"")), (float)getDouble(jitem.get("minSplashDamagePercent"+"")), (float)getDouble(jitem.get("impactForce"+"")), shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), wwdt));
                        wwd.put(id, wwdt);
                     break;
                  case "frezee":
                        weapons.put(id, new FrezeeEntity((float)getDouble(jitem.get("damageAreaConeAngle"+"")), (float)getDouble(jitem.get("damageAreaRange"+"")), (int)(Long.parseLong(jitem.get("energyCapacity")+"")), (int)(Long.parseLong(jitem.get("energyDischargeSpeed")+"")), (int)(Long.parseLong(jitem.get("energyRechargeSpeed")+"")), (int)(Long.parseLong(jitem.get("weaponTickMsec")+"")), (float)getDouble(jitem.get("coolingSpeed"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                     break;
                  case "isida":
                        weapons.put(id, new IsidaEntity((int)(Long.parseLong(jitem.get("capacity")+"")), (int)(Long.parseLong(jitem.get("chargeRate")+"")), (int)(Long.parseLong(jitem.get("dischargeRate")+"")), (int)(Long.parseLong(jitem.get("tickPeriod")+"")), (float)getDouble(jitem.get("lockAngle"+"")), (float)getDouble(jitem.get("lockAngleCos"+"")), (float)getDouble(jitem.get("maxAngle"+"")), (float)getDouble(jitem.get("maxAngleCos"+"")), (float)getDouble(jitem.get("maxRadius"+"")), shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                     break;
                  case "shaft":
                        weapons.put(id, new ShaftEntity(shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                        wwd.put(id, new WeaponWeakeningData(getDouble( jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "smoky":
                        weapons.put(id, new SmokyEntity(shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("a_per"+"")), (float)getDouble(jitem.get("per"+"")), (float)getDouble(jitem.get("d_per"+""))));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "twins":
                        weapons.put(id, new TwinsEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                        wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "railgun":
                        weapons.put(id, new RailgunEntity(shotData, (int)(Long.parseLong(jitem.get("charingTime")+"")), (int)(Long.parseLong(jitem.get("weakeningCoeff")+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                     break;
                  case "flamethrower":
                        weapons.put(id, new FlamethrowerEntity((int)(Long.parseLong(jitem.get("target_detection_interval") + "")), (float)getDouble(jitem.get("range"+"")), (float)getDouble(jitem.get("cone_angle"+"")), (int)(Long.parseLong(jitem.get("heating_speed")+"")), (int)(Long.parseLong(jitem.get("cooling_speed")+"")), (int)(Long.parseLong(jitem.get("heat_limit")+"")), shotData, (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("min_damage"+""))));
               }
            }
         }

         jsonListWeapons = JSONUtils.parseWeapons(getEntitys(), wwd);
      } catch (Exception var6) {
         var6.printStackTrace();
         Logger.log(Type.ERROR, "Loading entitys weapons failed. " + var6.getMessage());
      }

   }

   public static void init() {
      weapons.clear();
      try {
         for(int var3 = 0; var3 < yt.length; ++var3) {
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(DatabaseManagerImpl.instance().getPrByName(yt[var3]).get_val());
            String type = (String)jobj.get("type");
            String id;
            Object[] var4 = ((JSONArray)jobj.get("params")).toArray();
            for(Object item:var4) {
               JSONObject jitem = (JSONObject)item;
               String modification = (String)jitem.get("modification");
               id = type + "_" + modification;
               weapons.remove(id);
               ShotData shotData = new ShotData(id, getDouble(jitem.get("autoAimingAngleDown"+"")), getDouble(jitem.get("autoAimingAngleUp"+"")), Integer.parseInt(jitem.get("numRaysDown")+""), Integer.parseInt(jitem.get("numRaysUp")+""), Integer.parseInt(jitem.get("reloadMsec")+""), (float)getDouble(jitem.get("impactCoeff"+"")), (float)getDouble(jitem.get("kickback"+"")), (float)getDouble(jitem.get("turretRotationAccel"+"")), (float)getDouble(jitem.get("turretRotationSpeed"+"")));
               switch(type) {
                  case "hammer":
                     weapons.put(id, new HammerEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "vulcan":
                     weapons.put(id, new VulcanEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "аnnihil":
                     weapons.put(id, new AnnihilatorEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("frezee_speed"+"")), shotData));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "ricochet":
                     weapons.put(id, new RicochetEntity((float)getDouble(jitem.get("shotRadius"+"")), (float)getDouble(jitem.get("shotSpeed"+"")), (int)(Long.parseLong(jitem.get("energyCapacity")+""+"")), (int)(Long.parseLong(jitem.get("energyPerShot")+""+"")), (float)getDouble(jitem.get("energyRechargeSpeed"+"")), (float)getDouble(jitem.get("shotDistance"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "thunder":
                     WeaponWeakeningData wwdt = new WeaponWeakeningData(getDouble(jitem.get("maxSplashDamageRadius"+"")), getDouble(jitem.get("minSplashDamageRadius"+"")), getDouble(jitem.get("minSplashDamagePercent"+"")));
                     weapons.put(id, new ThunderEntity((float)getDouble(jitem.get("maxSplashDamageRadius"+"")), (float)getDouble(jitem.get("minSplashDamageRadius"+"")), (float)getDouble(jitem.get("minSplashDamagePercent"+"")), (float)getDouble(jitem.get("impactForce"+"")), shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), wwdt));
                     wwd.put(id, wwdt);
                     break;
                  case "frezee":
                     weapons.put(id, new FrezeeEntity((float)getDouble(jitem.get("damageAreaConeAngle"+"")), (float)getDouble(jitem.get("damageAreaRange"+"")), (int)(Long.parseLong(jitem.get("energyCapacity")+"")), (int)(Long.parseLong(jitem.get("energyDischargeSpeed")+"")), (int)(Long.parseLong(jitem.get("energyRechargeSpeed")+"")), (int)(Long.parseLong(jitem.get("weaponTickMsec")+"")), (float)getDouble(jitem.get("coolingSpeed"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                     break;
                  case "isida":
                     weapons.put(id, new IsidaEntity((int)(Long.parseLong(jitem.get("capacity")+"")), (int)(Long.parseLong(jitem.get("chargeRate")+"")), (int)(Long.parseLong(jitem.get("dischargeRate")+"")), (int)(Long.parseLong(jitem.get("tickPeriod")+"")), (float)getDouble(jitem.get("lockAngle"+"")), (float)getDouble(jitem.get("lockAngleCos"+"")), (float)getDouble(jitem.get("maxAngle"+"")), (float)getDouble(jitem.get("maxAngleCos"+"")), (float)getDouble(jitem.get("maxRadius"+"")), shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                     break;
                  case "shaft":
                     weapons.put(id, new ShaftEntity(shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                     wwd.put(id, new WeaponWeakeningData(getDouble( jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "smoky":
                     weapons.put(id, new SmokyEntity(shotData, (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("a_per"+"")), (float)getDouble(jitem.get("per"+"")), (float)getDouble(jitem.get("d_per"+""))));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "twins":
                     weapons.put(id, new TwinsEntity((float)getDouble(jitem.get("shot_range"+"")), (float)getDouble(jitem.get("shot_speed"+"")), (float)getDouble(jitem.get("shot_radius"+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+"")), shotData));
                     wwd.put(id, new WeaponWeakeningData(getDouble(jitem.get("max_damage_radius"+"")), getDouble(jitem.get("min_damage_percent"+"")), getDouble(jitem.get("min_damage_radius"+""))));
                     break;
                  case "railgun":
                     weapons.put(id, new RailgunEntity(shotData, (int)(Long.parseLong(jitem.get("charingTime")+"")), (int)(Long.parseLong(jitem.get("weakeningCoeff")+"")), (float)getDouble(jitem.get("min_damage"+"")), (float)getDouble(jitem.get("max_damage"+""))));
                     break;
                  case "flamethrower":
                     weapons.put(id, new FlamethrowerEntity((int)(Long.parseLong(jitem.get("target_detection_interval") + "")), (float)getDouble(jitem.get("range"+"")), (float)getDouble(jitem.get("cone_angle"+"")), (int)(Long.parseLong(jitem.get("heating_speed")+"")), (int)(Long.parseLong(jitem.get("cooling_speed")+"")), (int)(Long.parseLong(jitem.get("heat_limit")+"")), shotData, (float)getDouble(jitem.get("max_damage"+"")), (float)getDouble(jitem.get("min_damage"+""))));
               }
            }
         }

         jsonListWeapons = JSONUtils.parseWeapons(getEntitys(), wwd);
      } catch (Exception var6) {
         var6.printStackTrace();
         Logger.log(Type.ERROR, "Loading entitys weapons failed. " + var6.getMessage());
      }

   }

   public static WeaponWeakeningData getWwd(String id) {
      return (WeaponWeakeningData)wwd.get(id);
   }

   public static IEntity getEntity(Item id) {
      return (IEntity) weapons.get(id.getId());
   }

   public static String getId(IEntity entity) {
      String id = null;
      Iterator var3 = weapons.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         if((entry.getValue()).equals(entity)) {
            id = (String)entry.getKey();
         }
      }

      return id;
   }

   private static Long getLong(Object obj) {
      try {
         return new Long(obj + "");
      } catch (Exception var2) {
         return ((Long)obj).longValue();
      }
   }

   private static double getDouble(Object obj) {
      try {
         Double vr = new Double(obj + "");
         if(obj == "")
         {
            vr = new Double("1");
         }
         return vr;
      } catch (Exception var2) {
         return new Double("1");
      }
   }

   public static Collection getEntitys() {
      return weapons.values();
   }

   public static String getJSONList() {
      return jsonListWeapons;
   }
}
