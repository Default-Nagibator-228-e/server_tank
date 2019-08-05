//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lobby.chat;

import utils.StringUtils;
import commands.Type;
import json.JSONUtils;
import lobby.LobbyManager;
import lobby.battles.BattleInfo;
import lobby.battles.BattlesList;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import main.netty.NettyUsersHandler;
import main.params.OnlineStats;
import services.AutoEntryServices;
import services.BanServices;
import services.LobbysServices;
import services.TanksServices;
import services.annotations.ServicesInject;
import services.ban.BanChatCommads;
import services.ban.BanTimeType;
import services.ban.BanType;
import services.ban.DateFormater;
import services.ban.block.BlockGameReason;
import services.hibernate.HibernateService;
import system.timers.SystemTimerScheduler;
import users.User;
import users.garage.GarageItemsLoader;
import users.karma.Karma;
import users.locations.UserLocation;
import users.news.News;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class ChatLobby extends ChatLobbyComandsConst {
   private static final ChatLobby instance = new ChatLobby();
   private static final int MAX_SIZE_MESSAGES = 50;
   private static final int MAX_WARNING = 4;
   private ArrayList<ChatMessage> chatMessages = new ArrayList();
   private boolean stoped = false;
   @ServicesInject(
           target = TanksServices.class
   )
   private TanksServices tanksServices = TanksServices.getInstance();
   @ServicesInject(
           target = LobbysServices.class
   )
   private LobbysServices lobbyServices = LobbysServices.getInstance();
   @ServicesInject(
           target = DatabaseManagerImpl.class
   )
   private DatabaseManager database = DatabaseManagerImpl.instance();
   @ServicesInject(
           target = BanServices.class
   )
   private BanServices banServices = BanServices.getInstance();
   @ServicesInject(
           target = AutoEntryServices.class
   )
   private static AutoEntryServices autoEntryServices = AutoEntryServices.instance();

   public static ChatLobby getInstance() {
      return instance;
   }

   private ChatLobby() {
   }

   public void addMessage(ChatMessage msg) {
      this.checkSyntax(msg);
   }

   public void checkSyntax(ChatMessage msg) {
      msg.message = msg.message.trim();
      Karma karma = this.database.getKarmaByUser(msg.user);
      if (karma.isChatBanned()) {
         long currDate = System.currentTimeMillis();
         Date banTo = karma.getChatBannedBefore();
         long delta = currDate - banTo.getTime();
         if (delta <= 0L) {
            msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", StringUtils.concatStrings(new String[]{"Вы отключены от чата. Вы вернётесь в ЭФИР через ", DateFormater.formatTimeToUnban(delta), ". Причина: " + karma.getReasonChatBan()})});
            return;
         }
      }

      if (msg.message.startsWith("/")) {
         String temp = msg.message.replace('/', ' ').trim();
         String[] arguments = temp.split(" ");
         if (!msg.user.getUserGroup().isAvaliableChatCommand(arguments[0])) {
            msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Неизвестная команда!"});
            return;
         }
            String var28 = arguments[0];
            Boolean d = false;
            switch(var28) {
               case "addcry":
                     if (arguments.length >= 2) {
                        this.tanksServices.addCrystall(msg.localLobby, this.getInt(arguments[1]));
                     }
                     d = true;
                  break;
               case "wipe":
                     User dsg = msg.localLobby.getLocalUser();
                     dsg.getGarage().items = new ArrayList<>();
                     dsg.getGarage().items.add((GarageItemsLoader.items.get("smoky")).clone());
                     dsg.getGarage().items.add((GarageItemsLoader.items.get("hunter")).clone());
                     dsg.getGarage().items.add((GarageItemsLoader.items.get("green")).clone());
                     dsg.getGarage().items.add((GarageItemsLoader.items.get("holiday")).clone());
                     dsg.getGarage().mountItem("hunter_m0");
                     dsg.getGarage().mountItem("smoky_m0");
                     dsg.getGarage().mountItem("green_m0");
                     dsg.setGarage(dsg.getGarage());
                     DatabaseManagerImpl.instance().update(dsg.getGarage());
                     DatabaseManagerImpl.instance().update(dsg);
                     msg.localLobby.sendGarage();
                     d = true;
                  break;
               case "cleant":
                     if (arguments.length >= 2) {
                        this.cleanMessagesByText(StringUtils.concatMassive(arguments, 1));
                     }
                  d = true;
                  break;
               case "addscore":
                     if (arguments.length >= 2) {
                        int score = this.getInt(arguments[1]);
                        if (msg.localLobby.getLocalUser().getScore() + score < 0) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Ваше количество очков опыта не должно быть отрицательное!"});
                        } else {
                           this.tanksServices.addScore(msg.localLobby, score);
                        }
                     }
                  d = true;
                  break;
               case "online":
                     msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Current online: " + OnlineStats.getOnline() + "\nMax online: " + OnlineStats.getMaxOnline()});
                  d = true;
                  break;
               case "system":
                     if (arguments.length >= 2) {
                        this.sendSystemMessageToAll(arguments, false);
                     }
                  d = true;
                  break;
               case "unbanip":
                     if (arguments.length >= 2) {
                        User _victim = this.database.getUserById(arguments[1]);
                        if (_victim == null) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден"});
                           return;
                        }

                        NettyUsersHandler.unblock(_victim.getLastIP());
                     }
                  d = true;
                  break;
               case "w":
                     if (arguments.length < 3) {
                        return;
                     }

                     User giver = this.database.getUserById(arguments[1]);
                     if (giver == null) {
                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден!"});
                     } else {
                        String reason = StringUtils.concatMassive(arguments, 2);
                        this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкист ", giver.getNickname(), " предупрежден. Причина: ", reason}), false);
                     }
                  d = true;
                  break;
               case "kick":
                     if (arguments.length >= 2) {
                        User _userForKick = this.database.getUserById(arguments[1]);
                        if (_userForKick == null) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден"});
                        } else {
                           LobbyManager _lobby = this.lobbyServices.getLobbyByUser(_userForKick);
                           if (_lobby != null) {
                              _lobby.kick();
                              this.sendSystemMessageToAll(_userForKick.getNickname() + " кикнут", false);
                           }
                        }
                     }
                  d = true;
                  break;
               case "stop":
                     this.stoped = true;
                     this.sendSystemMessageToAll("Чат остановлен", false);
                  d = true;
                  break;
               case "warn":
                     this.sendSystemMessageToAll(arguments, true);
                  d = true;
                  break;
               case "banip":
                     if (arguments.length >= 2) {
                        User victim = this.database.getUserById(arguments[1]);
                        if (victim == null) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден"});
                           return;
                        }
                        LobbyManager l = this.lobbyServices.getLobbyByUser(victim);
                        NettyUsersHandler.block(victim.getLastIP());
                        try {
                           l.kick();
                        } catch (NullPointerException g){

                        }
                     }
                  d = true;
                  break;
               case "clean":
                     if (arguments.length >= 2) {
                        this.cleanMessagesByUser(arguments[1]);
                     }
                  d = true;
                  break;
               case "clear":
                     this.clear();
                  d = true;
                  break;
               case "getip":
                     if (arguments.length >= 2) {
                        User shower = this.database.getUserById(arguments[1]);
                        if (shower == null) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден"});
                           return;
                        }

                        String ip = shower.session.getIp();
                        if (ip == null) {
                           ip = shower.getLastIP();
                        }

                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "IP user " + shower.getNickname() + " : " + ip});
                     }
                  d = true;
                  break;
               case "start":
                     this.stoped = false;
                     this.sendSystemMessageToAll("Чат запущен", false);
                  d = true;
                  break;
               case "unban":
                     if (arguments.length >= 2) {
                        User cu = this.database.getUserById(arguments[1]);
                        if (cu == null) {
                           msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден!"});
                        } else {
                           this.banServices.unbanChat(cu);
                           this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкисту ", cu.getNickname(), " был разрешён выход в эфир"}), false);
                        }
                     }
                  d = true;
                  break;
               case "blockgame":
                     if (arguments.length < 3) {
                        return;
                     }

                     User victim_ = this.database.getUserById(arguments[1]);

                     int reasonId;
                     try {
                        reasonId = Integer.parseInt(arguments[2]);
                     } catch (Exception var24) {
                        reasonId = 0;
                     }

                     if (victim_ == null) {
                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден!"});
                     } else {
                        this.banServices.ban(BanType.GAME, BanTimeType.FOREVER, victim_, msg.user, BlockGameReason.getReasonById(reasonId).getReason());
                        LobbyManager lobby = this.lobbyServices.getLobbyByNick(victim_.getNickname());
                        if (lobby != null) {
                           lobby.kick();
                        }

                        this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкист ", victim_.getNickname(), " был заблокирован и кикнут"}), false);
                     }
                  d = true;
                  break;
               case "unblockgame":
                     if (arguments.length < 2) {
                        return;
                     }

                     User av = this.database.getUserById(arguments[1]);
                     if (av == null) {
                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден!"});
                     } else {
                        this.banServices.unblock(av);
                        this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкист ", av.getNickname(), " был разблокирован"}), false);
                     }
                  d = true;
                  break;
               case "rbattle":

                     StringBuilder id = new StringBuilder();

                     for(int i = 1; i < arguments.length; ++i) {
                        id.append(arguments[i]).append(" ");
                     }

                     BattleInfo battle = BattlesList.getBattleInfoById(id.toString().trim().replace("#battle", ""));
                     if (battle == null) {
                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Битва не найдена"});
                     } else {
                        if (battle.model != null) {
                           battle.model.sendTableMessageToPlayers("Битва была досрочна завершена, скоро вы будете кикнуты");
                        }

                        SystemTimerScheduler.scheduleTask(() -> {
                           this.sendSystemMessageToAll("Битва " + battle.name + " была принудительно завершена", false);
                           BattlesList.removeBattle(battle);
                           autoEntryServices.battleDisposed(battle.model);
                        }, 4000L);
                        msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Битва будет удалена через 4 секунды"});
                     }
                  d = true;
                  break;
               case "ban":
                  BanTimeType time = BanChatCommads.getTimeType(arguments[0]);
                  if (arguments.length < 3) {
                     return;
                  }

                  String reason = StringUtils.concatMassive(arguments, 2);
                  if (time == null) {
                     msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Команда бана не найдена!"});
                     return;
                  }

                  User _victim = this.database.getUserById(arguments[1]);
                  if (_victim == null) {
                     msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Игрок не найден!"});
                     return;
                  }

                  this.banServices.ban(BanType.CHAT, time, _victim, msg.user, reason);
                  this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкист ", _victim.getNickname(), " лишен права выхода в эфир ", time.getNameType(), " Причина: ", reason}), false);
                  d = true;
            }
            if (!d) {
               msg.localLobby.send(Type.LOBBY_CHAT, new String[]{"system", "Неизвестная команда!"});
            }
      } else if (!msg.message.isEmpty()) {
         if (msg.message.length() >= 399) {
            msg = null;
            return;
         }

         if (!this.stoped) {
            if (!msg.localLobby.getChatFloodController().detected(msg.message)) {
               msg.message = this.getNormalMessage(msg.message.trim());
               msg.localLobby.timer = System.currentTimeMillis();
               if (this.chatMessages.size() >= 50) {
                  this.chatMessages.remove(0);
               }

               this.chatMessages.add(msg);
               this.sendMessageToAll(msg);
            } else {
               if (msg.user.getWarnings() >= 4) {
                  BanTimeType time = BanTimeType.FIVE_MINUTES;
                  String reason = "Флуд.";
                  this.banServices.ban(BanType.CHAT, time, msg.user, msg.user, reason);
                  this.sendSystemMessageToAll(StringUtils.concatStrings(new String[]{"Танкист ", msg.user.getNickname(), " лишен права выхода в эфир ", time.getNameType(), " Причина: ", reason}), false);
                  return;
               }

               this.sendSystemMessageToAll("Танкист " + msg.user.getNickname() + "  предупрежден. Причина: Флуд.", false);
               msg.user.addWarning();
            }
         }
      }

   }

   public void cleanMessagesByText(String text) {
      Predicate<ChatMessage> filter = (p) -> {
         return p.message.equals(text);
      };
      this.chatMessages.removeIf(filter);
      this.lobbyServices.sendCommandToAllUsers(Type.LOBBY_CHAT, UserLocation.ALL, new String[]{"clean_by_text", text});
   }

   public void cleanMessagesByUser(String nickname) {
      Predicate<ChatMessage> ifDelete = (p) -> {
         return !p.system && p.user != null && p.user.getNickname().equals(nickname);
      };
      this.chatMessages.removeIf(ifDelete);
      this.lobbyServices.sendCommandToAllUsers(Type.LOBBY_CHAT, UserLocation.ALL, new String[]{"clean_by", nickname});
   }

   public void clear() {
      this.lobbyServices.sendCommandToAllUsers(Type.LOBBY_CHAT, UserLocation.ALL, new String[]{"clear_all"});
      this.chatMessages.clear();
      this.sendSystemMessageToAll("Чат очищен", false);
   }

   public void sendSystemMessageToAll(String[] ar, boolean yellow) {
      StringBuffer total = new StringBuffer();

      for(int i = 1; i < ar.length; ++i) {
         total.append(ar[i]).append(" ");
      }

      ChatMessage sys_msg = new ChatMessage((User)null, total.toString(), false, (User)null, yellow, (LobbyManager)null);
      sys_msg.system = true;
      this.chatMessages.add(sys_msg);
      if (this.chatMessages.size() >= 50) {
         this.chatMessages.remove(0);
      }

      this.lobbyServices.sendCommandToAllUsers(Type.LOBBY_CHAT, UserLocation.ALL, new String[]{"system", total.toString().trim(), yellow ? "yellow" : "green"});
   }

   public void sendSystemMessageToAll(String msg, boolean yellow) {
      ChatMessage sys_msg = new ChatMessage((User)null, msg, false, (User)null, yellow, (LobbyManager)null);
      sys_msg.system = true;
      this.chatMessages.add(sys_msg);
      if (this.chatMessages.size() >= 50) {
         this.chatMessages.remove(0);
      }

      this.lobbyServices.sendCommandToAllUsersBesides(Type.LOBBY_CHAT, UserLocation.BATTLE, new String[]{"system", msg.trim()});
   }

   public void sendMessageToAll(ChatMessage msg) {
      this.lobbyServices.sendCommandToAllUsersBesides(Type.LOBBY_CHAT, UserLocation.BATTLE, new String[]{JSONUtils.parseChatLobbyMessage(msg)});
   }

   public String getNormalMessage(String src) {
      StringBuilder str = new StringBuilder();
      char[] mass = src.toCharArray();

      for(int i = 0; i < mass.length; ++i) {
         if (mass[i] == ' ') {
            if (mass[i] != mass[i + 1]) {
               str.append(" ");
            }
         } else {
            str.append(mass[i]);
         }
      }

      return str.toString();
   }

   public void deleteMessagesByText(String text, boolean accuracy) {
   }

   public int getInt(String src) {
      try {
         return Integer.parseInt(src);
      } catch (Exception var3) {
         return 2147483647;
      }
   }

   public Collection<ChatMessage> getMessages() {
      return this.chatMessages;
   }
}
