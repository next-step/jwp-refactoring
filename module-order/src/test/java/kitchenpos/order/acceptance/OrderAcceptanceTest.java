package kitchenpos.order.acceptance;

import static kitchenpos.order.acceptance.OrderAcceptanceSupport.주문_테이블_등록됨;
import static kitchenpos.order.acceptance.OrderAcceptanceSupport.치킨세트_메뉴_등록함;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.OrderTestFixture;
import kitchenpos.order.acceptance.utils.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문에 대한 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private MenuResponse 메뉴;

    private OrderTableResponse 주문_테이블_response;
    private OrderLineItemRequest 주문_항목_request;
    private OrderRequest 주문_request;
    private OrderRequest 주문2_request;
    private OrderResponse 주문_response;

    @BeforeEach
    public void setUp() {
        super.setUp();

        메뉴 = 치킨세트_메뉴_등록함();

        주문_테이블_response = 주문_테이블_등록됨(OrderTestFixture.주문_테이블_REQUEST_FIXTURE);
        주문_항목_request = new OrderLineItemRequest(메뉴.getId(), 1);
        주문_request = new OrderRequest(주문_테이블_response.getId(), null, null,
            Collections.singletonList(주문_항목_request));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_등록요청(주문_request);

        // then
        주문_등록요청_검증됨(response);
    }

    @DisplayName("모든 주문목록을 조회한다")
    @Test
    void find_test() {
        // given
        주문_등록요청(주문_request);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회요청();

        // then
        주문_목록_조회_검증됨(response, 1);
    }

    @DisplayName("주문상태를 변경한다")
    @Test
    void change_orderStatus_test() {
        // given
        주문_response = 주문_등록요청(주문_request).as(OrderResponse.class);
        주문2_request = new OrderRequest(null, OrderStatus.MEAL, null, null);

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청(주문_response.getId(), 주문2_request);

        // then
        주문_상태_변경됨(response, OrderStatus.MEAL.name());
    }

    private ExtractableResponse<Response> 주문_등록요청(OrderRequest orderRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    private void 주문_등록요청_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        OrderResponse result = response.as(OrderResponse.class);
        assertNotNull(result.getOrderLineItems());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    private ExtractableResponse<Response> 주문_목록_조회요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }

    private void 주문_목록_조회_검증됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderResponse> result = response.jsonPath().getList(".", OrderResponse.class);
        assertThat(result).hasSize(size);
    }

    private ExtractableResponse<Response> 주문_상태_변경요청(Long orderId, OrderRequest orderRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().put("/api/orders/{orderId}/order-status", orderId)
            .then().log().all()
            .extract();
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus);
    }
}
