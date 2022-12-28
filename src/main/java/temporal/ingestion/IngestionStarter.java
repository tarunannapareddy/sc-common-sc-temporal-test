package temporal.ingestion;



import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import java.util.UUID;




public class IngestionStarter {

    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    public static final WorkflowClient client = WorkflowClient.newInstance(service);
    public static final String taskQueue = "ingestionTaskQueue";
    public static final String customerId = "675ac101f436854137421f340c66eb0db9e00c2f";

    public static void main(String[] args){
        String workFlowId = onboardNewCustomerWithClass(customerId, UUID.randomUUID().toString());
        System.out.println("submitted workflow"+ workFlowId);
    }

    private static String onboardNewCustomerWithClass(String customerId, String accountId) {
        String workFlowId = customerId;
        System.out.println("creating workflow with ID :" + workFlowId);
        IngestionWorkflow workflow = client.newWorkflowStub(
                IngestionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workFlowId)
                        .setTaskQueue(taskQueue)
                        .build());
        WorkflowClient.start(workflow::ingest,customerId, accountId);
        return workFlowId;
    }

    public static String createNewSchedule() {
        String workFlowId = UUID.randomUUID().toString();
        System.out.println("creating workflow with ID :" + workFlowId);
        IngestionWorkflow workflow = client.newWorkflowStub(
                IngestionWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workFlowId)
                        //.setWorkflowExecutionTimeout(Duration.ofSeconds(20))
                        //.setWorkflowRunTimeout(Duration.ofSeconds(20))
                        .setTaskQueue(taskQueue)
                        .setCronSchedule("@every 60s")
                        .build()
        );

        // start async, not blocking
        WorkflowClient.start(workflow::ingest,"customerId", "Account1");
        return workFlowId;
    }


    private static void signalPublishEventWithClass(String workflowId) {
        WorkflowServiceStubs service2 = WorkflowServiceStubs.newInstance();
        WorkflowClient client2 = WorkflowClient.newInstance(service2);
        IngestionWorkflow stub= client2.newWorkflowStub(IngestionWorkflow.class, workflowId);
        while (true) {
            try {
                Thread.sleep(1000);
                stub.setNewDataPublished(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static String onboardNewCustomer() {
            String workFlowId = UUID.randomUUID().toString();
            System.out.println("creating workflow with ID :" + workFlowId);
            WorkflowStub untypedWorkflowStub = client.newUntypedWorkflowStub("IngestionWorkflow",
                    WorkflowOptions.newBuilder()
                            .setWorkflowId(workFlowId)
                            .setTaskQueue(taskQueue)
                            .build());

            untypedWorkflowStub.start( "Account1");

            System.out.println("submitted workflow");
            return workFlowId;
    }

    private static void signalPublishEvent(String workflowId) {
        WorkflowServiceStubs service2 = WorkflowServiceStubs.newInstance();
        WorkflowClient client2 = WorkflowClient.newInstance(service2);
        WorkflowStub stub = client2.newUntypedWorkflowStub(workflowId);
        for(int i=0;i<20;i++) {
            try {
                Thread.sleep(30000);
                stub.signal("setData");
                System.out.println("send signal");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void pollforStatus(String workflowId){
        WorkflowServiceStubs service2 = WorkflowServiceStubs.newInstance();
        WorkflowClient client2 = WorkflowClient.newInstance(service2);
        WorkflowStub existingUntyped = client2.newUntypedWorkflowStub(workflowId);
        for(int i=0;i<3;i++) {
            int count = existingUntyped.query("getCount", Integer.class);
            System.out.println("processed count"+ count);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
