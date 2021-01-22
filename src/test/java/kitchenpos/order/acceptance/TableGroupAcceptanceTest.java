package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest orderTableRequest1;
    private OrderTableRequest orderTableRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTableRequest1 = new OrderTableRequest(50, true);
        orderTableRequest2 = new OrderTableRequest(30, true);
        OrderTableResponse orderTableResponse1 = TableAcceptanceTest.주문_테이블_등록_되어있음(orderTableRequest1).as(OrderTableResponse.class);
        OrderTableResponse orderTableResponse2 = TableAcceptanceTest.주문_테이블_등록_되어있음(orderTableRequest2).as(OrderTableResponse.class);
        orderTableRequest1.setId(orderTableResponse1.getId());
        orderTableRequest2.setId(orderTableResponse2.getId());
    }

    @DisplayName("단체 지정을 관리한다.")
    @Test
    void manageTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(tableGroupRequest);

        // then
        단체_지정_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);

        // then
        단체_지정_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 단체_지정_생성_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private void 단체_지정_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 단체_지정_삭제_요청(ExtractableResponse<Response> createResponse) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private void 단체_지정_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
