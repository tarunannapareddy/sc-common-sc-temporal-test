package test.test1;


import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;

public class IngestionWorkflowImpl implements IngestionWorkflow {
    private Logger logger = Workflow.getLogger(this.getClass().getName());
    private int iterationCount =0;
    private boolean continueWork = false;

    private IngestionActivities activityStub =
            Workflow.newActivityStub(IngestionActivities.class, ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofHours(1))
                    .build());

    @Override
    public void ingest(String accountId) {
        String id = Workflow.getInfo().getWorkflowId();
        while (true) {
            logger.info("continue to work {} for account {}", continueWork, accountId);
            Workflow.await(() -> continueWork);
            continueWork = false;
            logger.info("started workflow {} and iteration {}", id, iterationCount);
            activityStub.enrichmentActivity(accountId, id);
            activityStub.ingestionActivity(accountId, id);
            activityStub.validationActivity(accountId, id);
            logger.info("finished workflow {} and iteration {}", id, iterationCount);
            iterationCount++;
            if (iterationCount == 3) {
                Workflow.continueAsNew(accountId);
            }
        }
    }

    @Override
    public int getCount() {
        return iterationCount;
    }

    @Override
    public void setLogsPublished(){
        continueWork = true;
    }
}
