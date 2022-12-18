package temporal.collection;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CollectionWorkflow {
     @WorkflowMethod
     void collect(String customerId, String accountId);
}
