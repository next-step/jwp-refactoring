package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableGroupAcceptanceSupport.주문_테이블_등록요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.table.acceptance.utils.AcceptanceTest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 테이블에 대한 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블2_request;
    private OrderTableResponse 주문_테이블_response;
    private OrderTableResponse 주문_테이블2_response;
    private TableGroupRequest 테이블_그룹_request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_request = new OrderTableRequest(null, null, 3, true);
        주문_테이블2_request = new OrderTableRequest(null, null, 1, true);

        주문_테이블_response = 주문_테이블_등록요청(주문_테이블_request).as(OrderTableResponse.class);
        주문_테이블2_response = 주문_테이블_등록요청(주문_테이블2_request).as(OrderTableResponse.class);

        테이블_그룹_request = new TableGroupRequest(null, Arrays.asList(주문_테이블_response.getId(), 주문_테이블2_response.getId()));
    }

    @DisplayName("테이블 단체지정을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 테이블_단체지정_등록요청(테이블_그룹_request);

        // then
        테이블_단체지정_등록됨(response);
    }

    @DisplayName("테이블 단체지정을 해제한다")
    @Test
    void ungroup_test() {
        // given
        TableGroupResponse tableGroup = 테이블_단체지정_등록요청(테이블_그룹_request).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블_단체지정_해제요청(tableGroup.getId());

        // then
        테이블_단체지정_해제됨(response);
    }

    private ExtractableResponse<Response> 테이블_단체지정_등록요청(TableGroupRequest tableGroup) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    private void 테이블_단체지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        TableGroupResponse result = response.as(TableGroupResponse.class);
        List<OrderTableResponse> orderTables = result.getOrderTables();

        assertThat(result.getCreatedDate()).isNotNull();

        for (OrderTableResponse orderTable : orderTables) {
            assertFalse(orderTable.isEmpty());
            assertThat(orderTable.getTableGroupId()).isNotNull();
        }
    }

    private ExtractableResponse<Response> 테이블_단체지정_해제요청(Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
            .then().log().all()
            .extract();
    }

    private void 테이블_단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
