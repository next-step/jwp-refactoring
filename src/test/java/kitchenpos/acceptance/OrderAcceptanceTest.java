package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.치킨세트_메뉴_등록함;
import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_등록됨_copy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.acceptance.support.TestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.request.OrderRequest;
import kitchenpos.order.domain.response.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.request.OrderLineItemRequest;
import kitchenpos.table.domain.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문에 대한 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;
    private Menu 메뉴;
    private OrderLineItem 주문_항목;
    private Order 주문;
    private Order 주문2;

    private OrderTableResponse 주문_테이블_response;
    private OrderLineItemRequest 주문_항목_request;
    private OrderLineItemRequest 주문_항목2_request;
    private OrderRequest 주문_request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = TableAcceptanceTest.주문_테이블_등록됨(TestFixture.주문_테이블_FIXTURE);
        주문_테이블2 = TableAcceptanceTest.주문_테이블_등록됨(TestFixture.주문_테이블_FIXTURE);

        메뉴 = 치킨세트_메뉴_등록함();
        주문_항목 = OrderLineItem.of(null, null, 메뉴.getId(), 1);
        주문 = Order.of(null, 주문_테이블.getId(), null, null, Arrays.asList(주문_항목));
        주문2 = Order.of(null, 주문_테이블2.getId(), null, null, Arrays.asList(주문_항목));

        주문_테이블_response = 주문_테이블_등록됨_copy(TestFixture.주문_테이블_REQUEST_FIXTURE);
        주문_항목_request = new OrderLineItemRequest(메뉴.getId(), 1);
        주문_request = new OrderRequest(주문_테이블_response.getId(), null, null,
            Collections.singletonList(주문_항목_request));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_등록요청(주문);

        // then
        주문_등록요청_검증됨(response);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create_test_copy() {
        // when
        ExtractableResponse<Response> response = 주문_등록요청_copy(주문_request);

        // then
        주문_등록요청_검증됨_copy(response);
    }

    @DisplayName("모든 주문목록을 조회한다")
    @Test
    void find_test() {
        // given
        주문_등록요청(주문);
        주문_등록요청(주문2);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회요청();

        // then
        주문_목록_조회_검증됨(response, 2);
    }

    @DisplayName("모든 주문목록을 조회한다")
    @Test
    void find_test_copy() {
        // given
        주문_등록요청_copy(주문_request);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회요청_copy();

        // then
        주문_목록_조회_검증됨_copy(response, 1);
    }

    @DisplayName("주문상태를 변경한다")
    @Test
    void change_orderStatus_test() {
        // given
        주문 = 주문_등록요청(주문).as(Order.class);
        주문2.setOrderStatus(OrderStatus.MEAL.name());
        ;

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청(주문.getId(), 주문2);

        // then
        주문_상태_변경됨(response, OrderStatus.MEAL.name());
    }

    @DisplayName("주문상태를 변경한다")
    @Test
    void change_orderStatus_test_copy() {
        // given
        주문 = 주문_등록요청(주문).as(Order.class);
        주문2.setOrderStatus(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청_copy(주문.getId(), 주문2);

        // then
        주문_상태_변경됨_copy(response, OrderStatus.MEAL.name());
    }

    private ExtractableResponse<Response> 주문_등록요청(Order order) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 주문_등록요청_copy(OrderRequest orderRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderRequest)
            .when().post("/api/orders/copy")
            .then().log().all()
            .extract();
    }

    private void 주문_등록요청_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        Order result = response.as(Order.class);
        assertNotNull(result.getOrderLineItems());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private void 주문_등록요청_검증됨_copy(ExtractableResponse<Response> response) {
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

    private ExtractableResponse<Response> 주문_목록_조회요청_copy() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/orders/copy")
            .then().log().all()
            .extract();
    }

    private void 주문_목록_조회_검증됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Order> result = response.jsonPath().getList(".", Order.class);
        assertThat(result).hasSize(size);
    }

    private void 주문_목록_조회_검증됨_copy(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderResponse> result = response.jsonPath().getList(".", OrderResponse.class);
        assertThat(result).hasSize(size);
    }

    private ExtractableResponse<Response> 주문_상태_변경요청(Long orderId, Order order) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().put("/api/orders/{orderId}/order-status", orderId)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 주문_상태_변경요청_copy(Long orderId, Order order) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().put("/api/orders/{orderId}/order-status/copy", orderId)
            .then().log().all()
            .extract();
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus);
    }

    private void 주문_상태_변경됨_copy(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus);
    }
}
