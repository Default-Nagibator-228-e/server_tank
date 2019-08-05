package logger;

import main.Main;
import java.awt.Color;

public class Logger {
   private static ErrorsLogger errors = new ErrorsLogger();

   public static void log(Type type, String msg) {
      Log tempLog = new Log(type, msg);

      try {
         if(msg == null || msg.trim().equals("null")) {
            throw new Exception("");
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      System.err.println(tempLog.toString());
   }

   public static void debug(String msg) {
      System.out.println("[DEBUG] " + msg);
   }

   public static void error(Exception ex) {
      errors.onExcepton(ex);
   }

   public static void error(Throwable tw) {
      errors.onExcepton(tw);
   }

   public static void log(String msg) {
      try {
         if(msg == null || msg.trim().equals("null")) {
            throw new Exception("");
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
      Log temp = new Log(Type.INFO, msg);
      if(Main.console != null) {
         Main.console.append(Color.WHITE, temp.toString());
      }

      System.err.println(temp.toString());
   }
}
