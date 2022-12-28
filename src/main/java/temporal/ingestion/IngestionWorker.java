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
    public static final String taskQueue = "ingestionTaskQueue";

    public static void main(String[] args) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        WorkerFactory workerFactory = WorkerFactory.newInstance(client);

        registerCollectionWorkflow(workerFactory);
        registerIngestionWorkflow(workerFactory);

        workerFactory.start();
    }

    static void registerIngestionWorkflow(WorkerFactory workerFactory){
        Worker worker = workerFactory.newWorker(taskQueue);
        WorkflowImplementationOptions options =
                WorkflowImplementationOptions.newBuilder()
                        .setDefaultActivityOptions(ActivityOptions.newBuilder()
                                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(10).build())
                                .setScheduleToCloseTimeout(Duration.ofHours(1))
                                .build()).build();
        worker.registerWorkflowImplementationTypes(options, IngestionWorkflowImpl.class);
        worker.registerActivitiesImplementations(new IngestionActivityImpl());
    }

    static void registerCollectionWorkflow(WorkerFactory workerFactory){
        Worker worker = workerFactory.newWorker(taskQueue);
        WorkflowImplementationOptions options =
                WorkflowImplementationOptions.newBuilder()
                        .setDefaultActivityOptions(ActivityOptions.newBuilder()
                                .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(12).build())
                                .setScheduleToCloseTimeout(Duration.ofHours(1))
                                .build()).build();
        worker.registerWorkflowImplementationTypes(options, CollectionWorkflowImpl.class);
        worker.registerActivitiesImplementations(new CollectionActivitiesImpl());
    }
}
