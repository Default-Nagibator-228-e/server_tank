package services.ban;

public class BanChatCommads {
   public static final String BAN_FIVE_MINUTES = "banminutes";
   public static final String BAN_ONE_HOUR = "banhour";
   public static final String BAN_ONE_DAY = "banday";
   public static final String BAN_ONE_WEEK = "banweek";
   public static final String BAN_ONE_MONTH = "banmonth";
   public static final String BAN_HALF_YEAR = "banhalfyear";
   public static final String BAN_FOREVER = "banforever";

   public static BanTimeType getTimeType(String cmd) {
      switch(cmd) {
         case BAN_ONE_MONTH:
               return BanTimeType.ONE_MONTH;
         case BAN_FIVE_MINUTES:
               return BanTimeType.FIVE_MINUTES;
         case BAN_ONE_DAY:
               return BanTimeType.ONE_DAY;
         case BAN_ONE_HOUR:
               return BanTimeType.ONE_HOUR;
         case BAN_ONE_WEEK:
               return BanTimeType.ONE_WEEK;
         case BAN_FOREVER:
               return BanTimeType.FOREVER;
         case BAN_HALF_YEAR:
               return BanTimeType.HALF_YEAR;
      }
      return null;
   }
}
