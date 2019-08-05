package groups;

import logger.Logger;
import users.TypeUser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class UserGroupsLoader {
   private static final String FILE_FORMAT = ".group";
   private static HashMap usersGroups = new HashMap();

   public static UserGroup getUserGroup(TypeUser typeUser) {
      return (UserGroup)usersGroups.get(typeUser);
   }

   public static void load(String path) {
      File folder = new File(path);
      File[] var5;
      int var4 = (var5 = folder.listFiles()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         File file = var5[var3];
         if(file.getName().endsWith(".group")) {
            parseFile(file);
         }
      }

   }

   private static void parseFile(File file) {
      try {
         List e = Files.readAllLines(file.toPath());
         TypeUser typeUser = getTypeUser(file.getName().split(".group")[0]);
         UserGroup userGroup = new UserGroup(e);
         userGroup.setGroupName(typeUser.toString());
         usersGroups.put(typeUser, userGroup);
         Logger.log("User group " + typeUser.toString() + " has been inited.");
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   private static TypeUser getTypeUser(String name) {
      TypeUser[] var4;
      int var3 = (var4 = TypeUser.values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         TypeUser type = var4[var2];
         if(type.toString().equals(name)) {
            return type;
         }
      }

      return null;
   }
}
