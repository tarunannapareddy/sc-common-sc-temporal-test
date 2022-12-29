package temporal.ingestion;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.temporal.worker.WorkflowImplementationOptions;
import org.slf4j.LoggerFactory;
import temporal.collection.CollectionActivitiesImpl;
import temporal.collection.CollectionWorkflowImpl;

import java.time.Duration;

public class IngestionWorker {

    public static final WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
    public static final WorkflowClient client = WorkflowClient.newInstance(service);
    public static final String taskQueue = "ingestionTaskQueue-1";

    public static void main(String[] args) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        WorkerFactory workerFactory = WorkerFactory.newInstance(client);
        Worker worker = workerFactory.newWorker(taskQueue);
        registerCollectionWorkflow(worker);
        registerIngestionWorkflow(worker);

        workerFactory.start();
    }

    static void registerIngestionWorkflow(Worker worker){
        WorkflowImplementationOptions options =
                WorkflowImplementationOptions.newBuilder()
                        .setDefaultActivityOptions(ActivityOptions.newBuilder()
                                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
                                .setScheduleToCloseTimeout(Duration.ofHours(1))
                                .build()).build();
        worker.registerWorkflowImplementationTypes(options, IngestionWorkflowImpl.class);
        worker.registerActivitiesImplementations(new IngestionActivityImpl());
    }

    static void registerCollectionWorkflow(Worker worker){
        WorkflowImplementationOptions options =
                WorkflowImplementationOptions.newBuilder()
                        .setDefaultActivityOptions(ActivityOptions.newBuilder()
                                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
                                .setScheduleToCloseTimeout(Duration.ofHours(1))
                                .build()).build();
        worker.registerWorkflowImplementationTypes(options, CollectionWorkflowImpl.class);
        worker.registerActivitiesImplementations(new CollectionActivitiesImpl());
    }
}
