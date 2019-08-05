package system;

import console.ConsoleWindow;
import logger.Logger;
import logger.Type;
import main.params.OnlineStats;
import services.annotations.ServicesInject;
import system.restart.ServerRestartService;

import java.io.IOException;
import java.util.Scanner;

public class SystemConsoleHandler extends Thread {
   private static final SystemConsoleHandler instance = new SystemConsoleHandler();
   private ConsoleWindow fs;
   @ServicesInject(
      target = ServerRestartService.class
   )
   private static ServerRestartService serverRestartService = ServerRestartService.inject();

   public static SystemConsoleHandler getInstance() {
      return instance;
   }

   private SystemConsoleHandler() {
      this.setName("SystemConsoleHandler thread");
   }

   private void onCommand(String input) throws IOException {
      switch(input) {
      case "/online":
            System.out.println(this.getOnlineInfoString());
         break;
      case "/rf":
            Logger.log(Type.WARNING, "Attention! Reload swf!");
         break;
      case "/help":
            System.out.println(this.getHelpString());
         break;
      case "/restart":
         serverRestartService.restart();
         break;
      case "/console":
         fs = new ConsoleWindow();
      }
   }

   private String getOnlineInfoString() {
      return "\n Total online: " + OnlineStats.getOnline() + "\n Max online: " + OnlineStats.getMaxOnline() + "\n";
   }

   private String getHelpString() {
      return "rf - reload item\'s factories.\nonline - print current online.";
   }

   public void run() {
      Throwable var1 = null;
      Object var2 = null;

      try {
         Scanner scn = new Scanner(System.in);

         try {
            String input = "";

            while(true) {
               input = scn.nextLine();
               this.onCommand(input);
            }
         } finally {
            if(scn != null) {
               scn.close();
            }

         }
      } catch (Throwable var10) {
         if(var1 == null) {
            var1 = var10;
         } else if(var1 != var10) {
            var1.addSuppressed(var10);
         }

         //throw var1;
      }
   }
}
