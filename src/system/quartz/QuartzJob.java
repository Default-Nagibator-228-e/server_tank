package system.quartz;

import org.quartz.JobExecutionContext;

public interface QuartzJob {
   void run(JobExecutionContext var1);
}
