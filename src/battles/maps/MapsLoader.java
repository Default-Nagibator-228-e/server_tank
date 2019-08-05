//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package battles.maps;

import battles.maps.loaders.MapL;
import battles.maps.parser.Parser;
import battles.bonuses.BonusRegion;
import battles.tanks.math.Vector3;
import logger.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import logger.remote.RemoteDatabaseLogger;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.hibernate.HibernateService;

public class MapsLoader {
   public static HashMap<String, Map> maps = new HashMap();

   public MapsLoader() {
   }

   public static void initFactoryMaps() {
      Logger.log("Maps Loader Factory inited. Loading maps...");
      loadConfig();
   }

   private static void loadConfig() {
      Session session = null;
      try {
         session = HibernateService.getSessionFactory().getCurrentSession();
         if(!session.getTransaction().isActive() || session.getTransaction() == null)
         {
            session.beginTransaction();
         }
         List<MapL> listmap = session.createQuery("from MapL").list();
         for (MapL var5:listmap) {
            Map map = new Map() {
               {
                  this.name = var5.name;
                  this.id = var5.mapid;
                  this.skyboxId = var5.skybox_id;
                  this.minRank = var5.min_rank;
                  this.maxRank = var5.max_rank;
                  this.maxPlayers = var5.max_players;
                  this.tdm = var5.tdm;
                  this.ctf = var5.ctf;
                  this.themeId = var5.theme_id;
               }
            };
            PSpawn(var5.spawn,map);
            PBonus(var5.bonus,map);
            PFlag(var5.flags,map);
            maps.put(map.id, map);
         }
         session.getTransaction().commit();
      } catch (Exception var7) {
         if(session.getTransaction() != null) {
            session.getTransaction().rollback();
         }

         var7.printStackTrace();
         RemoteDatabaseLogger.error(var7);
      }
   }

   private static void PFlag(String flag, Map map) {
      if(flag.isEmpty())
      {
         System.out.println("Карта: " + map.name + " не имеет флагов");
         return;
      }
      JSONParser par = new JSONParser();
      try {
         JSONObject jobj = (JSONObject)par.parse(flag);
         JSONObject var = ((JSONObject)jobj.get("blue"));
         Vector3 jt1 = getVec(var);
         jt1.z += 50;
         map.flagBluePosition = jt1;
         JSONObject var1 = ((JSONObject)jobj.get("red"));
         Vector3 jt2 = getVec(var1);
         jt2.z += 50;
         map.flagRedPosition = jt2;
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   private static void PBonus(String bonus, Map map) {
      if(bonus.isEmpty())
      {
         System.out.println("Карта: " + map.name + " не имеет бонусов");
         return;
      }
      JSONParser par = new JSONParser();
      try {
         JSONObject jobj = (JSONObject)par.parse(bonus);
         Object[] var = ((JSONArray)jobj.get("gold")).toArray();
         for (Object obj:var) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "crystal_100";
            map.goldsRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
         Object[] var1 = ((JSONArray)jobj.get("crystal")).toArray();
         for (Object obj:var1) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "crystal";
            map.crystallsRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
         Object[] var2 = ((JSONArray)jobj.get("medkit")).toArray();
         for (Object obj:var2) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "medkit";
            map.healthsRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
         Object[] var3 = ((JSONArray)jobj.get("armorup")).toArray();
         for (Object obj:var3) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "armorup";
            map.armorsRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
         Object[] var4 = ((JSONArray)jobj.get("damageup")).toArray();
         for (Object obj:var4) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "damageup";
            map.damagesRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
         Object[] var5 = ((JSONArray)jobj.get("nitro")).toArray();
         for (Object obj:var5) {
            JSONObject jt = (JSONObject)obj;
            JSONObject jt1 = (JSONObject)jt.get("min");
            JSONObject jt2 = (JSONObject)jt.get("max");
            JSONObject jt3 = (JSONObject)jt.get("pos");
            String[] jt4 = new String[1];
            jt4[0] = "nitro";
            map.nitrosRegions.add(new BonusRegion(getVec((jt2)),getVec(jt1),jt4));
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   private static void PSpawn(String spawn, Map map) {
      if(spawn.isEmpty())
      {
         System.out.println("Карта: " + map.name + " не имеет спавнов");
         return;
      }
      JSONParser par = new JSONParser();
      try {
         JSONObject jobj = (JSONObject)par.parse(spawn);
         Object[] var = ((JSONArray)jobj.get("dm")).toArray();
         for (Object obj:var) {
            JSONObject jt = (JSONObject)obj;
            map.spawnPositonsDM.add(getVec1(jt));
         }
         Object[] var1 = ((JSONArray)jobj.get("blue")).toArray();
         for (Object obj:var1) {
            JSONObject jt = (JSONObject)obj;
            map.spawnPositonsBlue.add(getVec1(jt));
         }
         Object[] var2 = ((JSONArray)jobj.get("red")).toArray();
         for (Object obj:var2) {
            JSONObject jt = (JSONObject)obj;
            map.spawnPositonsRed.add(getVec1(jt));
         }
      } catch (ParseException e) {
         e.printStackTrace();
      }
   }

   private static Vector3 getVec(JSONObject obj) {
      try {
         return new Vector3(getFloat(obj.get("x")),getFloat(obj.get("y")),getFloat(obj.get("z")));
      } catch (Exception var2) {
         return new Vector3(0,0,0);
      }
   }

   private static Vector3 getVec1(JSONObject obj) {
      try {
         Vector3 v = new Vector3(getFloat(obj.get("x")),getFloat(obj.get("y")),getFloat(obj.get("z")));
         v.rot = getFloat(obj.get("r"));
         return v;
      } catch (Exception var2) {
         return new Vector3(0,0,0);
      }
   }

   private static float getFloat(Object obj) {
      try {
         Float vr = new Float(obj+"");
         if(obj == "")
         {
            vr = new Float("1");
         }
         return vr;
      } catch (Exception var2) {
         return new Float("1");
      }
   }
}
