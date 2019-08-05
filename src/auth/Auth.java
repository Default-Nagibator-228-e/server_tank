package auth;

import users.anticheat.AntiCheatData;
import utils.RankUtils;
import commands.Command;
import commands.Type;
import groups.UserGroupsLoader;
import json.JSONUtils;
import lobby.LobbyManager;
import lobby.chat.ChatLobby;
import logger.Logger;
import logger.remote.RemoteDatabaseLogger;
import main.Main;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import main.netty.ProtocolTransfer;
import network.Session;
import services.AutoEntryServices;
import services.annotations.ServicesInject;
import system.localization.Localization;
import users.User;
import users.karma.Karma;
import java.util.regex.Pattern;
import org.jboss.netty.channel.ChannelHandlerContext;

public class Auth extends AuthComandsConst {
   private ProtocolTransfer transfer;
   private ChannelHandlerContext context;
    private User us;
   private Localization localization;
   @ServicesInject(
      target = DatabaseManagerImpl.class
   )
   private DatabaseManager database = DatabaseManagerImpl.instance();
   @ServicesInject(
      target = ChatLobby.class
   )
   private ChatLobby chatLobby = ChatLobby.getInstance();
   @ServicesInject(
      target = AutoEntryServices.class
   )
   private AutoEntryServices autoEntryServices = AutoEntryServices.instance();

   public Auth(ProtocolTransfer transfer, ChannelHandlerContext context) {
      this.transfer = transfer;
      this.context = context;
   }

   public void executeCommand(Command command) {
      try {
         String ex;
         String password;
         User newUser;
         switch (command.type) {
             default:
                 break;
             case AUTH:
                 switch (command.args[0]) {
                     case "go":
                         this.onPasswordAccept();
                         break;
                     case "log":
                         password = command.args[2];
                         if (command.args[1].length() > 50) {
                             command.args[1] = null;
                             break;
                         }

                         if (password.length() > 50) {
                             password = null;
                             break;
                         }

                         newUser = this.database.getUserById(command.args[1]);

                         if (newUser == null) {
                             this.transfer.send(Type.AUTH, new String[]{"not_exist"});
                             break;
                         }

                         if (newUser.session != null) {
                             if (newUser.session.getIp() != this.transfer.getIP()) {
                                 this.transfer.closeConnection();
                             }else{
                                 this.transfer.closeConnection();
                                 newUser.session = null;
                                 this.database.update(newUser);
                             }
                             break;
                         }

                         if (!newUser.getPassword().equals(password)) {
                             Logger.log("The user " + newUser.getNickname() + " has not been logged. Password deined.");
                             this.transfer.send(Type.AUTH, new String[]{"denied"});
                             break;
                         }
                         Karma ex1 = this.database.getKarmaByUser(newUser);
                         newUser.setKarma(ex1);
                         if (ex1.isGameBlocked()) {
                             this.transfer.send(Type.AUTH, new String[]{"ban", ex1.getReasonGameBan()});
                             break;
                         }
                         newUser.getAntiCheatData().ip = this.transfer.getIP();
                         newUser.session = new Session(this.transfer, this.context);
                         this.database.cache(newUser);
                         newUser.setFriend(this.database.getFriendByUser(newUser));
                         newUser.setGarage(this.database.getGarageByUser(newUser));
                         newUser.getGarage().unparseJSONData();
                         newUser.setUserGroup(UserGroupsLoader.getUserGroup(newUser.getType()));
                         Logger.log("The user " + newUser.getNickname() + " has been logged. Password accept.");
                         this.transfer.send(Type.AUTH, "accept;" + newUser.getType().toString() + ";");
                         us = newUser;
                 }
                 break;
             case REGISTRATON :
                 switch (command.args[0]) {
                     case "check_name" :
                         ex = command.args[1];
                         boolean password2 = this.database.contains(ex);
                         boolean newUser1 = this.callsignNormal(ex);
                         this.transfer.send(Type.REGISTRATON, new String[]{"check_name_result", !password2 && newUser1 ? "not_exist" : "nickname_exist"});
                         break;
                     case "invite_check":
                         ex = command.args[1];
                         boolean password1 = this.database.getInvByCod(ex) != null;
                         this.transfer.send(Type.REGISTRATON, new String[]{"check_inv_result", password1 ? "not_exist" : "nickname_exist"});
                         break;
                     case "reg" :
                         ex = command.args[1];
                         password = command.args[2];
                         if (ex.length() > 50) {
                             ex = null;
                             break;
                         }

                         if (password.length() > 50) {
                             password = null;
                             break;
                         }

                         if (this.database.contains(ex)) {
                             this.transfer.send(Type.REGISTRATON, new String[]{"nickname_exist"});
                             break;
                         }

                         if (this.database.getInvByCod(command.args[3]) == null) {
                             this.transfer.send(Type.REGISTRATON, new String[]{"inv_exist"});
                             break;
                         } else {
                             this.database.DelInv(this.database.getInvByCod(command.args[3]));
                         }

                         if (this.callsignNormal(ex)) {
                             newUser = new User(ex, password);
                             newUser.invite = command.args[3];
                             AntiCheatData ds = new AntiCheatData();
                             ds.ip = this.transfer.getIP();
                             newUser.setAntiCheatData(ds);
                             newUser.setLastIP(this.transfer.getIP());
                             this.database.register(newUser);
                             this.transfer.send(Type.AUTH, "accept;" + newUser.getType().toString() + ";");
                             us = newUser;
                         } else {
                             this.transfer.closeConnection();
                         }
                 }
                 break;
             case SYSTEM :
                 switch (command.args[0]) {
                     case "init_location":
                         try {
                             this.localization = Localization.valueOf(command.args[1]);
                         }catch(Exception ee)
                         {}
                         break;
                     case "c01":
                         this.transfer.closeConnection();
                     }
         }
      } catch (Exception var5) {
         RemoteDatabaseLogger.error(var5);
      }

   }

   private boolean callsignNormal(String nick) {
      Pattern pattern = Pattern.compile("[a-zA-Z]\\w{5,30}");
      return pattern.matcher(nick).matches();
   }

   private void onPasswordAccept() {
      try {

         this.transfer.lobby = new LobbyManager(this.transfer, us);
         if(this.localization == null) {
            this.localization = Localization.RU;
         }

          us.setLocalization(this.localization);
         this.transfer.send(Type.LOBBY, new String[]{"init_panel", JSONUtils.parseUserToJSON(us)});
         this.transfer.send(Type.LOBBY, new String[]{"update_rang_progress", String.valueOf(RankUtils.getUpdateNumber(us.getScore()))});
         if(!this.autoEntryServices.needEnterToBattle(us)) {
            //this.transfer.send(Type.LOBBY, "init_battle_select", JSONUtils.parseBattleMapList());
            //this.transfer.send(Type.GARAGE, new String[]{"init_garage_items", JSONUtils.parseGarageUser(user).trim()});
            //this.transfer.send(Type.GARAGE, new String[]{"init_market", JSONUtils.parseMarketItems(user)});
            this.transfer.send(Type.LOBBY_CHAT, new String[]{"init_chat"});
            this.transfer.send(Type.LOBBY_CHAT, new String[]{"init_messages", JSONUtils.parseChatLobbyMessages(this.chatLobby.getMessages())});
            this.transfer.lobby.parseNews();
            this.transfer.lobby.parseChalleng();
         } else {
            this.transfer.send(Type.LOBBY, new String[]{"init_battlecontroller"});
            this.autoEntryServices.prepareToEnter(this.transfer.lobby);
            this.transfer.lobby.parseChalleng();
         }

         Main.opdf.add(this.transfer.lobby);
         this.transfer.send(Type.LOBBY, "ret;" + us.getRating() + ";" + us.getPlace() + ";");
          us.setLastIP(us.getAntiCheatData().ip);
         this.database.update(us);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
