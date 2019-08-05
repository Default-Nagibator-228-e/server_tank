package logger;

public class Log {
   public Type type;
   public String msg;

   public Log(Type type, String msg) {
      this.type = type;
      this.msg = msg;
   }

   public String toString() {
      return "[" + this.type + "]: " + this.msg;
   }
}
