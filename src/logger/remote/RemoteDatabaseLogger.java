package logger.remote;

import logger.remote.types.LogType;
import main.database.DatabaseManager;
import main.database.impl.DatabaseManagerImpl;
import services.annotations.ServicesInject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class RemoteDatabaseLogger {
   @ServicesInject(
      target = DatabaseManager.class
   )
   private static final DatabaseManager databaseManager = DatabaseManagerImpl.instance();

   public static void error(Exception ex) {
      log(buildLogObject(ex, LogType.ERROR));
   }

   public static void error(String message) {
      log(buildLogObject(message, LogType.ERROR));
   }

   public static void info(Exception ex) {
      log(buildLogObject(ex, LogType.INFO));
   }

   public static void warn(Exception ex) {
      log(buildLogObject(ex, LogType.WARNING));
   }

   public static void crit(Exception ex) {
      log(buildLogObject(ex, LogType.CRITICAL_ERROR));
   }

   public static String dumpLogs() {
      StringBuilder sb = new StringBuilder();
      List logs = databaseManager.collectLogs();
      ArrayList dump = new ArrayList();
      Iterator var4 = logs.iterator();

      while(true) {
         while(true) {
            while(var4.hasNext()) {
               LogObject _dd = (LogObject)var4.next();
               RemoteDatabaseLogger.DumpData dd = new RemoteDatabaseLogger.DumpData();
               dd.obj = _dd;
               if(dump.contains(dd)) {
                  Iterator var7 = dump.iterator();

                  while(var7.hasNext()) {
                     RemoteDatabaseLogger.DumpData _dd1 = (RemoteDatabaseLogger.DumpData)var7.next();
                     if(_dd1.equals(dd)) {
                        ++_dd1.count;
                        break;
                     }
                  }
               } else {
                  dump.add(dd);
               }
            }

            var4 = dump.iterator();

            while(var4.hasNext()) {
               RemoteDatabaseLogger.DumpData var8 = (RemoteDatabaseLogger.DumpData)var4.next();
               sb.append("[count :").append(var8.count).append("] ").append(var8.obj.toString()).append("\n");
            }

            return sb.toString();
         }
      }
   }

   public static List getDump() {
      List logs = databaseManager.collectLogs();
      ArrayList dump = new ArrayList();
      Iterator var3 = logs.iterator();

      while(true) {
         while(true) {
            while(var3.hasNext()) {
               LogObject log = (LogObject)var3.next();
               RemoteDatabaseLogger.DumpData dd = new RemoteDatabaseLogger.DumpData();
               dd.obj = log;
               if(dump.contains(dd)) {
                  Iterator var6 = dump.iterator();

                  while(var6.hasNext()) {
                     RemoteDatabaseLogger.DumpData _dd = (RemoteDatabaseLogger.DumpData)var6.next();
                     if(_dd.equals(dd)) {
                        ++_dd.count;
                        break;
                     }
                  }
               } else {
                  dump.add(dd);
               }
            }

            return dump;
         }
      }
   }

   private static LogObject buildLogObject(Exception ex, LogType type) {
      LogObject log = new LogObject();
      StringWriter sw = new StringWriter();
      ex.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      log.setDate(new Date());
      log.setType(type);
      log.setMessage(exceptionAsString);
      return log;
   }

   private static LogObject buildLogObject(String msg, LogType type) {
      LogObject log = new LogObject();
      log.setDate(new Date());
      log.setType(type);
      log.setMessage(msg);
      return log;
   }

   private static void log(LogObject obj) {
      if(obj != null) {
         databaseManager.register(obj);
      }

   }

   public static class DumpData {
      public LogObject obj;
      public int count = 1;

      public String getHeader() {
         StringTokenizer st = new StringTokenizer(this.obj.getMessage());
         return st.nextToken("\n");
      }

      public boolean equals(Object obj) {
         if(!(obj instanceof RemoteDatabaseLogger.DumpData)) {
            return false;
         } else {
            RemoteDatabaseLogger.DumpData _obj = (RemoteDatabaseLogger.DumpData)obj;
            return this.obj.getMessage().equals(_obj.obj.getMessage());
         }
      }
   }
}
