//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package bugs;

import utils.StringUtils;
import bugs.screenshots.BufferScreenshotTransfer;
import users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class BugReport {
   private static String URL_BUGS_FILE = "bugs/bugs.data";
   private static File bugsFile;

   public BugReport() {
   }

   public static void bugReport(User sender, BufferScreenshotTransfer screenshot) {
   }

   public static void bugReport(User sender, BugInfo bug) throws IOException {
      if (bugsFile == null) {
         bugsFile = new File(URL_BUGS_FILE);

         try {
            bugsFile.createNewFile();
         } catch (IOException var13) {
            var13.printStackTrace();
         }
      }

      Throwable var2 = null;
      Object var3 = null;

      try {
         FileWriter writer = new FileWriter(bugsFile, true);

         try {
            writer.append(getFormatedData(sender, bug));
         } finally {
            if (writer != null) {
               writer.close();
            }

         }
      } catch (Throwable var15) {
         if (var2 == null) {
            var2 = var15;
         } else if (var2 != var15) {
            var2.addSuppressed(var15);
         }

         //throw var2;
      }

   }

   private static String getFormatedData(User sender, BugInfo bug) {
      return StringUtils.concatStrings(new String[]{"----", (new Date()).toString(), "----\n", "  User: ", sender.getNickname(), "\n", bug.toString(), "---------------------------------------\n"});
   }
}
