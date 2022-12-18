package temporal.ingestion;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import temporal.pojo.Data;
import temporal.pojo.DataDTO;

import java.util.List;

@ActivityInterface
public interface IngestionActivities {

    @ActivityMethod
    List<Data> fetchDataActivity(String customerId, String accountId);
    @ActivityMethod
    List<DataDTO> enrichmentActivity(String customerId, String accountId, List<Data> data);
    @ActivityMethod
    String ingestionActivity(String customerId, String accountId, List<DataDTO> dataDTOList);
}
