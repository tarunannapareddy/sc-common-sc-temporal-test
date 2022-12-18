package temporal.ingestion;


import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import temporal.pojo.Data;
import temporal.pojo.DataDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngestionActivityImpl implements IngestionActivities {
    private Logger logger = Workflow.getLogger(this.getClass().getName());

    @Override
    public List<Data> fetchDataActivity(String customerId, String accountId) {
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
    public List<DataDTO> enrichmentActivity(String customerId, String accountId, List<Data> data) {
        logger.info("started enrichment for the customer {}",customerId);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<DataDTO> personDTO = getPersonDTO(data);
        logger.info("notified1 customer {}", customerId);
        return personDTO;
    }

    @Override
    public String ingestionActivity(String customerId, String accountId, List<DataDTO> dataDTOList) {
        logger.info("started ingestion for customer {}",customerId);
        try {
            // simulate some time....
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finished ingestion for the customer {}",customerId);
        return UUID.randomUUID().toString();
    }

    private List<Data> getPersonData(String customerId, String accountId){
        List<Data> personData = new ArrayList<>();
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test1").personAge(21).build());
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test2").personAge(22).build());
        personData.add(Data.builder().customerId(customerId).accountId(accountId).personName("test3").personAge(23).build());
        return personData;
    }

    private List<DataDTO> getPersonDTO(List<Data> personData){
        List<DataDTO> personDTO = new ArrayList<>();
        for(Data data : personData){
            personDTO.add(DataDTO.builder()
                    .customerId(data.getCustomerId())
                    .accountId(data.getAccountId())
                    .personName(data.getPersonName())
                    .personAge(data.getPersonAge())
                    .personGender(data.getPersonGender())
                    .personMetadata(UUID.randomUUID().toString())
                    .build());
        }
       return personDTO;
    }
}
