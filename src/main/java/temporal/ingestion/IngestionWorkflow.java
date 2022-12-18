package temporal.ingestion;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IngestionWorkflow {
    @WorkflowMethod
    void ingest(String customerId, String accountId);

    @SignalMethod
    void setNewDataPublished(boolean isNewDataPublished);

}
