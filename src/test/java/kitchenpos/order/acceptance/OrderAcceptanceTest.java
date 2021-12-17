package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴등록되어있음;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_되어있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.주문테이블_등록되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/orders";

    @Test
    @DisplayName("테이블별 주문을 관리한다.")
    void manageOrder() {
        // 상품 등록 되어 있음
        Product 후라이드치킨 = 상품_등록_되어있음("후라이드치킨", BigDecimal.valueOf(10000));
        Product 양념치킨 = 상품_등록_되어있음("양념치킨", BigDecimal.valueOf(11000));
        // 메뉴그룹 등록 되어 있음
        MenuGroup 치킨 = 메뉴그룹_등록_되어있음("치킨");
        // 메뉴 등록되어 있음
        Menu 세트1 = 메뉴등록되어있음("세트1", BigDecimal.valueOf(20000), 치킨, Arrays.asList(후라이드치킨, 양념치킨));
        // 주문 테이블 등록되어 있음
        OrderTable orderTable = 주문테이블_등록되어있음(3);

        // 주문 생성 요청
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(세트1.getId());
        orderLineItem.setQuantity(2L);
        ExtractableResponse<Response> saveResponse = 주문_생성_요청(orderTable.getId(), Arrays.asList(orderLineItem));

        // 주문 생성됨
        주문_생성됨(saveResponse);

        Order order = saveResponse.as(Order.class);

        // 주문 목록 조회
        ExtractableResponse<Response> listResponse = 주문_목록_조회_요청();

        // 주문 목록 조회됨
        주문_목록_조회됨(listResponse, Arrays.asList(order));

        // 주문 상태 변경 (조리 -> 식사)
        ExtractableResponse<Response> mealResponse = 주문_상태변경_요청(order.getId(), OrderStatus.MEAL.name());

        // 주문 상태 변경됨
        주문_상태_변경됨(mealResponse, OrderStatus.MEAL.name());

        // 주문 상태 변경 (식사 -> 계산완료)
        ExtractableResponse<Response> completionResponse = 주문_상태변경_요청(order.getId(), OrderStatus.COMPLETION.name());

        // 주문 상태 변경됨
        주문_상태_변경됨(completionResponse, OrderStatus.COMPLETION.name());

    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long tableId, List<OrderLineItem> orderLineItems) {
        Order request = new Order();
        request.setOrderTableId(tableId);
        request.setOrderLineItems(orderLineItems);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        Order order = response.as(Order.class);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertNotNull(order.getOrderedTime());
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response, List<Order> expected) {
        List<Order> orders = response.jsonPath().getList(".", Order.class);
        List<Long> expectedIds = expected.stream()
            .map(it -> it.getOrderTableId())
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orders)
                .extracting(Order::getOrderTableId)
                .containsAll(expectedIds);
        });
    }

    public static ExtractableResponse<Response> 주문_상태변경_요청(Long orderId, String status) {
        Order request = new Order();
        request.setOrderStatus(status);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(URL+"/{orderId}/order-status", orderId)
            .then().log().all()
            .extract();
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, String expectedStatus) {
        Order order = response.as(Order.class);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(order.getOrderStatus()).isEqualTo(expectedStatus);
        });
    }
}
