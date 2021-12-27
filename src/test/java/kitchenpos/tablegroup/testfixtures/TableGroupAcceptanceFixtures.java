package kitchenpos.tablegroup.testfixtures;

import java.util.List;
import kitchenpos.common.CustomTestRestTemplate;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class TableGroupAcceptanceFixtures {

    private static final String BASE_URL = "/api/table-groups";

    public static ResponseEntity<TableGroupResponse> 테이블_그룹_생성(
        TableGroupRequest tableGroupRequest) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.postForEntity(BASE_URL, tableGroupRequest, TableGroupResponse.class);
    }

    public static ResponseEntity<Void> 테이블_그룹_해제(Long tableGroupId) {
        TestRestTemplate restTemplate = CustomTestRestTemplate.initInstance();
        return restTemplate.exchange(BASE_URL + "/" + tableGroupId, HttpMethod.DELETE, null,
            Void.class);
    }

    public static TableGroupRequest 테이블_그룹_정의(List<OrderTableRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }
}
