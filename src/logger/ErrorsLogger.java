package logger;

import java.io.PrintStream;

/** @deprecated */
@Deprecated
public class ErrorsLogger {
   private PrintStream printStream;

   public void onExcepton(Exception ex) {
   }

   public void onExcepton(Throwable ex) {
   }
}
