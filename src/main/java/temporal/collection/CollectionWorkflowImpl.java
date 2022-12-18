package temporal.collection;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.Workflow;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import temporal.ingestion.IngestionWorkflow;
import temporal.pojo.Data;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class CollectionWorkflowImpl implements CollectionWorkflow{
    private Logger logger = Workflow.getLogger(this.getClass().getName());

    private CollectionActivities activityStub =
            Workflow.newActivityStub(CollectionActivities.class, ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofHours(1))
                    .build());

    @Override
    public void collect(String customerId, String accountId) {
        String id = Workflow.getInfo().getWorkflowId();
        logger.info("started data collection for customer {} and workflow {}", customerId, id);
        List<Data> dataList = activityStub.fetchDataAfterMarkerActivity(customerId,accountId, UUID.randomUUID().toString());
        String collectionId = activityStub.copyDataToInternalSystemActivity(customerId, accountId, dataList);
        logger.info("finished data collection for customer {} and collectionId {}", customerId, collectionId);
        if(CollectionUtils.isNotEmpty(dataList)){
            signalIngestion(customerId);
            logger.info("signaled ingestion worker with id {}", customerId);
        }
    }

    private void signalIngestion(String customerId){
        IngestionWorkflow ingestionWorkflowStub = Workflow.newExternalWorkflowStub(IngestionWorkflow.class, customerId);
        ingestionWorkflowStub.setNewDataPublished(true);
    }


}
