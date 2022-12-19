package kitchenpos;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static kitchenpos.AcceptanceTest.restTemplate;
import static org.assertj.core.api.Assertions.assertThat;

public final class TableGroupAcceptanceTestUtil {

    private TableGroupAcceptanceTestUtil() {
    }

    public static TableGroupResponse 단체_지정_등록됨(OrderTableResponse... orderTables) {
        return 단체_지정_생성_요청(orderTables).getBody();
    }

    static ResponseEntity<TableGroupResponse> 단체_지정_생성_요청(OrderTableResponse... orderTables) {
        return 단체_지정_생성_요청(Arrays.stream(orderTables)
                                 .map(OrderTableResponse::getId)
                                 .collect(Collectors.toList()));
    }

    static ResponseEntity<TableGroupResponse> 단체_지정_생성_요청(Long... orderTableIds) {
        return 단체_지정_생성_요청(Arrays.stream(orderTableIds)
                                 .collect(Collectors.toList()));
    }

    static ResponseEntity<TableGroupResponse> 단체_지정_생성_요청(List<Long> ids) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTables", ids.stream()
                                      .map(OrderTableIdRequest::new)
                                      .collect(Collectors.toList()));
        return restTemplate().postForEntity("/api/table-groups", request, TableGroupResponse.class);
    }

    static ResponseEntity<Void> 단체_지정_해지_요청(TableGroupResponse tableGroup) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("tableGroupId", tableGroup.getId());
        return restTemplate().exchange("/api/table-groups/{tableGroupId}", HttpMethod.DELETE,
                                       null, Void.class, urlVariables);
    }

    static void 단체_지정_생성됨(ResponseEntity<TableGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    static void 단체_지정_생성_실패됨(ResponseEntity<TableGroupResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static void 단체_지정_해지됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    static void 단체_지정_해지_실패됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
