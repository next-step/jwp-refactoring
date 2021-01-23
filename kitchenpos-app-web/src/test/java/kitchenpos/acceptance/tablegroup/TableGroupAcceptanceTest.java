package kitchenpos.acceptance.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceTest;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    public static OrderTableRequest orderTableRequest1;
    public static OrderTableRequest orderTableRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTableResponse orderTableResponse1 = OrderTableAcceptanceTest.주문_테이블_등록_요청(2, true).as(OrderTableResponse.class);
        OrderTableResponse orderTableResponse2 = OrderTableAcceptanceTest.주문_테이블_등록_요청(4, true).as(OrderTableResponse.class);

        orderTableRequest1 = new OrderTableRequest(orderTableResponse1.getId(), orderTableResponse1.getNumberOfGuests(), orderTableResponse1.isEmpty());
        orderTableRequest2 = new OrderTableRequest(orderTableResponse2.getId(), orderTableResponse2.getNumberOfGuests(), orderTableResponse2.isEmpty());
    }

    @Test
    @DisplayName("시나리오1: 여러개의 테이블을 단체 지정 할 수 있다.")
    public void scenarioTest() throws Exception {
        // when 테이블 단체 지정 요청
        ExtractableResponse<Response> 테이블_단체_지정 = 테이블_단체_지정_요청(Arrays.asList(orderTableRequest1, orderTableRequest2));
        // then 테이블 단체 지정됨
        테이블_단체_지정됨(테이블_단체_지정);

        // when 테이블 단체 해지 요청
        ExtractableResponse<Response> 테이블_단체_해지 = 테이블_단체_해지_요청(테이블_단체_지정);
        // then 테이블 단체 해지됨
        테이블_단체_해지됨(테이블_단체_해지);
    }

    public static ExtractableResponse<Response> 테이블_단체_지정_요청(List<OrderTableRequest> orderTables) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 테이블_단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 테이블_단체_해지_요청(ExtractableResponse<Response> response) {
        String location = response.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    public static void 테이블_단체_해지됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
