package system.quartz;

import org.quartz.JobDetail;

public interface QuartzService {
   JobDetail addJobInterval(String var1, String var2, QuartzJob var3, TimeType var4, long var5);

   JobDetail addJobInterval(String var1, String var2, QuartzJob var3, TimeType var4, long var5, int var7);

   JobDetail addJob(String var1, String var2, QuartzJob var3, TimeType var4, long var5);

   void deleteJob(String var1, String var2);
}
