package groups;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UserGroup {
   private String groupName;
   private final List avaliableChatCommands;

   public UserGroup(List avaliableChatCommands) {
      this.avaliableChatCommands = Collections.unmodifiableList(avaliableChatCommands);
   }

   public boolean isAvaliableChatCommand(String command) {
      return this.avaliableChatCommands.contains(command);
   }

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("User group: ").append(this.getGroupName()).append(". ").append("Avaliable chat commands:\n");
      Iterator var3 = this.avaliableChatCommands.iterator();

      while(var3.hasNext()) {
         String command = (String)var3.next();
         sb.append("        ---- /").append(command).append('\n');
      }

      return sb.toString();
   }
}
