package lobby.chat.flood;

public abstract class FloodController {
   public static final int MIN_TIME_FLOOD_MS = 500;
   public static final int MAX_WARNING = 5;

   public abstract boolean detected(String var1);
}
