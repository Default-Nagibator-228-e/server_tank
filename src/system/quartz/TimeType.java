package system.quartz;

public enum TimeType {
   MS(1),
   SEC(1000),
   MIN('\uea60'),
   HOUR(3600000);

   private final int mn;

   private TimeType(int mn) {
      this.mn = mn;
   }

   public long time(long time) {
      return (long)this.mn * time;
   }
}
