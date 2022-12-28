package temporal.collection;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.UUID;

public class CollectionStarter {
    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    public static final WorkflowClient client = WorkflowClient.newInstance(service);
    public static final String taskQueue = "ingestionTaskQueue";
    public static final String customerId = "675ac101f436854137421f340c66eb0db9e00c2d";

    public static void main(String[] args){
        String workFlowId = createCronForCollection(customerId, UUID.randomUUID().toString());
        System.out.println("submitted workflow"+ workFlowId);
    }

    private static String createCronForCollection(String customerId, String accountId) {
        String workFlowId = UUID.randomUUID().toString();
        System.out.println("creating cron with ID :" + workFlowId);
        CollectionWorkflow workflow = client.newWorkflowStub(
                CollectionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workFlowId)
                        .setTaskQueue(taskQueue)
                        .setCronSchedule("@every 2m")
                        .build());
        WorkflowClient.start(workflow::collect,customerId, accountId);
        return workFlowId;
    }
}
