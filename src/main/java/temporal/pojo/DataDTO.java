package temporal.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {
    private  String customerId;
    private  String accountId;
    private  String personName;
    private  int personAge;
    private  String personGender;
    private  String personMetadata;
}
