package services.ban;

import java.util.concurrent.TimeUnit;

public class DateFormater {
   public static String formatTimeToUnban(long millis) {
      String hours = format(String.valueOf(TimeUnit.MILLISECONDS.toHours(millis)));
      String minutes = format(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))));
      StringBuilder sb = new StringBuilder();
      sb.append(hours).append(" часа(ов) ").append(minutes).append(" минут(ы)");
      return sb.toString();
   }

   private static String format(String src) {
      if(src.startsWith("-")) {
         src = src.substring(1);
      }

      return src;
   }
}
