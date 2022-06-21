package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.치킨세트_메뉴_등록함;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.acceptance.support.TestFixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = TableAcceptanceTest.주문_테이블_등록됨(TestFixture.주문_테이블_FIXTURE);
        주문_테이블2 = TableAcceptanceTest.주문_테이블_등록됨(TestFixture.주문_테이블_FIXTURE);

        메뉴 = 치킨세트_메뉴_등록함();
        주문_항목 = OrderLineItem.of(null, null, 메뉴.getId(), 1);
        주문 = Order.of(null, 주문_테이블.getId(), null, null, Arrays.asList(주문_항목));
        주문2 = Order.of(null, 주문_테이블2.getId(), null, null, Arrays.asList(주문_항목));
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_등록요청(주문);

        // then
        주문_등록요청_검증됨(response);
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

    @DisplayName("주문상태를 변경한다")
    @Test
    void change_orderStatus_test() {
        // given
        주문 = 주문_등록요청(주문).as(Order.class);
        주문2.setOrderStatus(OrderStatus.MEAL.name());;

        // when
        ExtractableResponse<Response> response = 주문_상태_변경요청(주문.getId(), 주문2);

        // then
        주문_상태_변경됨(response, OrderStatus.MEAL.name());
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

    private void 주문_등록요청_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        Order result = response.as(Order.class);
        assertNotNull(result.getOrderLineItems());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
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

        List<Order> result = response.jsonPath().getList(".", Order.class);
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

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, String orderStatus) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(orderStatus);
    }
}
