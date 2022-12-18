package temporal.ingestion;


import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import temporal.pojo.Data;
import temporal.pojo.DataDTO;

import java.time.Duration;
import java.util.List;

public class IngestionWorkflowImpl implements IngestionWorkflow {
        private Logger logger = Workflow.getLogger(this.getClass().getName());
        private boolean continueWork = false;

        private IngestionActivities activityStub =
                Workflow.newActivityStub(IngestionActivities.class, ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofHours(1))
                        .build());

        @Override
     public void ingest(String customerId, String accountId) {
        String id = Workflow.getInfo().getWorkflowId();
        while (true) {
            logger.info("continue to work {} for account {}", continueWork, accountId);
            Workflow.await(() -> continueWork);
            continueWork = false;
            logger.info("started workflow {} for account {}", id, accountId);
            List<Data> dataList = activityStub.fetchDataActivity(customerId, accountId);
            List<DataDTO> enrichedData = activityStub.enrichmentActivity(customerId, accountId, dataList);
            String ingestionId = activityStub.ingestionActivity(customerId, accountId, enrichedData);
            logger.info("Ingestion finished for account {} with id {}", accountId, ingestionId);
            Workflow.continueAsNew(customerId,accountId);
        }
    }

    @Override
    public void setNewDataPublished(boolean isNewDataPublished){
        continueWork = isNewDataPublished;
    }
}
