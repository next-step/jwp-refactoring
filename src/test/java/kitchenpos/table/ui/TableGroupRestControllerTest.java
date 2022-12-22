package kitchenpos.table.ui;

import static kitchenpos.table.ui.TableRestControllerTest.좌석_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.BaseTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TableGroupRestControllerTest extends BaseTest {
    private OrderTable 좌석_요청_1;
    private OrderTable 좌석_요청_2;
    private TableGroupRequest 좌석_그룹_요청;

    @BeforeEach
    void beforeEach() {
        좌석_요청_1 = new OrderTable(null, 3, true);
        좌석_요청_2 = new OrderTable(null, 1, true);

        Long orderTableId1 = 좌석_생성_요청(좌석_요청_1).getBody().getId();
        Long orderTableId2 = 좌석_생성_요청(좌석_요청_2).getBody().getId();

        좌석_그룹_요청 = new TableGroupRequest(Arrays.asList(orderTableId1, orderTableId2));
    }

    @Test
    void 생성() {
        ResponseEntity<TableGroupResponse> response = 좌석_그룹_생성_요청(좌석_그룹_요청);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 제거() {
        ResponseEntity<TableGroupResponse> created = 좌석_그룹_생성_요청(좌석_그룹_요청);

        ResponseEntity<Void> response = 좌석_그룹_제거_요청(created.getBody().getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<TableGroupResponse> 좌석_그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return testRestTemplate.postForEntity(basePath + "/api/table-groups", tableGroupRequest, TableGroupResponse.class);
    }

    private ResponseEntity<Void> 좌석_그룹_제거_요청(Long tableGroupId) {
        return testRestTemplate.exchange(
                basePath + "/api/table-groups/" + tableGroupId,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>() {});
    }
}
