package test.test1;


import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class IngestionActivityImpl implements IngestionActivities {
    private Logger logger = Workflow.getLogger(this.getClass().getName());

    @Override
    public String enrichmentActivity(String accountId, String id) {
        logger.info("started1 customer {}",id);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("notified1 customer {}", id);
        return "";
    }

    @Override
    public String ingestionActivity(String accountId, String id) {
        logger.info("started2 customer {}",id);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("notified2 customer {}",id);
        return "";
    }

    @Override
    public String validationActivity(String accountId, String id) {
        logger.info("started3 customer {}",id);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("notified3 customer {}",id);
        return "";
    }
}
