package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {
    private List<OrderLineItemRequest> orderLineItemRequests;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록된_menu_id, 2));

        OrderRequest orderRequest = new OrderRequest(1L, 비어있지_않은_orderTable_id, orderLineItemRequests);
        주문_생성_요청(orderRequest).as(OrderResponse.class);
    }

    @Test
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest(2L, 비어있지_않은_orderTable_id, orderLineItemRequests);

        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        주문_생성됨(response);
    }

    @DisplayName("선택한 주문 항목의 메뉴가 등록되어 있지 않으면 생성할 수 없다.")
    @Test
    void createOrderException1() {
        orderLineItemRequests = Collections.singletonList(new OrderLineItemRequest(등록되어_있지_않은_menu_id, 2));
        OrderRequest orderRequest = new OrderRequest(2L, 비어있지_않은_orderTable_id, orderLineItemRequests);

        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        주문_생성_실패(response);
    }

    @DisplayName("해당 주문 테이블이 등록되어 있지 않으면 생성할 수 없다.")
    @Test
    void createOrderException2() {
        OrderRequest orderRequest = new OrderRequest(2L, 등록되어_있지_않은_orderTable_id, orderLineItemRequests);

        ExtractableResponse<Response> response = 주문_생성_요청(orderRequest);

        주문_생성_실패(response);
    }

    @Test
    void getOrders() {
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        주문_목록_조회됨(response);
        주문_목록_크기_일치(response, 1);
    }

    @Test
    void changeOrderStatus() {
        OrderRequest changeOrder = new OrderRequest(1L, 비어있지_않은_orderTable_id, OrderStatus.MEAL, orderLineItemRequests);

        ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrder);

        주문_상태_변경됨(response, changeOrder.getOrderStatus());
    }

    @DisplayName("주문이 등록되어 있지 않으면 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusException() {
        OrderRequest changeOrder = new OrderRequest(2L, 비어있지_않은_orderTable_id, OrderStatus.MEAL, orderLineItemRequests);

        ExtractableResponse<Response> response = 주문_상태_변경_요청(changeOrder);

        주문_상태_변경_실패(response);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(OrderRequest orderRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().put("/api/orders/{orderId}/kitchenpos.order-status", orderRequest.getId())
                .then().log().all()
                .extract();
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 주문_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_크기_일치(ExtractableResponse<Response> response, int size) {
        List<OrderResponse> orderResponses = response.jsonPath().getList(".", OrderResponse.class);

        assertThat(orderResponses.size()).isEqualTo(size);
    }

    private static void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderResponse result = response.as(OrderResponse.class);
        assertThat(result.getOrderStatus()).isEqualTo(orderStatus);
    }

    private static void 주문_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
