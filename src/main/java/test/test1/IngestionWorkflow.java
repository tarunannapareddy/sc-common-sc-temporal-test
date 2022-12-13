package test.test1;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface IngestionWorkflow {
    @WorkflowMethod
    void ingest(String accountIds);

    @SignalMethod
    void setLogsPublished();

    @QueryMethod
    int getCount();
}
