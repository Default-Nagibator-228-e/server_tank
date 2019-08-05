package services.ban;

import utils.StringUtils;

public class BanTimeType {
   public static final BanTimeType FIVE_MINUTES = new BanTimeType("НА 5 МИНУТ.", 12, 5);
   public static final BanTimeType ONE_HOUR = new BanTimeType("НА 1 ЧАС.", 10, 1);
   public static final BanTimeType ONE_DAY = new BanTimeType("НА 1 ДЕНЬ.", 5, 1);
   public static final BanTimeType ONE_WEEK = new BanTimeType("НА 1 НЕДЕЛЮ.", 4, 1);
   public static final BanTimeType ONE_MONTH = new BanTimeType("НА 1 МЕСЯЦ.", 2, 1);
   public static final BanTimeType HALF_YEAR = new BanTimeType("НА ПОЛ ГОДА.", 2, 6);
   public static final BanTimeType FOREVER = new BanTimeType("НАВСЕГДА.", 1, 2);
   private final String nameType;
   private int field;
   private int amount;

   private BanTimeType(String nameType, int field, int amount) {
      this.nameType = nameType;
      this.field = field;
      this.amount = amount;
   }

   public String getNameType() {
      return this.nameType;
   }

   public int getField() {
      return this.field;
   }

   public int getAmount() {
      return this.amount;
   }

   public String toString() {
      return StringUtils.concatStrings(new String[]{"BanTimeType [", this.nameType, "]"});
   }

   public boolean equals(Object obj) {
      BanTimeType _obj;
      try {
         _obj = (BanTimeType)obj;
      } catch (Exception var4) {
         return false;
      }

      return this.getNameType().equals(_obj.getNameType());
   }
}
