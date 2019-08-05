package commands;

import org.apache.commons.lang3.ArrayUtils;

public class Commands {
   public static final String SPLITTER_ARGS = ";";

   public static Command decrypt(String crypt) {
      Type type = Type.UNKNOWN;
      String[] temp = crypt.split(SPLITTER_ARGS);
         switch(temp[0]) {
         case "battle":
               type = Type.BATTLE;
            break;
         case "registration":
               type = Type.REGISTRATON;
            break;
         case "garage":
               type = Type.GARAGE;
            break;
         case "system":
               type = Type.SYSTEM;
            break;
         case "auth":
               type = Type.AUTH;
            break;
         case "chat":
               type = Type.CHAT;
            break;
         case "ping":
               type = Type.PING;
            break;
         case "lobby":
               type = Type.LOBBY;
            break;
         case "lobby_chat":
               type = Type.LOBBY_CHAT;
         }
      return new Command(type, ArrayUtils.removeElement(temp, temp[0]));
   }
}
