package temporal.collection;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import temporal.pojo.Data;
import temporal.pojo.DataDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CollectionActivitiesImpl implements CollectionActivities{
    private Logger logger = Workflow.getLogger(this.getClass().getName());
    @Override
    public List<Data> fetchDataAfterMarkerActivity(String customerId, String accountId, String Marker) {
        logger.info("started data fetch for customer {}",customerId);
        try {
            // simulate some work....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Data> personData = getPersonData(customerId, accountId);
        logger.info("fetched data for customer {}",customerId);
        return personData;
    }

    @Override
    public String copyDataToInternalSystemActivity(String customerId, String accountId, List<Data> data) {
        logger.info("started data copy for customer {}",customerId);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finished data copy for the customer {}",customerId);
        return UUID.randomUUID().toString();
    }

    private List<Data> getPersonData(String customerId, String accountId){
        List<Data> personData = new ArrayList<>();
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test1").personAge(21).build());
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test2").personAge(22).build());
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test3").personAge(23).build());
        return personData;
    }
}
