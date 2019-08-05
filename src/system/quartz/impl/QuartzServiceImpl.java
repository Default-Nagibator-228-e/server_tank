package system.quartz.impl;

import system.quartz.QuartzJob;
import system.quartz.QuartzService;
import system.quartz.TimeType;
import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzServiceImpl implements QuartzService {
   private static QuartzServiceImpl instance = new QuartzServiceImpl();
   private Scheduler scheduler;

   private QuartzServiceImpl() {
      StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();

      try {
         this.scheduler = schedulerFactory.getScheduler();
         this.scheduler.start();
      } catch (SchedulerException var3) {
      }

   }

   private JobDetail createJob(String name, String group, QuartzJob object) {
      JobDetail job = new JobDetail(name, group, QuartzJobRunner.class);
      job.getJobDataMap().put(QuartzJobRunner.jobRunKey, object);
      return job;
   }

   public JobDetail addJobInterval(String name, String group, QuartzJob object, TimeType type, long interval, int repeatCount) {
      JobDetail job = this.createJob(name, group, object);

      try {
         SimpleTrigger trigger = new SimpleTrigger(name, group, repeatCount, type.time(interval));
         this.scheduler.scheduleJob(job, trigger);
      } catch (SchedulerException var10) {
         ;
      }

      return job;
   }

   public JobDetail addJobInterval(String name, String group, QuartzJob object, TimeType type, long interval) {
      return this.addJobInterval(name, group, object, type, interval, -1);
   }

   public JobDetail addJob(String name, String group, QuartzJob object, TimeType type, long time) {
      JobDetail job = this.createJob(name, group, object);

      try {
         SimpleTrigger trigger = new SimpleTrigger(name, group, new Date(System.currentTimeMillis() + type.time(time)));
         this.scheduler.scheduleJob(job, trigger);
      } catch (SchedulerException var9) {
      }

      return job;
   }

   public Scheduler getScheduler() {
      return this.scheduler;
   }

   public void deleteJob(String name, String group) {
      try {
         this.scheduler.deleteJob(name, group);
      } catch (SchedulerException var4) {
      }

   }

   public static QuartzService inject() {
      return instance;
   }
}
