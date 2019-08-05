package services.ban.block;

public class BlockGameReason {
   public static final BlockGameReason DEFAULT = new BlockGameReason(0, "Аккаунт был заблокирован за нарушение правил игры\nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com");
   public static final BlockGameReason UNACCEPTABLE_NICK = new BlockGameReason(1, "Аккаунт был заблокирован за использование недопустимого игрового имени\nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com ");
   public static final BlockGameReason USING_CHEATS = new BlockGameReason(2, "Аккаунт был заблокирован за использование читов\nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com ");
   public static final BlockGameReason TRANSFER_OF_ACCOUNT = new BlockGameReason(3, "Аккаунт был заблокирован за попытку передачи аккаунта стороннему игроку\nЗаявки на восстановление аккаунта приниматься не будут. ");
   public static final BlockGameReason PUMPING = new BlockGameReason(4, "Прокачка (набор очков за счет бездействия другого игрока)\nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com ");
   public static final BlockGameReason USING_BAGS = new BlockGameReason(5, "Злонамерное использование програмной ошибки \nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com ");
   public static final BlockGameReason SABOTAGE = new BlockGameReason(6, "Саботаж (создание помех чужой команде через бездействующих «фальшивых» игроков)\nЕсли вы уверены, что мы ошиблись - пишите на help@legendtanks.com ");
   private final String reason;
   private final int reasonId;

   private BlockGameReason(int reasonId, String reason) {
      this.reason = reason;
      this.reasonId = reasonId;
   }

   public String getReason() {
      return this.reason;
   }

   public int getReasonId() {
      return this.reasonId;
   }

   public static BlockGameReason getReasonById(int i) {
         switch (i)
         {
            case 0:
               return DEFAULT;
            case 1:
               return UNACCEPTABLE_NICK;
            case 2:
               return USING_CHEATS;
            case 3:
               return TRANSFER_OF_ACCOUNT;
            case 4:
               return PUMPING;
            case 5:
               return USING_BAGS;
            case 6:
               return SABOTAGE;
         }
         return null;
   }
}