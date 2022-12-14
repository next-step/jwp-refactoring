package kitchenpos.acceptance;

import static kitchenpos.domain.OrderTestFixture.order;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderAcceptanceTestUtils {
    private static final String ORDER_PATH = "/api/orders";

    private OrderAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderTable orderTable, Menu... menus) {
        Order order = order(null, orderTable.getId(), toOrderLineItems(menus), OrderStatus.COOKING.name());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post(ORDER_PATH)
                .then().log().all()
                .extract();
    }

    private static List<OrderLineItem> toOrderLineItems(Menu[] menus) {
        return Arrays.stream(menus)
                .map(menu -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menu.id());
                    orderLineItem.setQuantity(1L);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, OrderStatus orderStatus) {
        Order changeOrderStatus = order(null, null, null, orderStatus.name());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeOrderStatus)
                .when().put(ORDER_PATH + "/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static Order 주문_등록되어_있음(OrderTable orderTable, Menu... menus) {
        ExtractableResponse<Response> response = 주문_생성_요청(orderTable, menus);
        주문_생성됨(response);
        return response.as(Order.class);
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_생성_주문상태_확인(ExtractableResponse<Response> response) {
        String actual = response.jsonPath()
                .getString("orderStatus");
        assertThat(actual).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_상태_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, Order... orders) {
        List<Long> actual = response.jsonPath()
                .getList("id", Long.class);
        List<Long> expect = Arrays.stream(orders)
                .map(Order::getId)
                .collect(Collectors.toList());
        assertThat(actual).containsExactlyElementsOf(expect);
    }
}
