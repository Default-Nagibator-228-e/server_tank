package console;

import utils.StringUtils;
import services.LobbysServices;
import services.annotations.ServicesInject;
import java.awt.Color;

public class ConsoleComandHandler implements IComandHandler {
   private ConsoleWindow context;
   @ServicesInject(
      target = LobbysServices.class
   )
   private LobbysServices lobbyMessages = LobbysServices.getInstance();

   public ConsoleComandHandler(ConsoleWindow context) {
      this.context = context;
   }

   public void onEnterComand(String cmd) {
      String[] args = cmd.split(" ");
      switch(args[0]) {
      case "trace":
            this.context.append(Color.YELLOW, StringUtils.concatMassive(args, 1));
      default:
      }
   }
}
