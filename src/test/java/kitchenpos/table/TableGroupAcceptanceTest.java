package kitchenpos.table;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createTableGroup() {
        // given
        OrderTableResponse 주문_테이블_첫번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderTableRequest.of(4, true));
        OrderTableResponse 주문_테이블_두번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderTableRequest.of(5, true));
        TableGroupRequest 테이블_그룹_첫번째 = TableGroupRequest.of(Arrays.asList(주문_테이블_첫번째.getId(), 주문_테이블_두번째.getId()));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_첫번째);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void changeTableGroup() {
        // given
        OrderTableResponse 주문_테이블_첫번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderTableRequest.of(4, true));
        OrderTableResponse 주문_테이블_두번째 = TableAcceptanceTest.주문_테이블_생성됨(OrderTableRequest.of(5, true));
        TableGroupRequest 테이블_그룹_첫번째 = TableGroupRequest.of(Arrays.asList(주문_테이블_첫번째.getId(), 주문_테이블_두번째.getId()));

        TableGroupResponse 테이블_그룹 = 테이블_그룹_생성됨(테이블_그룹_첫번째);

        // when
        ExtractableResponse<Response> response = 테이블_그룹_해제_요청(테이블_그룹);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private TableGroupResponse 테이블_그룹_생성됨(TableGroupRequest tableGroupRequest) {
        return 테이블_그룹_생성_요청(tableGroupRequest).as(TableGroupResponse.class);
    }

    private ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return ofRequest(Method.POST, "/api/table-groups", tableGroupRequest);
    }

    private ExtractableResponse<Response> 테이블_그룹_해제_요청(TableGroupResponse tableGroupRequest) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put("tableGroupId", tableGroupRequest.getId());
        return ofRequest(Method.DELETE, "/api/table-groups/{tableGroupId}", pathParams);
    }
}
