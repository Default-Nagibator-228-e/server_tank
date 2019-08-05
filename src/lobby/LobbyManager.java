package lobby;

import bugs.screenshots.BufferScreenshotTransfer;
import logger.remote.RemoteDatabaseLogger;
import main.Main;
import org.apache.commons.net.ftp.FTPClient;
import rmi.payments.mapping.Payment;
import services.hibernate.HibernateService;
import users.friends.Friends;
import users.garage.Garage;
import users.garage.GarageItemsLoader;
import users.garage.items.Item;
import users.anticheat.AntiCheatData;
import battles.maps.MapsLoader;
import battles.maps.Map;
import users.news.News;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import lobby.battles.BattleInfo;
import logger.Logger;
import lobby.battles.BattlesList;
import users.TypeUser;
import json.JSONUtils;
import lobby.chat.ChatMessage;
import commands.Command;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import commands.Type;
import main.params.OnlineStats;
import users.locations.UserLocation;
import lobby.chat.flood.LobbyFloodController;
import network.listeners.DisconnectListener;
import lobby.chat.flood.FloodController;
import services.AutoEntryServices;
import system.dailybonus.DailyBonusService;
import lobby.chat.ChatLobby;
import services.LobbysServices;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import main.database.DatabaseManager;
import battles.spectator.SpectatorController;
import battles.BattlefieldPlayerController;
import main.netty.ProtocolTransfer;
import users.User;
import users.opros.Opros;
import utils.StringUtils;
import javax.xml.bind.DatatypeConverter;

public class LobbyManager extends LobbyComandsConst
{
   private User localUser;
   public ProtocolTransfer networker;
   public BattlefieldPlayerController battle;
   public SpectatorController spectatorController;
   @ServicesInject(target = DatabaseManagerImpl.class)
   private static DatabaseManager database;
   @ServicesInject(target = LobbysServices.class)
   private static LobbysServices lobbysServices;
   @ServicesInject(target = ChatLobby.class)
   private static ChatLobby chatLobby;
   @ServicesInject(target = DailyBonusService.class)
   private static DailyBonusService dailyBonusService;
   @ServicesInject(target = AutoEntryServices.class)
   private AutoEntryServices autoEntryServices;
   private FloodController chatFloodController;
   public DisconnectListener disconnectListener;
   public long timer;
   private int ut = 0;
   private boolean chal = false;

   static {
      LobbyManager.database = DatabaseManagerImpl.instance();
      LobbyManager.lobbysServices = LobbysServices.getInstance();
      LobbyManager.chatLobby = ChatLobby.getInstance();
      LobbyManager.dailyBonusService = DailyBonusService.instance();
   }


   public LobbyManager(final ProtocolTransfer networker, final User localUser) {
      this.autoEntryServices = AutoEntryServices.instance();
      this.networker = networker;
      this.localUser = localUser;
      this.disconnectListener = new DisconnectListener();
      this.setChatFloodController(new LobbyFloodController());
      this.timer = System.currentTimeMillis();
      this.localUser.setUserLocation(UserLocation.BATTLESELECT);
      LobbyManager.lobbysServices.addLobby(this);
      OnlineStats.addOnline();
      OnlineStats.addinOnline(localUser.getNickname());
      this.localUser.idbat = "";
      LobbyManager.dailyBonusService.userInited(this);
   }

   public void send(final Type type, final String... args) {
         this.networker.send(type, args);
   }

   public void executeCommand(final Command cmd) {
      try {
         Garage ga = this.localUser.getGarage();
         switch (cmd.type) {
            case BATTLE:
               if (this.battle != null) {
                  this.battle.executeCommand(cmd);
               }
               if (this.spectatorController != null) {
                  this.spectatorController.executeCommand(cmd);
                  break;
               }
               break;
            case LOBBY:
                switch (cmd.args[0])
                {
                    case "get_garage_data":
                        this.sendGarage();
                        break;
                    case "addon":
                        try {
                            if (this.getLocalUser().getFriend().getDrV().contains(cmd.args[1])) {
                                this.sendFriend();
                            }
                        }catch (NullPointerException n) {}
                        break;
                    case "get_friend":
                        this.sendFriend();
                        break;
                    case "inv":
                        if(this.getLocalUser().getNickname().equals(cmd.args[1]))
                        {
                            this.sendIn(cmd.args[4],cmd.args[2],cmd.args[3]);
                        }
                        break;
                    case "invd":
                        LobbysServices.getInstance().chCommandToAllUsers(Type.LOBBY,"inv",cmd.args[1],database.getUserById(cmd.args[1]).getRang()+"",this.localUser.idbat,this.localUser.getNickname());
                        break;
                    case "make_friend":
                        makeFriend(cmd.args[1]);
                        break;
                    case "got_friend":
                        gotFriend(cmd.args[1]);
                        break;
                    case "del_friend":
                        delFriend(cmd.args[1]);
                        break;
                    case "del_infriend":
                        delnFriend(cmd.args[1]);
                        break;
                    case "addnews":
                        addNews(cmd.args[1],cmd.args[2],cmd.args[3]);
                        break;
                    case "remnews":
                        remNews(cmd.args[1]);
                        break;
                    case "lsl":
                        this.send(Type.LOBBY, "dno",cmd.args[1]);
                        break;
                    case "addopros":
                        addOpros(cmd.args[1],cmd.args[2],cmd.args[3],cmd.args[4]);
                        break;
                    case "addgolos":
                        addGolos(cmd.args[1],cmd.args[2]);
                        break;
                    case "remopsros":
                        remOpros(cmd.args[1]);
                        break;
                    case "get_rank":
                        this.sendRang(cmd.args[1],cmd.args[2],cmd.args[3]);
                        break;
                    case "imageup":
                        /*System.out.println("Test upload init");
                       FTPClient client = new FTPClient();
                       //Base64.Decoder dec = Base64.getDecoder();
                       byte[] decbytes = decode(cmd.args[1]);
                       try (InputStream inputStream = new ByteArrayInputStream(decbytes)) {
                           client.connect("legendtanks.com", 21);
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
                        break;
                    case "get_data_init_battle_select":
                        this.sendMapsInit();
                        break;
                    case "check_user":
                        try {
                            database.getUserById(cmd.args[1]).getNickname();
                            this.send(Type.LOBBY, "val", JSONUtils.parseV(0));
                        }catch (NullPointerException f){
                            this.send(Type.LOBBY, "val", JSONUtils.parseV(1));
                        }
                        break;
                    case "check_battleName_for_forbidden_words":
                        final String _name = (cmd.args.length > 1) ? cmd.args[1] : "";
                        this.checkBattleName(_name);
                        break;
                    case "try_create_battle_dm":
                        this.tryCreateBattleDM(cmd.args[1], cmd.args[2], Integer.parseInt(cmd.args[3]), Integer.parseInt(cmd.args[4]), Integer.parseInt(cmd.args[5]), Integer.parseInt(cmd.args[6]), Integer.parseInt(cmd.args[7]), this.stringToBoolean(cmd.args[8]), this.stringToBoolean(cmd.args[9]), this.stringToBoolean(cmd.args[10]));
                        break;
                    case "try_create_battle_tdm":
                        this.tryCreateTDMBattle(cmd.args[1]);
                        break;
                    case "zacet":
                        this.zacet(cmd.args[1]);
                        break;
                    case "obnew":
                        lobbysServices.chCommandToAllUsers(Type.LOBBY, "new");
                        break;
                    case "new":
                        parseNews();
                        break;
                    case "cool":
                        this.send(Type.LOBBY, "co", cmd.args[1],cmd.args[2],cmd.args[3],cmd.args[4],cmd.args[5]);
                        break;
                    case "try_create_battle_ctf":
                        this.tryCreateCTFBattle(cmd.args[1]);
                        break;
                    case "get_show_battle_info":
                        this.sendBattleInfo(cmd.args[1]);
                        break;
                    case "enter_battle":
                        this.onEnterInBattle(cmd.args[1]);
                        break;
                    case "bug_report":
                        RemoteDatabaseLogger.error(localUser.getNickname() + " " + cmd.args[1]);
                        break;
                    case "screenshot":
                        BufferScreenshotTransfer tr = new BufferScreenshotTransfer();
                        tr.encryptPacket(cmd.args[1],ut);
                        ut++;
                        break;
                    case "enter_battle_team":
                        this.onEnterInTeamBattle(cmd.args[1], Boolean.parseBoolean(cmd.args[2]));
                        break;
                    case "enter_battle_spectator":
                        break;
                    case "user_inited":
                        LobbyManager.dailyBonusService.userLoaded(this);
                }
               break;
            case LOBBY_CHAT:
               LobbyManager.chatLobby.addMessage(new ChatMessage(this.localUser, cmd.args[0], this.stringToBoolean(cmd.args[1]), cmd.args[2].equals("NULL") ? null : LobbyManager.database.getUserById(cmd.args[2]), this));
               break;
            case GARAGE:
                switch (cmd.args[0]) {
                    case "try_mount_item":
                        if (ga.mountItem(cmd.args[1])) {
                            Iterator<Item> var3 = ga.items.iterator();

                            while(var3.hasNext()) {
                                Item item = var3.next();
                                if (item.getId().equals(cmd.args[1])) {
                                    this.send(Type.GARAGE, "mount_item", StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)),item.mx? StringUtils.concatStrings(item.id, "_XT") : StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)));
                                    ga.parseJSONData();
                                    LobbyManager.database.update(ga);
                                }
                            }
                        }
                        else {
                            this.send(Type.GARAGE, "try_mount_item_NO");
                        }
                        break;
                    case "try_update_item":
                        this.onTryUpdateItem(cmd.args[1]);
                        break;
                    case "sskin":
                        this.sskin(cmd.args[1]);
                        break;
                    case "skin":
                        this.skin(cmd.args[1]);
                        break;
                    case "bskin":
                        this.bskin(cmd.args[1]);
                        break;
                    case "get_garage_data":
                        if(ga.mountHull != null && ga.mountTurret != null && ga.mountColormap != null)
                        {
                            this.send(Type.GARAGE, "init_mounted_item", StringUtils.concatStrings(ga.mountHull.id, "_m", String.valueOf(ga.mountHull.modificationIndex)),ga.mountHull.mx? StringUtils.concatStrings(ga.mountHull.id, "_XT"): StringUtils.concatStrings(ga.mountHull.id, "_m", String.valueOf(ga.mountHull.modificationIndex)));
                            this.send(Type.GARAGE, "init_mounted_item", StringUtils.concatStrings(ga.mountTurret.id, "_m", String.valueOf(ga.mountTurret.modificationIndex)),ga.mountTurret.mx ? StringUtils.concatStrings(ga.mountTurret.id, "_XT"): StringUtils.concatStrings(ga.mountTurret.id, "_m", String.valueOf(ga.mountTurret.modificationIndex)));
                            this.send(Type.GARAGE, "init_mounted_item", StringUtils.concatStrings(ga.mountColormap.id, "_m", String.valueOf(ga.mountColormap.modificationIndex)));
                        }
                        break;
                    case "try_buy_item":
                        this.onTryBuyItem(cmd.args[1], Integer.parseInt(cmd.args[2]));
                }
               break;
            case SYSTEM:
               if ((cmd.args[0]).equals("c01")) {
                  this.kick();
                  break;
               }
               break;
            case PING:
         }
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   public void remOpros(String v) {
      if(localUser.getType() == TypeUser.ADMIN) {
         Session session = null;
         try{
             Opros n1 = database.getOprosByHash(v);
             session = HibernateService.getSessionFactory().getCurrentSession();
             if(!session.getTransaction().isActive() || session.getTransaction() == null)
             {
                 session.beginTransaction();
             }
             List<User> products1 = (List<User>) session.createQuery("from User").list();
             session.getTransaction().commit();
             Iterator var3 = products1.iterator();
             while (var3.hasNext()) {
                 User us = (User) var3.next();
                 if (!us.opro.isEmpty()) {
                     JSONParser par1 = new JSONParser();
                     JSONObject jobj1 = (JSONObject) par1.parse(us.opro);
                     JSONArray var41 = (JSONArray) jobj1.get("op");
                     jobj1.remove("op");
                     for (int i = 0; i < var41.size(); i++) {
                         JSONObject item = (JSONObject) var41.get(i);
                         String ig = item.get("id") + "";
                         if (ig.equals(n1.gethash())) {
                             var41.remove(item);
                         }
                     }
                     jobj1.put("op", var41);
                     us.opro = jobj1.toJSONString();
                     database.update(us);
                 }
             }
             database.unregister(n1);
             Main.ininews();
             lobbysServices.chCommandToAllUsers(Type.LOBBY,"lsl",v);
         } catch (Exception var7) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }

            var7.printStackTrace();
            RemoteDatabaseLogger.error(var7);
         }
      }
   }

   public void addGolos(String v,String v1) throws Exception {
         Opros n = database.getOprosByHash(v);
            JSONObject ds = new JSONObject();
            JSONArray dof = new JSONArray();
            JSONObject ds1 = new JSONObject();
            ds1.put("id",n.gethash());
            ds1.put("nom",Integer.parseInt(v1));
            dof.add(ds1);
            ds.put("op",dof);
         if(localUser.opro.isEmpty()) {
            localUser.opro = ds.toJSONString();
            database.update(localUser);
            addinOpros(n,v1,ds.toJSONString());
         }else {
             JSONParser par1 = new JSONParser();
             JSONObject jobj1 = (JSONObject) par1.parse(localUser.opro);
             JSONArray var41 = (JSONArray) jobj1.get("op");
             Iterator var3 = var41.iterator();
             Boolean csa = false;
             while (var3.hasNext()) {
                 JSONObject item = (JSONObject) var3.next();
                 String ig = item.get("id") + "";
                 if (ig.equals(n.gethash())) {
                     csa = true;
                 }
             }
             if (!csa) {
                 jobj1.remove("op");
                 JSONObject ds2 = new JSONObject();
                 ds2.put("id", n.gethash());
                 ds2.put("nom", Integer.parseInt(v1));
                 var41.add(ds2);
                 jobj1.put("op", var41);
                 localUser.opro = jobj1.toJSONString();
                 database.update(localUser);
                 addinOpros(n, v1, ds.toJSONString());
             }
         }
   }

    public void addinOpros(Opros n, String v1, String v2) {
        try {
            String dsa = n.getclines();
            JSONParser par = new JSONParser();
            JSONObject jobj = (JSONObject) par.parse(dsa);
            JSONArray var4 = (JSONArray) jobj.get("lin");
            jobj.remove("lin");
            int i = Integer.parseInt(v1);
            Object obj = var4.get(i);
            JSONObject jt = (JSONObject) obj;
            Double mass = Double.valueOf(jt.get("siz") + "");
            jt.remove("siz");
            mass += 1;
            jt.put("siz", mass);
            Double vs = Double.valueOf(jobj.get("vs") + "");
            jobj.remove("vs");
            vs += 1;
            jobj.put("vs", vs);
            Iterator var3 = var4.iterator();
            while (var3.hasNext()) {
                JSONObject item = (JSONObject) var3.next();
                Double mass1 = Double.valueOf(item.get("siz") + "");
                Double fdss1 = mass1 / vs;
                item.remove("up");
                item.put("up", fdss1);
            }
            jobj.put("lin", var4);
            n.setclines(jobj.toJSONString());
            database.update0(n);
            Main.ininews();
            lobbysServices.chCommandToAllUsers(Type.LOBBY, "cool", n.getDat(), n.getZa(), n.getTex(), n.gethash(), n.getclines());
            this.send(Type.LOBBY, "pao;" + v2 + ";");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

   public void addOpros(String v,String v1,String v2,String v3) {
      if(localUser.getType() == TypeUser.ADMIN) {
         Opros n = new Opros();
         n.setDat(v);
         n.setZa(v1);
         n.setTex(v2);
         n.setclines(v3);
         String passSymbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         Random random = new Random();
         String password = "";
         for (int i = 0; i < 10; i++) {
            password += passSymbols.charAt(random.nextInt(passSymbols.length()));
         }
         n.sethash(password);
         n.setno(false);
         this.database.register(n);
         Main.ininews();
         this.send(Type.LOBBY, "omi;" + password + ";");
      }
   }

   public void remNews(String v) {
      if(localUser.getType() == TypeUser.ADMIN) {
         Session session = null;
         try{
            session = HibernateService.getSessionFactory().getCurrentSession();
            if(!session.getTransaction().isActive() || session.getTransaction() == null)
            {
               session.beginTransaction();
            }
            List<News> products = (List<News>) session.createQuery("from News").list();
            session.getTransaction().commit();
            for(int bt = products.size()-1;bt > -1; bt--)
            {
               if(products.get(bt).gethash().equals(v)) {
                  database.unregister(products.get(bt));
               }
            }
            Main.ininews();
            lobbysServices.chCommandToAllUsers(Type.LOBBY,"lsl",v);
         }catch (Exception e) {
            if(session.getTransaction() != null) {
               session.getTransaction().rollback();
            }

            e.printStackTrace();
            RemoteDatabaseLogger.error(e);
         }
      }
   }

   public void addNews(String v,String v1,String v2) {
      if(localUser.getType() == TypeUser.ADMIN) {
         News n = new News();
         n.setDat(v);
         n.setZa(v1);
         n.setTex(v2);
         String passSymbols = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         Random random = new Random();
         String password = "";
         for (int i = 0; i < 10; i++) {
            password += passSymbols.charAt(random.nextInt(passSymbols.length()));
         }
         n.sethash(password);
         n.setno(true);
         this.database.register(n);
         Main.ininews();
         this.send(Type.LOBBY, "nami;" + password + ";");
      }
   }

   public void parseChalleng() {
       if(!chal) {
           JSONObject json = new JSONObject();
           json.put("ye", Main.ych);
           if (Main.ych) {
               json.put("bp", this.localUser.bp);
               json.put("zv", this.localUser.zv);
               json.put("mzv", Main.cha.get(Main.cha.size() - this.localUser.part).gzv());
               JSONArray ar = new JSONArray();
               for (int bt = Main.cha.size() - 1; bt > -1; bt--) {
                       if (Main.cha.get(bt).gzv() > this.localUser.zv && Main.cha.get(bt).gzv() <= this.localUser.zv) {
                           if (bt > this.localUser.part) {
                               if (Main.cha.get(bt).getTpe() == 3) {
                                   addCrystall(Main.cha.get(bt).getcount());
                               }
                               if (Main.cha.get(bt).getTpe() == 2) {
                                   sPXT(Main.cha.get(bt).gpid());
                               }
                               if (Main.cha.get(bt).getTpe() == 1) {
                                   sP(Main.cha.get(bt).gpid());
                               }
                               if (Main.cha.get(bt).getTpe() == 0) {
                                   sPI(Main.cha.get(bt).gpid(), Main.cha.get(bt).getcount());
                               }
                               if (this.localUser.bp) {
                                   if (Main.cha.get(bt).getTpe() == 3) {
                                       addCrystall(Main.cha.get(bt).getcount1());
                                   }
                                   if (Main.cha.get(bt).getTpe() == 2) {
                                       sPXT(Main.cha.get(bt).gpid1());
                                   }
                                   if (Main.cha.get(bt).getTpe() == 1) {
                                       sP(Main.cha.get(bt).gpid1());
                                   }
                                   if (Main.cha.get(bt).getTpe() == 0) {
                                       sPI(Main.cha.get(bt).gpid1(), Main.cha.get(bt).getcount1());
                                   }
                               }
                           }
                           this.localUser.part = Main.cha.size() - bt;
                       }
                       JSONObject ob = new JSONObject();
                       ob.put("id", bt);
                       ob.put("nazv", Main.cha.get(bt).getNazv());
                       ob.put("type", Main.cha.get(bt).getTpe());
                       ob.put("sort", 0);
                       ob.put("count", Main.cha.get(bt).getcount());
                       ob.put("prew", Main.cha.get(bt).getprew());
                       ob.put("id1", Main.cha.get(bt).getId());
                       ob.put("nazv1", Main.cha.get(bt).getNazv1());
                       ob.put("type1", Main.cha.get(bt).getType1());
                       ob.put("sort1", 0);
                       ob.put("count1", Main.cha.get(bt).getcount1());
                       ob.put("prew1", Main.cha.get(bt).getprew1());
                       ar.add(ob);
               }
               json.put("part", ar);
               json.put("pa", this.localUser.part);
               json.put("cu", (Main.endch - System.currentTimeMillis())/1000);
               json.put("ti", 43200);
           }
           this.send(Type.LOBBY, "init_ch;" + json.toJSONString() + ";");
           database.update(this.localUser);
           chal = true;
       }
   }

   public void parseNews() {
         this.send(Type.LOBBY,"remn");
         JSONObject json = new JSONObject();
         JSONArray jarray = new JSONArray();
         JSONArray jarray1 = new JSONArray();
         for(int bt = Main.lisno.size()-1;bt > -1; bt--)
         {
            if(Main.lisno.get(bt).getClass().getName().equals("users.news.News")) {
               News f = (News)Main.lisno.get(bt);
                JSONObject jso = new JSONObject();
                jso.put("da",f.getDat());
                jso.put("ou",f.getZa());
                jso.put("out",f.getTex());
                jso.put("b",f.gethash());
                jarray.add(jso);
            }else{
               Opros f = (Opros)Main.lisno.get(bt);
                JSONObject jo = new JSONObject();
                jo.put("da",f.getDat());
                jo.put("ou",f.getZa());
                jo.put("out",f.getTex());
                jo.put("b",f.gethash());
                jo.put("j",f.getclines());
                jarray1.add(jo);
            }
         }
         json.put("n",jarray);
         json.put("o",jarray1);
         this.send(Type.LOBBY, "addno;" + json.toJSONString() + ";");
         this.send(Type.LOBBY, "pao;" + localUser.opro + ";");
   }

   private void sPI(final String itemId,int count) {
         if (this.localUser.getGarage().buyItem(itemId, count, this) != null) {
            this.localUser.getGarage().parseJSONData();
            LobbyManager.database.update(this.localUser.getGarage());
         }
   }

   private void sP(final String itemId) {
         if (this.localUser.getGarage().buyItem(itemId, 1, this) != null) {
            this.localUser.getGarage().parseJSONData();
            LobbyManager.database.update(this.localUser.getGarage());
         }
   }

   private void sPXT(final String itemId) {
      Iterator<Item> var3 = this.localUser.getGarage().items.iterator();
      while (var3.hasNext()) {
         Item item = var3.next();
         if (item.getId().equals(itemId)) {
            item.rot = true;
            item.mx = true;
            this.localUser.getGarage().parseJSONData();
            this.send(Type.GARAGE, "skins", JSONUtils.parseSkin(item));
            this.send(Type.GARAGE, "mount_item", StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)), item.mx ? StringUtils.concatStrings(item.id, "_XT") : StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)));
            database.update(this.localUser.getGarage());
         }
      }
   }

   private void enterInBattleBySpectator(final String battleId) {
      final BattleInfo battle = BattlesList.getBattleInfoById(battleId);
      if (battle == null) {
         return;
      }
      battle.model.spectatorModel.addSpectator(this.spectatorController = new SpectatorController(this, battle.model, battle.model.spectatorModel));
      this.localUser.setUserLocation(UserLocation.BATTLE);
      this.send(Type.BATTLE, "init_battle_model", JSONUtils.parseBattleModelInfo(battle, true,this));
      Logger.log("User " + this.localUser.getNickname() + " enter in battle by spectator.");
   }

   private void skin(final String id) {
      try {
         Iterator<Item> var3 = this.localUser.getGarage().items.iterator();

         while(var3.hasNext()) {
            Item item = var3.next();
            if(item.getId().equals(id)) {
               this.send(Type.GARAGE, "skins", JSONUtils.parseSkin(item));
            }
         }
      }catch (NullPointerException upd){
      }
   }

   private void sskin(final String id) {
      try {
         Iterator<Item> var3 = this.localUser.getGarage().items.iterator();

         while(var3.hasNext()) {
            Item item = var3.next();
            if(item.getId().equals(id)) {
               item.mx = !item.mx;
               this.localUser.getGarage().parseJSONData();
               this.send(Type.GARAGE, "skins", JSONUtils.parseSkin(item));
               this.send(Type.GARAGE, "mount_item", StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)),item.mx? StringUtils.concatStrings(item.id, "_XT"): StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)));
               database.update(this.localUser.getGarage());
            }
         }
      }catch (NullPointerException upd){
      }
   }

   private void bskin(final String id) {
      try {
         if(this.localUser.getCrystall()>=500000) {
            Iterator<Item> var3 = this.localUser.getGarage().items.iterator();

            while(var3.hasNext()) {
               Item item = var3.next();
               if (item.getId().equals(id)) {
                  item.rot = true;
                  item.mx = true;
                  this.localUser.getGarage().parseJSONData();
                  this.send(Type.GARAGE, "skins", JSONUtils.parseSkin1());
                  this.send(Type.GARAGE, "mount_item", StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)),item.mx? StringUtils.concatStrings(item.id, "_XT"): StringUtils.concatStrings(item.id, "_m", String.valueOf(item.modificationIndex)));
                  this.addCrystall(-500000);
               }
            }
            database.update(this.localUser.getGarage());
         }
      }catch (NullPointerException upd){
      }
   }

   private void sendTableMessage(final String msg) {
      this.send(Type.LOBBY, "server_message", msg);
   }

   private void tryCreateCTFBattle(final String json) {
      if (System.currentTimeMillis() - this.localUser.getAntiCheatData().lastTimeCreationBattle <= 300000L) {
         if (this.localUser.getAntiCheatData().countCreatedBattles >= 3) {
            if (this.localUser.getAntiCheatData().countWarningForFludCreateBattle >= 5) {
               this.kick();
            }
            this.sendTableMessage("\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u043e\u0437\u0434\u0430\u0432\u0430\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u0435\u0435 \u0442\u0440\u0435\u0445 \u0431\u0438\u0442\u0432 \u0432 \u0442\u0435\u0447\u0435\u043d\u0438\u0438 5 \u043c\u0438\u043d\u0443\u0442.");
            final AntiCheatData antiCheatData = this.localUser.getAntiCheatData();
            ++antiCheatData.countWarningForFludCreateBattle;
            return;
         }
      }
      else {
         this.localUser.getAntiCheatData().countCreatedBattles = 0;
         this.localUser.getAntiCheatData().countWarningForFludCreateBattle = 0;
      }
      JSONObject parser = null;
      try {
         parser = (JSONObject)new JSONParser().parse(json);
      }
      catch (ParseException e) {
         e.printStackTrace();
      }
      final BattleInfo battle = new BattleInfo();
      battle.battleType = "CTF";
      battle.isPaid = (boolean)parser.get("pay");
      battle.isPrivate = (boolean)parser.get("privateBattle");
      battle.friendlyFire = (boolean)parser.get("frielndyFire");
      battle.name = (String)parser.get("gameName");
      battle.map = MapsLoader.maps.get(parser.get("mapId"));
      battle.maxPeople = (int)(long)parser.get("numPlayers");
      battle.numFlags = (int)(long)parser.get("numFlags");
      battle.minRank = (int)(long)parser.get("minRang");
      battle.maxRank = (int)(long)parser.get("maxRang");
      battle.team = true;
      battle.time = (int)(long)parser.get("time");
      battle.autobalance = (boolean)parser.get("autoBalance");
      battle.inventory = (boolean)parser.get("inventory");
      final Map map = battle.map;
      if (battle.maxRank < battle.minRank) {
         battle.maxRank = battle.minRank;
      }
      if (battle.maxPeople < 2) {
         battle.maxPeople = 2;
      }
      if (battle.time <= 0 && battle.numFlags <= 0) {
         battle.time = 15;
         battle.numFlags = 0;
      }
      if (battle.maxPeople > map.maxPlayers) {
         battle.maxPeople = map.maxPlayers;
      }
      if (battle.numKills > 999) {
         battle.numKills = 999;
      }
      BattlesList.tryCreateBatle(battle,this);
      this.localUser.getAntiCheatData().lastTimeCreationBattle = System.currentTimeMillis();
      final AntiCheatData antiCheatData2 = this.localUser.getAntiCheatData();
      ++antiCheatData2.countCreatedBattles;
   }

   private void tryCreateTDMBattle(final String json) {
      if (System.currentTimeMillis() - this.localUser.getAntiCheatData().lastTimeCreationBattle <= 300000L) {
         if (this.localUser.getAntiCheatData().countCreatedBattles >= 3) {
            if (this.localUser.getAntiCheatData().countWarningForFludCreateBattle >= 5) {
               this.kick();
            }
            this.sendTableMessage("\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u043e\u0437\u0434\u0430\u0432\u0430\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u0435\u0435 \u0442\u0440\u0435\u0445 \u0431\u0438\u0442\u0432 \u0432 \u0442\u0435\u0447\u0435\u043d\u0438\u0438 5 \u043c\u0438\u043d\u0443\u0442.");
            final AntiCheatData antiCheatData = this.localUser.getAntiCheatData();
            ++antiCheatData.countWarningForFludCreateBattle;
            return;
         }
      }
      else {
         this.localUser.getAntiCheatData().countCreatedBattles = 0;
         this.localUser.getAntiCheatData().countWarningForFludCreateBattle = 0;
      }
      JSONObject parser = null;
      try {
         parser = (JSONObject)new JSONParser().parse(json);
      }
      catch (ParseException e) {
         e.printStackTrace();
      }
      final BattleInfo battle = new BattleInfo();
      battle.battleType = "TDM";
      battle.isPaid = (boolean)parser.get("pay");
      battle.isPrivate = (boolean)parser.get("privateBattle");
      battle.friendlyFire = (boolean)parser.get("frielndyFire");
      battle.name = (String)parser.get("gameName");
      battle.map = MapsLoader.maps.get(parser.get("mapId"));
      battle.maxPeople = (int)(long)parser.get("numPlayers");
      battle.numKills = (int)(long)parser.get("numKills");
      battle.minRank = (int)(long)parser.get("minRang");
      battle.maxRank = (int)(long)parser.get("maxRang");
      battle.team = true;
      battle.time = (int)(long)parser.get("time");
      battle.inventory = (boolean)parser.get("inventory");
      battle.autobalance = (boolean)parser.get("autoBalance");
      final Map map = battle.map;
      if (battle.maxRank < battle.minRank) {
         battle.maxRank = battle.minRank;
      }
      if (battle.maxPeople < 2) {
         battle.maxPeople = 2;
      }
      if (battle.time <= 0 && battle.numKills <= 0) {
         battle.time = 900;
         battle.numKills = 0;
      }
      if (battle.maxPeople > map.maxPlayers) {
         battle.maxPeople = map.maxPlayers;
      }
      if (battle.numKills > 999) {
         battle.numKills = 999;
      }
      BattlesList.tryCreateBatle(battle,this);
      this.localUser.getAntiCheatData().lastTimeCreationBattle = System.currentTimeMillis();
      final AntiCheatData antiCheatData2 = this.localUser.getAntiCheatData();
      ++antiCheatData2.countCreatedBattles;
   }

   public void onExitFromBattle() {
      if (this.battle != null) {
         if (this.autoEntryServices.removePlayer(this.battle.battle, this.getLocalUser().getNickname(), this.battle.playerTeamType, this.battle.battle.battleInfo.team)) {
            this.battle.destroy(true);
         }
         else {
            this.battle.destroy(false);
         }
         this.battle = null;
         this.disconnectListener.removeListener(this.battle);
      }
      if (this.spectatorController != null) {
         this.spectatorController.onDisconnect();
         this.spectatorController = null;
      }
      this.localUser.idbat = "";
      LobbyManager.lobbysServices.chCommandToAllUsers(Type.LOBBY,"addon",this.localUser.getNickname());
      this.send(Type.LOBBY_CHAT, "init_messages", JSONUtils.parseChatLobbyMessages(LobbyManager.chatLobby.getMessages()));
      this.parseNews();
      this.parseChalleng();
   }

   public void onExitFromStatistic() {
      this.onExitFromBattle();
      this.sendMapsInit();
   }

   public void zacet(String cod) {
      Payment pay = database.getPayByCod(cod);
      try {
         Iterator<Item> var3 = this.localUser.getGarage().items.iterator();

         while(var3.hasNext()) {
            Item item = var3.next();
            if(item.id.equals("double_crystalls")) {
               this.addCrystall(pay.getSum());
            }
         }
         this.addCrystall(pay.getSum());
         this.send(Type.LOBBY, "pa", JSONUtils.parsePa(pay.getSum()));
         database.DelPayByCod(pay);
      }catch (NullPointerException kk){

      }
   }

   private double roundResult (double d1, int precise) {
      DecimalFormat df = new DecimalFormat("#.##");
      df.setRoundingMode(RoundingMode.CEILING);
      //Double d = new Double("1");
      for (Number n : Arrays.asList(d1)) {
         d1 = n.doubleValue();
      }
      return Double.parseDouble(df.format(d1).replace(",","."));
      //return d1;
   }

   private void onEnterInTeamBattle(final String battleId, final boolean red) {

      this.localUser.idbat = battleId;
      this.localUser.setUserLocation(UserLocation.BATTLE);
      if (this.battle != null) {
         return;
      }
      final BattleInfo battleInfo = BattlesList.getBattleInfoById(battleId);
      //this.localUser.nbat = battleInfo.name;
      if (battleInfo == null) {
         return;
      }
      if (battleInfo.model.players.size() >= battleInfo.maxPeople * 2) {
         return;
      }
      if (red) {
         final BattleInfo battleInfo2 = battleInfo;
         ++battleInfo2.redPeople;
      }
      else {
         final BattleInfo battleInfo3 = battleInfo;
         ++battleInfo3.bluePeople;
      }
      LobbyManager.lobbysServices.chCommandToAllUsers(Type.LOBBY,"addon",this.localUser.getNickname());
      this.send(Type.BATTLE, "init_battle_model", JSONUtils.parseBattleModelInfo(battleInfo, false,this));
      this.battle = new BattlefieldPlayerController(this, battleInfo.model, red ? "RED" : "BLUE");
      this.disconnectListener.addListener(this.battle);
      LobbyManager.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "update_count_users_in_team_battle", JSONUtils.parseUpdateCoundPeoplesCommand(battleInfo) });
      LobbyManager.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "add_player_to_battle", JSONUtils.parseAddPlayerComand(this.battle, battleInfo) });
   }

   private void onEnterInBattle(final String battleId) {
      this.localUser.setUserLocation(UserLocation.BATTLE);
      this.autoEntryServices.removePlayer(this.getLocalUser().getNickname());
      if (this.battle != null) {
         return;
      }
      final BattleInfo battleInfo = BattlesList.getBattleInfoById(battleId);
      this.localUser.idbat = battleId;
      //this.localUser.nbat = battleInfo.name;
      if (battleInfo == null) {
         return;
      }
      LobbyManager.lobbysServices.chCommandToAllUsers(Type.LOBBY,"addon",this.localUser.getNickname());
      if (battleInfo.model.players.size() >= battleInfo.maxPeople) {
         return;
      }
      this.send(Type.BATTLE, "init_battle_model", JSONUtils.parseBattleModelInfo(battleInfo, false,this));
      this.battle = new BattlefieldPlayerController(this, battleInfo.model, "NONE");
      this.disconnectListener.addListener(this.battle);
      final BattleInfo battleInfo2 = battleInfo;
      ++battleInfo2.countPeople;
      if (!battleInfo.team) {
         LobbyManager.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[] { StringUtils.concatStrings(new String[] { "update_count_users_in_dm_battle", ";", battleId, ";", String.valueOf(this.battle.battle.battleInfo.countPeople) }) });
      }
      else {
         LobbyManager.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "update_count_users_in_team_battle", JSONUtils.parseUpdateCoundPeoplesCommand(battleInfo) });
      }
      LobbyManager.lobbysServices.sendCommandToAllUsers(Type.LOBBY, UserLocation.BATTLESELECT, new String[] { "add_player_to_battle", JSONUtils.parseAddPlayerComand(this.battle, battleInfo) });
   }

   public void sendBattleInfo(final String id) {
      this.send(Type.LOBBY, "show_battle_info", JSONUtils.parseBattleInfoShow(BattlesList.getBattleInfoById(id), this.getLocalUser().getType() != TypeUser.DEFAULT && this.getLocalUser().getType() != TypeUser.TESTER));
   }

   public void sendIn(String n,String r,String id) {
      this.send(Type.LOBBY, "inv", JSONUtils.parseInv(n, r, id));
   }

   private void delnFriend(String uid1) {
      User jj = database.getUserById(uid1);
      jj.setFriend(database.getFriendByUser(jj));
      dedrj(this.localUser.getNickname(), jj.getFriend());
      dedrj(uid1, this.localUser.getFriend());
      database.update(this.localUser.getFriend());
      database.update(jj.getFriend());
      try
      {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(localUser.getFriend()));
         for(int hm = 0; hm< Main.opdf.size();hm++)
         {
            if(Main.opdf.get(hm).localUser.getNickname().equals(uid1)) {
               Main.opdf.get(hm).send(Type.LOBBY, "init_f", JSONUtils.parseFriend(jj.getFriend()));
            }
         }
      }catch (NullPointerException g) {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(localUser.getFriend()));
      }
   }

   private void delFriend(String uid1) {
      User jj = database.getUserById(uid1);
      jj.setFriend(database.getFriendByUser(jj));
      Vector<String> fgre = dedrj(uid1,this.localUser.getFriend()).get(2);
      fgre.addElement(uid1);
      this.localUser.getFriend().setOutt(fgre);
      database.update(this.localUser.getFriend());
      Vector<String> fgre1 = dedrj(this.localUser.getNickname(),jj.getFriend()).get(1);
      fgre1.addElement(this.localUser.getNickname());
      jj.getFriend().setInn(fgre1);
      database.update(jj.getFriend());
      try
      {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
         for(int hm = 0; hm< Main.opdf.size();hm++)
         {
            if(Main.opdf.get(hm).localUser.getNickname().equals(uid1)) {
               Main.opdf.get(hm).send(Type.LOBBY, "init_f", JSONUtils.parseFriend(jj.getFriend()));
            }
         }
      }catch (NullPointerException g) {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
      }
   }

   private void gotFriend(String uid1) {
      User jj = database.getUserById(uid1);
      jj.setFriend(database.getFriendByUser(jj));
      Vector<String> fgre = dedrj(uid1,this.localUser.getFriend()).get(1);
      fgre.addElement(uid1);
      this.localUser.getFriend().setInn(fgre);
      database.update(this.localUser.getFriend());
      Vector<String> fgre1 = dedrj(this.localUser.getNickname(),jj.getFriend()).get(2);
      fgre1.addElement(this.localUser.getNickname());
      jj.getFriend().setOutt(fgre1);
      database.update(jj.getFriend());
      try
      {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
         for(int hm = 0; hm< Main.opdf.size();hm++)
         {
            if(Main.opdf.get(hm).localUser.getNickname().equals(uid1)) {
               Main.opdf.get(hm).send(Type.LOBBY, "init_f", JSONUtils.parseFriend(jj.getFriend()));
            }
         }
      }catch (NullPointerException g) {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
      }
   }

   private void makeFriend(String uid1) {
      User jj = database.getUserById(uid1);
      jj.setFriend(database.getFriendByUser(jj));
      Vector<String> fgre = dedrj(uid1,this.localUser.getFriend()).get(0);
      fgre.addElement(uid1);
      this.localUser.getFriend().setDR(fgre);
      database.update(this.localUser.getFriend());
      Vector<String> fgre1 = dedrj(this.localUser.getNickname(),jj.getFriend()).get(0);
      fgre1.addElement(this.localUser.getNickname());
      jj.getFriend().setDR(fgre1);
      database.update(jj.getFriend());
      try
      {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
         for(int hm = 0; hm< Main.opdf.size();hm++)
         {
            if(Main.opdf.get(hm).localUser.getNickname().equals(uid1)) {
               Main.opdf.get(hm).send(Type.LOBBY, "init_f", JSONUtils.parseFriend(jj.getFriend()));
            }
         }
      }catch (NullPointerException g) {
         this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(this.localUser.getFriend()));
      }
   }

   private Vector<Vector<String>> dedrj(String uid1, Friends fg) {
      Vector<String> fd = fg.getDrV();
      Vector<String> fd1 = fg.getInnV();
      Vector<String> fd2 = fg.getOuttV();
      for(int f = 0; f < fd.size(); f++)
      {
         if(fd.get(f).equals(uid1))
         {
            fd.remove(f);
         }
      }
      for(int f1 = 0; f1 < fd1.size(); f1++)
      {
         if(fd1.get(f1).equals(uid1))
         {
            fd1.remove(f1);
         }
      }
      for(int f2 = 0; f2 < fd2.size(); f2++)
      {
         if(fd2.get(f2).equals(uid1))
         {
            fd2.remove(f2);
         }
      }
      fg.setInn(fd1);
      fg.setDR(fd);
      fg.setOutt(fd2);
      fd = fg.getDrV();
      fd1 = fg.getInnV();
      fd2 = fg.getOuttV();
      Vector<Vector<String>> gkl = new Vector<Vector<String>>();
      gkl.add(fd);
      gkl.add(fd1);
      gkl.add(fd2);
      return gkl;
   }

   private void sendRang(final String id, String uid, String type) {
      try {
         this.send(Type.LOBBY, "rang", JSONUtils.parseR(database.getUserById(uid).getRang(), id, type));
      }catch (NullPointerException upd){
      }
   }

   private void tryCreateBattleDM(final String gameName, final String mapId, int time, int kills, int maxPlayers, final int minRang, int maxRang, final boolean isPrivate, final boolean pay, final boolean mm) {
      if (System.currentTimeMillis() - this.localUser.getAntiCheatData().lastTimeCreationBattle <= 300000L) {
         if (this.localUser.getAntiCheatData().countCreatedBattles >= 3) {
            if (this.localUser.getAntiCheatData().countWarningForFludCreateBattle >= 5) {
               this.kick();
            }
            this.sendTableMessage("\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u0441\u043e\u0437\u0434\u0430\u0432\u0430\u0442\u044c \u043d\u0435 \u0431\u043e\u043b\u0435\u0435 \u0442\u0440\u0435\u0445 \u0431\u0438\u0442\u0432 \u0432 \u0442\u0435\u0447\u0435\u043d\u0438\u0438 5 \u043c\u0438\u043d\u0443\u0442.");
            final AntiCheatData antiCheatData = this.localUser.getAntiCheatData();
            ++antiCheatData.countWarningForFludCreateBattle;
            return;
         }
      }
      else {
         this.localUser.getAntiCheatData().countCreatedBattles = 0;
         this.localUser.getAntiCheatData().countWarningForFludCreateBattle = 0;
      }
      final BattleInfo battle = new BattleInfo();
      final Map map = MapsLoader.maps.get(mapId);
      if (maxRang < minRang) {
         maxRang = minRang;
      }
      if (maxPlayers < 2) {
         maxPlayers = 2;
      }
      if (time <= 0 && kills <= 0) {
         time = 900;
         kills = 0;
      }
      if (maxPlayers > map.maxPlayers) {
         maxPlayers = map.maxPlayers;
      }
      if (kills > 999) {
         kills = 999;
      }
      battle.battleType = "DM";
      battle.name = gameName;
      battle.map = MapsLoader.maps.get(mapId);
      battle.time = time;
      battle.numKills = kills;
      battle.maxPeople = maxPlayers;
      battle.minRank = minRang;
      battle.countPeople = 0;
      battle.maxRank = maxRang;
      battle.team = false;
      battle.isPrivate = isPrivate;
      battle.isPaid = pay;
      battle.inventory = mm;
      BattlesList.tryCreateBatle(battle,this);
      this.localUser.getAntiCheatData().lastTimeCreationBattle = System.currentTimeMillis();
      final AntiCheatData antiCheatData2 = this.localUser.getAntiCheatData();
      ++antiCheatData2.countCreatedBattles;
   }

   private void checkBattleName(final String name) {
      this.send(Type.LOBBY, "check_battle_name", name);
   }

   private void sendMapsInit() {
      this.localUser.setUserLocation(UserLocation.BATTLESELECT);
      this.send(Type.LOBBY, "init_battle_select", JSONUtils.parseBattleMapList(this));
   }

   private void sendFriend() {
      this.localUser.setUserLocation(UserLocation.ALL);
      this.send(Type.LOBBY, "init_f", JSONUtils.parseFriend(localUser.getFriend()));
   }

   public void sendGarage() {
      this.localUser.setUserLocation(UserLocation.GARAGE);
      this.send(Type.GARAGE, "init_market1", JSONUtils.parseMarketItemsTan(this.localUser));
      this.send(Type.GARAGE, "init_market2", JSONUtils.parseMarketItemsCol(this.localUser));
      this.send(Type.GARAGE, "init_market3", JSONUtils.parseMarketItemsInv(this.localUser));
      this.send(Type.GARAGE, "init_market4", JSONUtils.parseMarketItemsKit(this.localUser));
      this.send(Type.GARAGE, "init_market5", JSONUtils.parseMarketItemsPlugin(this.localUser));
      this.send(Type.GARAGE, "init_market6", JSONUtils.parseMarketItemsPuh(this.localUser));
      this.send(Type.GARAGE, "init_garage_items1", JSONUtils.parseGarageUserTan(this.localUser));
      this.send(Type.GARAGE, "init_garage_items2", JSONUtils.parseGarageUserCol(this.localUser));
      this.send(Type.GARAGE, "init_garage_items3", JSONUtils.parseGarageUserInv(this.localUser));
      this.send(Type.GARAGE, "init_garage_items4", JSONUtils.parseGarageUserKit(this.localUser));
      this.send(Type.GARAGE, "init_garage_items5", JSONUtils.parseGarageUserPlu(this.localUser));
      this.send(Type.GARAGE, "init_garage_items6", JSONUtils.parseGarageUserPuh(this.localUser));
   }

   public synchronized void onTryUpdateItem(final String id) {
      final Item item = this.localUser.getGarage().getItemById(id.substring(0, id.length() - 3));
      final int modificationID = Integer.parseInt(id.substring(id.length() - 1));
      if (this.checkMoney(item.modifications[modificationID + 1].price)) {
         if (this.getLocalUser().getRang() + 1 < item.modifications[modificationID + 1].rank) {
            return;
         }
         if (this.localUser.getGarage().updateItem(id)) {
            this.send(Type.GARAGE, "update_item", id);
            this.addCrystall(-item.modifications[modificationID + 1].price);
            this.localUser.getGarage().parseJSONData();
            LobbyManager.database.update(this.localUser.getGarage());
         }
      }
      else {
         this.send(Type.GARAGE, "try_update_NO");
      }
   }

   public synchronized void onTryBuyItem(final String itemId, final int count) {
      if (count <= 0 || count > 9999) {
         this.crystallToZero();
         return;
      }
      final Item item = GarageItemsLoader.items.get(itemId.substring(0, itemId.length() - 3));
      final int price = item.price * count;
      final int itemRang = item.modifications[0].rank;
      if (this.checkMoney(price)) {
         if (this.getLocalUser().getRang() + 1 < itemRang) {
            return;
         }
         if (this.localUser.getGarage().buyItem(itemId, count, this) != null) {
            this.addCrystall(-price);
            this.localUser.getGarage().parseJSONData();
            LobbyManager.database.update(this.localUser.getGarage());
         }
         else {
            this.send(Type.GARAGE, "try_buy_item_NO");
         }
      }
   }

   private boolean checkMoney(final int buyValue) {
      return this.localUser.getCrystall() - buyValue >= 0;
   }

   public synchronized void addCrystall(final int value) {
      this.localUser.addCrystall(value);
      this.send(Type.LOBBY, "add_crystall", String.valueOf(this.localUser.getCrystall()));
      LobbyManager.database.update(this.localUser);
   }

   public void crystallToZero() {
      this.localUser.setCrystall(0);
      this.send(Type.LOBBY, "add_crystall", String.valueOf(this.localUser.getCrystall()));
      LobbyManager.database.update(this.localUser);
   }

   private boolean stringToBoolean(final String src) {
      return src.toLowerCase().equals("true");
   }

   public void onDisconnect() {
      LobbyManager.database.uncache(this.localUser.getNickname());
      LobbyManager.lobbysServices.removeLobby(this);
      OnlineStats.reminOnline(this.localUser.getNickname());
      OnlineStats.removeOnline();
      if (this.spectatorController != null) {
         this.spectatorController.onDisconnect();
         this.spectatorController = null;
      }
      if (this.battle != null) {
         this.battle.onDisconnect();
         this.battle = null;
      }
      this.localUser.session = null;
      chal = false;
   }

   public void kick() {
      this.networker.closeConnection();
   }

   public User getLocalUser() {
      return this.localUser;
   }

   public void setLocalUser(final User localUser) {
      this.localUser = localUser;
   }

   public FloodController getChatFloodController() {
      return this.chatFloodController;
   }

   public void setChatFloodController(final FloodController chatFloodController) {
      this.chatFloodController = chatFloodController;
   }
}