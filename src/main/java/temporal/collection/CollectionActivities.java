package temporal.collection;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import temporal.pojo.Data;
import temporal.pojo.DataDTO;

import java.util.List;

@ActivityInterface
public interface CollectionActivities {
    @ActivityMethod
    List<Data> fetchDataAfterMarkerActivity(String customerId, String accountId, String Marker);
    @ActivityMethod
    String copyDataToInternalSystemActivity(String customerId, String accountId, List<Data> data);
}
