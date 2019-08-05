package main;

import org.apache.commons.net.ftp.FTPClient;
import users.news.News;
import users.opros.Opros;
import utils.RandomUtils;
import utils.RankUtils;
import battles.maps.MapsLoader;
import battles.tanks.loaders.HullsFactory;
import battles.tanks.loaders.WeaponsFactory;
import battles.timer.Timr;
import groups.UserGroupsLoader;
import console.ConsoleWindow;
import lobby.LobbyManager;
import logger.Logger;
import logger.remote.RemoteDatabaseLogger;
import main.database.impl.DatabaseManagerImpl;
import main.database.impl.Upd;
import main.netty.NettyService;
import users.garage.items.kit.loader.KitsLoader;
import services.AutoEntryServices;
import services.hibernate.HibernateService;
import system.SystemConsoleHandler;
import system.dailybonus.DailyBonusService;
import system.localization.Localization;
import system.quartz.impl.QuartzServiceImpl;
import test.server.configuration.ConfigurationsLoader;
import users.User;
import users.chal.Chal;
import users.garage.GarageItemsLoader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import users.garage.items.Item;
import users.invites.Invite;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
   public static ConsoleWindow console;
   public static Vector<LobbyManager> opdf = new Vector<LobbyManager>();
   public static Vector<Chal> cha = new Vector<>();
   private static SystemConsoleHandler sch;
   public static Upd bs = new Upd();
   public static String pat = "";//   /root/legendtanks/
   private static List<User> products;
   public static long endch;
   public static boolean ych = false;
   private static Vector<Vector<Item>> it = new Vector<Vector<Item>>();
   public static String site = "http://legendtanks.com/battle/resources/";
   public static List lisno;

   public static void main(String[] args) {
      try {
         PropertyConfigurator.configure(Main.class.getResource("log4j.properties"));
         System.out.println("Init config");
         ConfigurationsLoader.load(pat);
         initFactorys();
         System.out.println("Init groups");
         UserGroupsLoader.load(pat + "groups/");
         Logger.log("Connecting to DB...");
         Session ex = HibernateService.getSessionFactory().getCurrentSession();
         ex.beginTransaction();
         disableWarning();
         ex.createSQLQuery("SET NAMES \'utf8\' COLLATE \'utf8_general_ci\';");
         //System.out.println("Setting UTF-8 charset on DB: " + query.executeUpdate());
         ex.getTransaction().commit();
         QuartzServiceImpl.inject();
         System.out.println("Init maps");
         MapsLoader.initFactoryMaps();
         System.out.println("Init EP");
         DailyBonusService.instance();
         System.out.println("Init EntryServices");
         AutoEntryServices.instance();
         System.out.println("Init hulls");
         HullsFactory.init();
         System.out.println("Init turrets");
         WeaponsFactory.init();
         System.out.println("Prepare invates");
         prepareinv();
         System.out.println("Init challenge");
         initCh();
         System.out.println("Set ratings for users");
         iniU();
         Collections.sort(products, new Comparator<User>() {
            @Override
            public int compare(User r1, User r2) {
               int result = r2.yp - r1.yp;
               if(result != 0) {
                  return result;
               }else{
                  int result1 = r2.y - r1.y;
                  if(result1 != 0) {
                     return result1;
                  }else{
                     int result2 = r2.p - r1.p;
                     if(result2 != 0) {
                        return result2;
                     }else{
                        return r2.getCrystall() - r1.getCrystall();
                     }
                  }
               }
            }
         });
         for(int cds = 0;cds<products.size();cds++)
         {
            User var = products.get(cds);
            var.setPlace(var.getRating());
            var.setRating(cds+1);
            DatabaseManagerImpl.instance().update(var);
         }
         System.out.println("Prepare news and opros");
         ininews();
         System.out.println("Init socket");
         NettyService.inject().init();
         System.out.println("Started");
      } catch (Exception var3) {
         var3.printStackTrace();
         RemoteDatabaseLogger.error(var3);
      }

   }

    public static void testupload() {
        /*System.out.println("Test upload init");
        FTPClient client = new FTPClient();
        try (InputStream inputStream = new ByteArrayInputStream("ghsdtn".getBytes())) {
            client.connect("legendtanks.com",21);
            client.enterLocalPassiveMode();
            client.login("f0272236", "udrumiaxur");
            // Download file from FTP server.
            boolean status = client.storeFile("temp.txt", inputStream);
            System.out.println("status = " + status);
            System.out.println("reply  = " + client.getReplyString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finito test");*/
    }

    public static void ininews() {
        Session session = null;
        try {
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
                session.beginTransaction();
            }
            lisno = session.createQuery("from News").list();
            lisno.addAll(session.createQuery("from Opros").list());
            Collections.sort(lisno, new Comparator<Object>() {
                @Override
                public int compare(Object r1, Object r2) {
                    String da1 = r1.getClass().getName().equals("users.news.News") ? ((News) r1).getDat() : ((Opros) r1).getDat();
                    Long id1 = r1.getClass().getName().equals("users.news.News") ? ((News) r1).getId() : ((Opros) r1).getId();
                    String da2 = r2.getClass().getName().equals("users.news.News") ? ((News) r2).getDat() : ((Opros) r2).getDat();
                    Long id2 = r2.getClass().getName().equals("users.news.News") ? ((News) r2).getId() : ((Opros) r2).getId();
                    String[] s1 = da1.split("\\.");
                    String[] s2 = da2.split("\\.");
                    Date f1 = new Date(Integer.parseInt(s1[2]),Integer.parseInt(s1[1]),Integer.parseInt(s1[0]));
                    Date f2 = new Date(Integer.parseInt(s2[2]),Integer.parseInt(s2[1]),Integer.parseInt(s2[0]));
                    Long gg = f1.getTime() - f2.getTime();
                    gg /= 10000;
                    int result = Integer.parseInt(gg + "");
                    if(result != 0) {
                        return result;
                    }else{
                        return Integer.parseInt((id1 - id2) + "");
                    }
                }
            });
            session.getTransaction().commit();
        } catch (Exception var7) {
            if(session.getTransaction() != null) {
                session.getTransaction().rollback();
            }

            var7.printStackTrace();
            RemoteDatabaseLogger.error(var7);
        }
    }

    public static String generatePswd(int len) {
        String passSymbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        String password = "";
        int i1 = 0;
        for (int i = 0; i < len; i++) {
            if (i1 == 6) {
                password += "-";
                i1 = 0;
            }else{
                password += passSymbols.charAt(random.nextInt(passSymbols.length()));
                i1++;
            }
        }
        return password;
    }

    public static void prepareinv() {
        Session session = HibernateService.getSessionFactory().getCurrentSession();
        if(!session.getTransaction().isActive() || session.getTransaction() == null)
        {
            session.beginTransaction();
        }
        List<Invite> product;
        product = (List<Invite>) session.createQuery("from Invite").list();
        int rt = product.size();
        StringBuilder dsa = new StringBuilder();
        try(FileWriter writer = new FileWriter("inv.txt", false))
        {
            while(rt < 500)
            {
                Invite var = new Invite();
                var.setcod(generatePswd(33));
                dsa.append('\n').append(var.getcod());
                session.save(var);
                rt++;
            }
            writer.write(dsa.toString());
            writer.flush();
            writer.close();
            System.out.println("Save new invites");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        session.getTransaction().commit();
    }

    public static void disableWarning() {

    }

   private static void initCh() {
      it.add(GarageItemsLoader.invent);
      it.add(GarageItemsLoader.colormaps);
      it.add(GarageItemsLoader.tan);
      Session session = HibernateService.getSessionFactory().getCurrentSession();
      if(!session.getTransaction().isActive() || session.getTransaction() == null)
      {
         session.beginTransaction();
      }
      List<Chal> product;
      product = (List<Chal>) session.createQuery("from Chal").list();
      Collections.sort(product, new Comparator<Chal>() {
         @Override
         public int compare(Chal r1, Chal r2) {
            return r2.gzv() - r1.gzv();
         }
      });
      if(product.size() > 0)
      {
         for(int bt = 0;bt < product.size(); bt++)
         {
            if(product.get(bt).getsort() == 5)
            {
               ych = true;
               if(product.get(bt).getprew().isEmpty())
               {
                  long d = System.currentTimeMillis();// + 2592000000L;
                  String gr = d + "";
                  char[] strToArray = gr.toCharArray();
                  String fr = "";
                  String fr1 = "";
                  for(int i = 0; i < 13; i++) {
                     fr += strToArray[i] + "";
                  }
                  for(int i1 = 13; i1 < strToArray.length; i1++) {
                     fr1 += strToArray[i1] + "";
                  }
                  product.get(bt).setprew(fr);
                  DatabaseManagerImpl.instance().update(product.get(bt));
                  product.get(bt).setprew1(fr1);
                  DatabaseManagerImpl.instance().update(product.get(bt));
               }
                String doi = product.get(bt).getprew() + product.get(bt).getprew1();
                endch = Long.parseLong(doi) + 2592000000L;
               long fd = Main.endch - System.currentTimeMillis();
               if(fd <= 0)
               {
                   ych = false;
                   cch(product);
                   product.get(bt).setprew("");
               }else{
                   new Timr(fd);
                   ych = true;
               }
            }else{
               if(product.get(bt).gpid().isEmpty())
               {
                  if(product.get(bt).getTpe() != 3) {
                     initc(product.get(bt));
                  }
               }else{
                  cha.add(product.get(bt));
               }

            }
         }
      }
      session.getTransaction().commit();
   }

   private static void cch(List<Chal> product) {
      if(product.size() > 0)
      {
         for(int bt = 0;bt < product.size(); bt++)
         {
            Chal ch = product.get(bt);
            if(ch.getsort() < 3 && ch.getTpe() != 3)
            {
               ch.spid("");
               ch.setNazv("");
               ch.setcount(0);
               ch.setprew("");
               ch.spid1("");
               ch.setNazv1("");
               ch.setcount1(0);
               ch.setprew1("");
            }
            DatabaseManagerImpl.instance().update(ch);
         }
      }
   }

   private static void initc(Chal ch) {
      int io = (int)RandomUtils.getRandom(0F,Float.parseFloat( (it.get((int)ch.getTpe()).size() - 1) + ""));
      Item i = it.get((int)ch.getTpe()).get(io);
      ch.spid(i.id);
      ch.setNazv(i.name.localizatedString(Localization.RU));
      ch.setcount(ch.getTpe() == 0 ? RandomUtils.getRan(5,10,20,30,40,50,60) : 1);
      ch.setprew(site + getpr(ch.getTpe() == 2 ? i.id + "_XT_preview" : i.getId() + "_preview"));
      int io1 = (int) RandomUtils.getRandom(0F,Float.parseFloat( (it.get((int)ch.getTpe()).size() - 1) + ""));
      Item i1 = it.get((int)ch.getTpe()).get(io1);
      ch.spid1(i1.id);
      ch.setNazv1(i1.name.localizatedString(Localization.RU));
      ch.setType1(ch.getTpe());
      ch.setcount1(ch.getTpe() == 0 ? RandomUtils.getRan(5,10,20,30,40,50,60) : 1);
      ch.setprew1(site + getpr(ch.getTpe() == 2 ? i1.id + "_XT_preview" : i1.getId() + "_preview"));
      DatabaseManagerImpl.instance().update(ch);
   }

   private static String getpr(String nam)
   {
      StringBuilder builder = new StringBuilder();
      Throwable e1 = null;
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pat + "resourceImages.json")), StandardCharsets.UTF_8));

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
         if(e1 == null) {
            e1 = var18;
         } else if(e1 != var18) {
            e1.addSuppressed(var18);
         }
      }
      String json = builder.toString();
      JSONParser parser = new JSONParser();
      String src = null;
      try {
         Object e = parser.parse(json);
         JSONObject jparser = (JSONObject)e;
         JSONArray jarray = (JSONArray)jparser.get("items");
         for(int i = 0; i < jarray.size(); ++i) {
            JSONObject item = (JSONObject)jarray.get(i);
            if(((String)item.get("name")).equals(nam))
            {
               src = (String)item.get("src");
            }
         }
      } catch (ParseException var29) {
         var29.printStackTrace();
      }
      return src;
   }

   private static void iniU() {
      Session session = HibernateService.getSessionFactory().getCurrentSession();
      if(!session.getTransaction().isActive() || session.getTransaction() == null)
      {
         session.beginTransaction();
      }
      products = (List<User>) session.createQuery("from User").list();
      session.getTransaction().commit();
      if(!Main.ych)
      {
          for(int cds = 0;cds<products.size();cds++)
          {
              User var = products.get(cds);
              var.bp = false;
              var.zv = 0;
              var.part = 1;
              DatabaseManagerImpl.instance().update(var);
          }
      }
   }

   private static void initFactorys() throws IOException, ParseException {
      System.out.println("Init garage");
      GarageItemsLoader.loadFromConfig(pat + "turrets.json", pat + "hulls.json", pat + "colormaps.json", pat + "inventory.json", pat + "effects.json",1);
      System.out.println("Init kit");
      KitsLoader.load(pat + "kits/kits.cfg");
      System.out.println("Init ranks");
      RankUtils.init();
      System.out.println("Init console");
      sch = SystemConsoleHandler.getInstance();
      sch.start();
   }
}
