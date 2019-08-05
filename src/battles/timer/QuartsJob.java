package battles.timer;

import battles.BattlefieldModel;
import system.quartz.QuartzJob;
import org.quartz.JobExecutionContext;

public class QuartsJob implements QuartzJob {
    private BattlefieldModel v;

    public QuartsJob(BattlefieldModel v1) {
        v = v1;
    }

    public void run(JobExecutionContext context) {
        this.v.battleRestart();
    }
}
