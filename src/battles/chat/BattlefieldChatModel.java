package battles.chat;

import utils.StringUtils;
import battles.BattlefieldModel;
import battles.BattlefieldPlayerController;
import battles.bonuses.BonusType;
import commands.Type;
import json.JSONUtils;
import lobby.LobbyManager;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import main.params.OnlineStats;
import services.BanServices;
import services.LobbysServices;
import services.TanksServices;
import services.annotations.ServicesInject;
import services.ban.BanChatCommads;
import services.ban.BanTimeType;
import services.ban.BanType;
import services.ban.DateFormater;
import services.ban.block.BlockGameReason;
import users.TypeUser;
import users.User;
import users.karma.Karma;

import java.util.Date;

public class BattlefieldChatModel {
   private BattlefieldModel bfModel;
   private final int MAX_WARNING = 5;
   @ServicesInject(
      target = TanksServices.class
   )
   private TanksServices tanksServices = TanksServices.getInstance();
   @ServicesInject(
      target = DatabaseManager.class
   )
   private DatabaseManager database = DatabaseManagerImpl.instance();
   @ServicesInject(
      target = LobbysServices.class
   )
   private LobbysServices lobbyServices = LobbysServices.getInstance();
   @ServicesInject(
      target = BanServices.class
   )
   private BanServices banServices = BanServices.getInstance();

   public BattlefieldChatModel(BattlefieldModel bfModel) {
      this.bfModel = bfModel;
   }

   public void onMessage(BattlefieldPlayerController player, String message, boolean team) {
      if(!(message = message.trim()).isEmpty()) {
         Karma karma = this.database.getKarmaByUser(player.getUser());
         if(karma.isChatBanned()) {
            long time = System.currentTimeMillis();
            Date time1 = karma.getChatBannedBefore();
            long reason1 = time - time1.getTime();
            if(reason1 <= 0L) {
               player.parentLobby.send(Type.LOBBY_CHAT, new String[]{"system", StringUtils.concatStrings(new String[]{"Вы отключены от чата. Вы вернётесь в ЭФИР через ", DateFormater.formatTimeToUnban(reason1), ". Причина: " + karma.getReasonChatBan()})});
               return;
            }

            this.banServices.unbanChat(player.getUser());
         }

         if(!this.bfModel.battleInfo.team) {
            team = false;
         }

         String reason;
         if(message.startsWith("/")) {
            if (player.getUser().getType() == TypeUser.DEFAULT) {
               return;
            }

            String[] var21 = message.replace('/', ' ').trim().split(" ");
            if (!player.getUser().getUserGroup().isAvaliableChatCommand(var21[0])) {
               return;
            }

            User _victim;
            int _userForKick;
            int var25;
            Boolean d = false;
            reason = var21[0];
            switch (reason) {
               case "addcry":
                  this.tanksServices.addCrystall(player.parentLobby, this.getInt(var21[1]));
                  d = true;
                  break;
               case "addscore":
                  var25 = this.getInt(var21[1]);
                  if (player.parentLobby.getLocalUser().getScore() + var25 < 0) {
                     this.sendSystemMessage("Ваше количество очков опыта не должно быть отрицательное!", player);
                  } else {
                     this.tanksServices.addScore(player.parentLobby, var25);
                  }
                  d = true;
                  break;
               case "online":
                  this.sendSystemMessage("Current online: " + OnlineStats.getOnline() + "\nMax online: " + OnlineStats.getMaxOnline(), player);
                  break;
               case "system":
                  StringBuffer var23 = new StringBuffer();

                  for (var25 = 1; var25 < var21.length; ++var25) {
                     var23.append(var21[var25]).append(" ");
                  }

                  this.sendSystemMessage(var23.toString());
                  d = true;
                  break;
               case "spawngold":
                  _userForKick = 0;
                  this.bfModel.bonusesSpawnService.spawnBonus(BonusType.GOLD);
                  ++_userForKick;
                  d = true;
                  break;
               case "w":
                     if(var21.length < 3) {
                        return;
                     }

                     User giver = this.database.getUserById(var21[1]);
                     if(giver == null) {
                        this.sendSystemMessage("Игрок не найден!", player);
                     } else {
                        String reason2 = StringUtils.concatMassive(var21, 2);
                        this.sendSystemMessage(StringUtils.concatStrings(new String[]{"Танкист ", giver.getNickname(), " предупрежден. Причина: ", reason2}));
                     }
                  d = true;
                  break;
               case "kick":
                     User var28 = this.database.getUserById(var21[1]);
                     if(var28 == null) {
                        this.sendSystemMessage("Игрок не найден", player);
                     } else {
                        LobbyManager _lobby = this.lobbyServices.getLobbyByUser(var28);
                        if(_lobby != null) {
                           _lobby.kick();
                           this.sendSystemMessage(var28.getNickname() + " кикнут");
                        }
                     }
                  d = true;
                  break;
               case "getip":
                     if(var21.length >= 2) {
                        User shower = this.database.getUserById(var21[1]);
                        if(shower == null) {
                           return;
                        }

                        String ip = shower.getAntiCheatData().ip;
                        if(ip == null) {
                           ip = shower.getLastIP();
                        }

                        this.sendSystemMessage("IP user " + shower.getNickname() + " : " + ip, player);
                     }
                  d = true;
                     break;
               case "unban":
                     if(var21.length >= 2) {
                        User cu = this.database.getUserById(var21[1]);
                        if(cu == null) {
                           this.sendSystemMessage("Игрок не найден!", player);
                        } else {
                           this.banServices.unbanChat(cu);
                           this.sendSystemMessage("Танкисту " + cu.getNickname() + " был разрешён выход в эфир");
                        }
                  }
                  d = true;
                  break;
               case "blockgame":
                     if(var21.length < 3) {
                        return;
                     }

                     _victim = this.database.getUserById(var21[1]);
                     boolean reasonId = false;

                     int var27;
                     try {
                        var27 = Integer.parseInt(var21[2]);
                     } catch (Exception var20) {
                        var27 = 0;
                     }

                     if(_victim == null) {
                        this.sendSystemMessage("Игрок не найден!", player);
                     } else {
                        this.banServices.ban(BanType.GAME, BanTimeType.FOREVER, _victim, player.getUser(), BlockGameReason.getReasonById(var27).getReason());
                        LobbyManager lobby = this.lobbyServices.getLobbyByNick(_victim.getNickname());
                        if(lobby != null) {
                           lobby.kick();
                        }

                        this.sendSystemMessage(StringUtils.concatStrings(new String[]{"Танкист ", _victim.getNickname(), " был заблокирован и кикнут"}));
                  }
                  d = true;
                  break;
               case "unblockgame":
                     if(var21.length < 2) {
                        return;
                     }

                     User av = this.database.getUserById(var21[1]);
                     if(av == null) {
                        this.sendSystemMessage("Игрок не найден!", player);
                     } else {
                        this.banServices.unblock(av);
                        this.sendSystemMessage(av.getNickname() + " разблокирован");
                     }
                  d = true;
                  break;
               case "spawncry":
                     _userForKick = 0;
                     this.bfModel.bonusesSpawnService.spawnBonus(BonusType.CRYSTALL);
                     ++_userForKick;
                     d = true;
                     break;
               case "ban" :
                  BanTimeType var24 = BanChatCommads.getTimeType(var21[0]);
                  if(var21.length < 3) {
                     return;
                  }

                  String var26 = StringUtils.concatMassive(var21, 2);
                  if(var24 == null) {
                     this.sendSystemMessage("Команда бана не найдена!", player);
                     return;
                  }

                  _victim = this.database.getUserById(var21[1]);
                  if(_victim == null) {
                     this.sendSystemMessage("Игрок не найден!", player);
                     return;
                  }

                  this.banServices.ban(BanType.CHAT, var24, _victim, player.getUser(), var26);
                  this.sendSystemMessage(StringUtils.concatStrings(new String[]{"Танкист ", _victim.getNickname(), " лишен права выхода в эфир ", var24.getNameType(), " Причина: ", var26}));
               }

            if (!d) {
               this.sendSystemMessage("Неизвестная команда!", player);
            }
         } else if(!message.isEmpty()){
            if(message.length() >= 399) {
               message = null;
               return;
            }

            if(!player.parentLobby.getChatFloodController().detected(message)) {
               player.parentLobby.timer = System.currentTimeMillis();
               this.sendMessage(new BattleChatMessage(player.getUser().getNickname(), player.getUser().getRang(), message, player.playerTeamType, team, false));
            } else {
               if(player.getUser().getWarnings() >= 5) {
                  BanTimeType var22 = BanTimeType.FIVE_MINUTES;
                  reason = "Флуд.";
                  this.banServices.ban(BanType.CHAT, var22, player.getUser(), player.getUser(), reason);
                  this.sendSystemMessage(StringUtils.concatStrings(new String[]{"Танкист ", player.getUser().getNickname(), " лишен права выхода в эфир ", var22.getNameType(), " Причина: ", reason}));
                  return;
               }

               this.sendSystemMessage("Танкист " + player.getUser().getNickname() + "  предупрежден. Причина: Флуд.");
               player.getUser().addWarning();
            }
         }

      }
   }

   public void sendSystemMessage(String message) {
      if(message == null) {
         message = " ";
      }

      this.sendMessage(new BattleChatMessage(null, 0, message, "NONE", false, true));
   }

   public void sendSystemMessage(String message, BattlefieldPlayerController player) {
      if(message == null) {
         message = " ";
      }

      this.sendMessage(new BattleChatMessage(null, 0, message, "NONE", false, true), player);
   }

   private void sendMessage(BattleChatMessage msg) {
      this.bfModel.sendToAllPlayers(Type.BATTLE, new String[]{"chat", JSONUtils.parseBattleChatMessage(msg)});
   }

   private void sendMessage(BattleChatMessage msg, BattlefieldPlayerController controller) {
      controller.send(Type.BATTLE, new String[]{"chat", JSONUtils.parseBattleChatMessage(msg)});
   }

   public int getInt(String src) {
      try {
         return Integer.parseInt(src);
      } catch (Exception var3) {
         return Integer.MAX_VALUE;
      }
   }
}
