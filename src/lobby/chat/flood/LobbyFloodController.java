package lobby.chat.flood;

public class LobbyFloodController extends FloodController {
   private String lastMessage;
   private int messagesRepeated;
   private long lastMessageTime;

   public boolean detected(String msg) {
      if(System.currentTimeMillis() - this.lastMessageTime < 500L) {
         return true;
      } else {
         if(msg.equals(this.lastMessage)) {
            ++this.messagesRepeated;
            if(this.messagesRepeated >= 5) {
               return true;
            }
         } else {
            this.messagesRepeated = 0;
         }

         this.lastMessageTime = System.currentTimeMillis();
         this.lastMessage = msg;
         return false;
      }
   }
}
