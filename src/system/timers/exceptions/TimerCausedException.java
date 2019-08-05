package system.timers.exceptions;

public class TimerCausedException extends Exception {
   private static final long serialVersionUID = 1L;

   public TimerCausedException(String msg) {
      super(msg);
   }
}
