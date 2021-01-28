package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderLIneItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest orderTableRequest;
    private OrderRequest orderRequest;
    private List<OrderLIneItemRequest> orderLineItems = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItems.add(new OrderLIneItemRequest(2L, 2L));
        orderLineItems.add(new OrderLIneItemRequest(1L, 1L));
        orderTableRequest = new OrderTableRequest(9, true);
    }
    @DisplayName("테이블을 관리한다.")
    @Test
    void manageTable() {
        //테이블 등록
        ExtractableResponse<Response> createResponse = 주문테이블_등록_요청(orderTableRequest);
        주문테이블_등록됨(createResponse);

        ExtractableResponse<Response> findResponse = 주문테이블목록_조회_요청();
        주문테이블목록_조회됨(findResponse);

        주문_등록됨(createResponse.as(OrderTableResponse.class).getId(), orderLineItems);

        ExtractableResponse<Response> changeResponse = 주문테이블상태_변경_요청(createResponse, false);
        주문테이블상태_변경됨(changeResponse);

        ExtractableResponse<Response> changeResponse2 = 주문테이블_손님수_변경_요청(2L, 6);
        주문테이블_손님수_변경됨(changeResponse2);
    }

    private ExtractableResponse<Response> 주문_등록됨(Long tableId, List<OrderLIneItemRequest> orderLineItems) {
        orderRequest = new OrderRequest(tableId, orderLineItems);
        return RestAssured.given().log().all().
                body(orderRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/orders").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문테이블_등록_요청(OrderTableRequest orderTableRequest) {
        return RestAssured.given().log().all().
                body(orderTableRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/tables").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문테이블목록_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/tables").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문테이블상태_변경_요청(ExtractableResponse<Response> response, boolean tableStatus) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                body(tableStatus).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().put(uri + "/empty").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문테이블_손님수_변경_요청(Long tableId, int numberOfGuests) {
        return RestAssured.given().log().all().
                body(numberOfGuests).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().put("/api/tables/" + tableId + "/number-of-guests").
                then().log().all().
                extract();
    }

    private void 주문테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(9);
        assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(true);
    }

    private void 주문테이블목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTableResponse> orderTables = response.jsonPath().getList(".", OrderTableResponse.class);
        assertThat(orderTables.get(0).getId()).isEqualTo(1);
        assertThat(orderTables.get(1).getId()).isEqualTo(2);
    }

    private void 주문테이블상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(false);
    }

    private void 주문테이블_손님수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(6);
    }
}
