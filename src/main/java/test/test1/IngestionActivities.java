package test.test1;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface IngestionActivities {

    String enrichmentActivity(String accountId, String id);

    String ingestionActivity(String accountId, String id);

    String validationActivity(String accountId, String id);
}
