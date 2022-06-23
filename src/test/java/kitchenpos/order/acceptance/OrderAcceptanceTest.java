package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("주문 관련 기능 인수테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    private MenuGroup 추천메뉴;
    private ProductResponse 허니콤보;
    private ProductResponse 레드콤보;
    private Menu 허니레드콤보;
    private OrderTable 손님_4명_테이블;
    private Order 주문;

    @TestFactory
    @DisplayName("주문 관련 기능 정상 시나리오")
    Stream<DynamicTest> successTest() {
        return Stream.of(
                dynamicTest("주문 등록요청하면 주문이 등록된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    레드콤보 = 상품_등록_되어있음("레드콤보", 19_000L);
                    허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보);
                    손님_4명_테이블 = 테이블_등록_되어있음(4, false);

                    ResponseEntity<Order> 주문_등록_요청_결과 = 주문_등록_요청(손님_4명_테이블, 허니레드콤보);

                    주문_등록됨(주문_등록_요청_결과);
                }),
                dynamicTest("주문 목록 조회요청하면 주문 목록이 조회된다.", () -> {
                    ResponseEntity<List<Order>> 주문_목록_조회_요청_결과 = 주문_목록_조회_요청();

                    주문_목록_조회됨(주문_목록_조회_요청_결과, 허니레드콤보);
                }),
                dynamicTest("주문 상태 변경요청하면 주문 상태가 변경된다.", () -> {
                    주문 = 주문_등록_되어있음(손님_4명_테이블, 허니레드콤보);

                    ResponseEntity<Order> 주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION.name());

                    주문_상태_변경됨(주문_상태_변경_요청_결과);
                })
        );
    }

    @TestFactory
    @DisplayName("주문 관련 기능 예외 시나리오")
    Stream<DynamicTest> failTest() {
        return Stream.of(
                dynamicTest("계산완료인 주문을 변경요청하면 변경 실패된다.", () -> {
                    추천메뉴 = 메뉴_그룹_등록되어_있음("추천메뉴");
                    허니콤보 = 상품_등록_되어있음("허니콤보", 20_000L);
                    레드콤보 = 상품_등록_되어있음("레드콤보", 19_000L);
                    허니레드콤보 = 메뉴_등록_되어있음(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보);
                    손님_4명_테이블 = 테이블_등록_되어있음(4, false);
                    주문 = 주문_등록_되어있음(손님_4명_테이블, 허니레드콤보);
                    주문_상태_변경_요청(주문, OrderStatus.COMPLETION.name());

                    ResponseEntity<Order> 계산_완료_주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.MEAL.name());

                    주문_상태_변경_실패됨(계산_완료_주문_상태_변경_요청_결과);
                })
        );
    }

    public static ResponseEntity<Order> 주문_상태_변경_요청(Order order, String orderStatus) {
        order.setOrderStatus(orderStatus);
        HttpEntity<Order> httpEntity = new HttpEntity<>(order);

        Map<String, Object> params = new HashMap<>();
        params.put("orderId", order.getId());

        return testRestTemplate.exchange("/api/orders/{orderId}/order-status",
                HttpMethod.PUT,
                httpEntity,
                Order.class,
                params);
    }

    public static Order 주문_등록_되어있음(OrderTable orderTable, Menu... menus) {
        return 주문_등록_요청(orderTable, menus).getBody();
    }

    public static ResponseEntity<Order> 주문_등록_요청(OrderTable orderTable, Menu... menus) {
        List<OrderLineItem> orderLineItems = 주문_항목_생성(menus);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        return testRestTemplate.postForEntity("/api/orders", order, Order.class);
    }

    private void 주문_상태_변경_실패됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void 주문_상태_변경됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 주문_목록_조회됨(ResponseEntity<List<Order>> response, Menu... menus) {
        List<Long> actualMenuIds = 주문_항목_메뉴_아이디_추출(response);
        List<Long> expectedMenuIds = Arrays.stream(menus)
                .map(Menu::getId)
                .collect(Collectors.toList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualMenuIds).containsExactlyElementsOf(expectedMenuIds);
    }

    private List<Long> 주문_항목_메뉴_아이디_추출(ResponseEntity<List<Order>> response) {
        List<OrderLineItem> orderLineItems = 주문_항목_조회(response);
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private List<OrderLineItem> 주문_항목_조회(ResponseEntity<List<Order>> response) {
        return response.getBody().stream()
                .map(Order::getOrderLineItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<Order>> 주문_목록_조회_요청() {
        return testRestTemplate.exchange("/api/orders", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {});
    }

    private void 주문_등록됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().get("Location")).isNotNull();
    }

    private static List<OrderLineItem> 주문_항목_생성(Menu[] menus) {
        return Arrays.stream(menus)
                .map(menu -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menu.getId());
                    orderLineItem.setQuantity(1L);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }
}
