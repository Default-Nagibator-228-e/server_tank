package battles.spectator.chat;

import lobby.LobbyManager;
import users.User;
import commands.Type;
import services.ban.BanChatCommads;
import services.ban.block.BlockGameReason;
import services.ban.BanTimeType;
import services.ban.BanType;
import utils.StringUtils;
import battles.bonuses.BonusType;
import battles.spectator.SpectatorController;
import services.BanServices;
import services.LobbysServices;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import main.database.DatabaseManager;
import battles.chat.BattlefieldChatModel;
import battles.BattlefieldModel;
import battles.spectator.SpectatorModel;

public class SpectatorChatModel
{
   private static final String CHAT_SPECTATOR_COMAND = "spectator_message";
   private SpectatorModel spModel;
   private BattlefieldModel bfModel;
   private BattlefieldChatModel chatModel;
   @ServicesInject(target = DatabaseManagerImpl.class)
   private DatabaseManager database;
   @ServicesInject(target = LobbysServices.class)
   private LobbysServices lobbyServices;
   @ServicesInject(target = BanServices.class)
   private BanServices banServices;

   public SpectatorChatModel(final SpectatorModel spModel) {
      this.database = DatabaseManagerImpl.instance();
      this.lobbyServices = LobbysServices.getInstance();
      this.banServices = BanServices.getInstance();
      this.spModel = spModel;
      this.bfModel = spModel.getBattleModel();
      this.chatModel = this.bfModel.chatModel;
   }

   public void onMessage(final String message, final SpectatorController spectator) {
      if (message.startsWith("/")) {
         final String[] arguments = message.replace('/', ' ').trim().split(" ");
         if (!spectator.getUser().getUserGroup().isAvaliableChatCommand(arguments[0])) {
            return;
         }
         final String s;
         switch (s = arguments[0]) {
            case "spawngold": {
               for (int i = 0; i < Integer.parseInt(arguments[1]); ++i) {
                  this.spModel.getBattleModel().bonusesSpawnService.spawnBonus(BonusType.GOLD);
               }
               break;
            }
            case "w": {
               if (arguments.length < 3) {
                  return;
               }
               final User giver = this.database.getUserById(arguments[1]);
               if (giver == null) {
                  break;
               }
               final String reason = StringUtils.concatMassive(arguments, 2);
               this.chatModel.sendSystemMessage(StringUtils.concatStrings(new String[] { "\u0422\u0430\u043d\u043a\u0438\u0441\u0442 ", giver.getNickname(), " \u043f\u0440\u0435\u0434\u0443\u043f\u0440\u0435\u0436\u0434\u0435\u043d. \u041f\u0440\u0438\u0447\u0438\u043d\u0430: ", reason }));
               break;
            }
            case "kick": {
               final User _userForKick = this.database.getUserById(arguments[1]);
               if (_userForKick == null) {
                  break;
               }
               final LobbyManager _lobby = this.lobbyServices.getLobbyByUser(_userForKick);
               if (_lobby != null) {
                  _lobby.kick();
                  this.chatModel.sendSystemMessage(String.valueOf(_userForKick.getNickname()) + " \u043a\u0438\u043a\u043d\u0443\u0442");
                  break;
               }
               break;
            }
            case "unban": {
               if (arguments.length < 2) {
                  break;
               }
               final User cu = this.database.getUserById(arguments[1]);
               if (cu == null) {
                  break;
               }
               this.banServices.unbanChat(cu);
               this.chatModel.sendSystemMessage("\u0422\u0430\u043d\u043a\u0438\u0441\u0442\u0443 " + cu.getNickname() + " \u0431\u044b\u043b \u0440\u0430\u0437\u0440\u0435\u0448\u0451\u043d \u0432\u044b\u0445\u043e\u0434 \u0432 \u044d\u0444\u0438\u0440");
               break;
            }
            case "blockgame": {
               if (arguments.length < 3) {
                  return;
               }
               final User victim_ = this.database.getUserById(arguments[1]);
               int reasonId = 0;
               try {
                  reasonId = Integer.parseInt(arguments[2]);
               }
               catch (Exception ex) {
                  reasonId = 0;
               }
               if (victim_ == null) {
                  break;
               }
               this.banServices.ban(BanType.GAME, BanTimeType.FOREVER, victim_, spectator.getUser(), BlockGameReason.getReasonById(reasonId).getReason());
               final LobbyManager lobby = this.lobbyServices.getLobbyByNick(victim_.getNickname());
               if (lobby != null) {
                  lobby.kick();
               }
               this.chatModel.sendSystemMessage(StringUtils.concatStrings(new String[] { "\u0422\u0430\u043d\u043a\u0438\u0441\u0442 ", victim_.getNickname(), " \u0431\u044b\u043b \u0437\u0430\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d \u0438 \u043a\u0438\u043a\u043d\u0443\u0442" }));
               break;
            }
            case "unblockgame": {
               if (arguments.length < 2) {
                  return;
               }
               final User av = this.database.getUserById(arguments[1]);
               if (av == null) {
                  break;
               }
               this.banServices.unblock(av);
               this.chatModel.sendSystemMessage(String.valueOf(av.getNickname()) + " \u0440\u0430\u0437\u0431\u043b\u043e\u043a\u0438\u0440\u043e\u0432\u0430\u043d");
               break;
            }
            case "spawncry": {
               for (int i = 0; i < Integer.parseInt(arguments[1]); ++i) {
                  this.spModel.getBattleModel().bonusesSpawnService.spawnBonus(BonusType.CRYSTALL);
               }
               break;
            }
            default:
               break;
         }
         if (message.startsWith("/ban")) {
            final BanTimeType time = BanChatCommads.getTimeType(arguments[0]);
            if (arguments.length < 3) {
               return;
            }
            final String reason2 = StringUtils.concatMassive(arguments, 2);
            if (time == null) {
               return;
            }
            final User _victim = this.database.getUserById(arguments[1]);
            if (_victim == null) {
               return;
            }
            this.banServices.ban(BanType.CHAT, time, _victim, spectator.getUser(), reason2);
            this.chatModel.sendSystemMessage(StringUtils.concatStrings(new String[] { "\u0422\u0430\u043d\u043a\u0438\u0441\u0442 ", _victim.getNickname(), " \u043b\u0438\u0448\u0435\u043d \u043f\u0440\u0430\u0432\u0430 \u0432\u044b\u0445\u043e\u0434\u0430 \u0432 \u044d\u0444\u0438\u0440 ", time.getNameType(), " \u041f\u0440\u0438\u0447\u0438\u043d\u0430: ", reason2 }));
         }
      }
      else {
         this.spModel.getBattleModel().sendToAllPlayers(Type.BATTLE, new String[] { "spectator_message", message });
      }
   }
}