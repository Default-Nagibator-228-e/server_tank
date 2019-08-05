package system.quartz.impl;

import system.quartz.QuartzJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJobRunner implements Job {
   public static String jobRunKey = "runnable";

   public void execute(JobExecutionContext context) throws JobExecutionException {
      QuartzJob run = (QuartzJob)context.getJobDetail().getJobDataMap().get(jobRunKey);
      run.run(context);
   }
}
