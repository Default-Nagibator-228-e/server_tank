package commands;

import utils.StringUtils;

public class Command {
   public Type type;
   public String[] args;

   public Command(Type type, String[] args) {
      this.type = type;
      this.args = args;
   }

   public String toString() {
      String argsString = StringUtils.concatStrings(this.args);
      return this.type.toString() + " " + argsString;
   }
}
