package kitchenpos.order;


import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.menu.MenuAcceptanceUtil.신메뉴_강정치킨_가져오기;
import static kitchenpos.table.TableAcceptanceTest.테이블_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 테이블;
    private OrderTable 빈_테이블;
    private Menu 강정치킨;
    private Order 주문;

    @DisplayName("주문 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        return Stream.of(
                dynamicTest("기초 데이터를 추가한다.", () -> {
                    테이블 = 테이블_등록됨(false, 5);
                    빈_테이블 = 테이블_등록됨(true, 0);
                    강정치킨 = 신메뉴_강정치킨_가져오기();
                }),
                dynamicTest("주문을 등록한다.", () -> {
                    ResponseEntity<Order> response = 주문_생성_요청(테이블, 강정치킨);

                    주문_생성됨(response);
                    주문_생성시_조리상태_확인(response);
                    주문 = response.getBody();
                }),
                dynamicTest("주문 항목 없이 주문을 등록한다.", () -> {
                    ResponseEntity<Order> response = 주문_생성_요청(테이블);

                    주문_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 메뉴가 포함된 주문 항목으로 주문을 등록한다.", () -> {
                    Menu 존재하지_않는_메뉴 = new Menu();
                    존재하지_않는_메뉴.setId(Long.MAX_VALUE);

                    ResponseEntity<Order> response = 주문_생성_요청(테이블, 존재하지_않는_메뉴);

                    주문_생성_실패됨(response);
                }),
                dynamicTest("빈 테이블에 주문을 등록한다.", () -> {
                    ResponseEntity<Order> response = 주문_생성_요청(빈_테이블, 강정치킨);

                    주문_생성_실패됨(response);
                }),
                dynamicTest("주문 목록을 조회한다.", () -> {
                    ResponseEntity<List<Order>> response = 주문_목록_조회_요청();

                    주문_목록_응답됨(response);
                    주문_목록_주문에_주문_항목이_포함됨(response, 강정치킨);
                }),
                dynamicTest("주문의 상태를 변경한다. (조리 -> 식사)", () -> {
                    ResponseEntity<Order> response = 주문_상태_변경_요청(주문, OrderStatus.MEAL);

                    주문_상태_변경됨(response);
                }),
                dynamicTest("주문의 상태를 변경한다. (식사 -> 계산 완료)", () -> {
                    ResponseEntity<Order> response = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    주문_상태_변경됨(response);
                }),
                dynamicTest("주문의 상태를 변경한다. (계산 완료 -> 계산 완료)", () -> {
                    ResponseEntity<Order> response = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    주문_상태_변경_실패됨(response);
                }),
                dynamicTest("존재하지 않는 주문의 상태를 변경한다.", () -> {
                    Order 존재하지_않는_주문 = new Order();
                    존재하지_않는_주문.setId(Long.MAX_VALUE);

                    ResponseEntity<Order> response = 주문_상태_변경_요청(존재하지_않는_주문, OrderStatus.MEAL);

                    주문_상태_변경_실패됨(response);
                })
        );
    }

    public static Order 주문_등록됨(OrderTable orderTable, Menu... menus) {
        return 주문_생성_요청(orderTable, menus).getBody();
    }

    public static ResponseEntity<Order> 주문_생성_요청(OrderTable orderTable, Menu... menus) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTableId", orderTable.getId());
        request.put("orderLineItems", toOrderLoneItems(menus));
        return restTemplate().postForEntity("/api/orders", request, Order.class);
    }

    private static List<OrderLineItem> toOrderLoneItems(Menu[] menus) {
        return Arrays.stream(menus)
                .map(m -> {
                    OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(m.getId());
                    orderLineItem.setQuantity(1L);
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }

    public static ResponseEntity<List<Order>> 주문_목록_조회_요청() {
        return restTemplate().exchange("/api/orders", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {
                });
    }

    public static ResponseEntity<Order> 주문_상태_변경_요청(Order order, OrderStatus status) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("orderId", order.getId());

        Map<String, Object> request = new HashMap<>();
        request.put("orderStatus", status.name());
        return restTemplate().exchange("/api/orders/{orderId}/order-status", HttpMethod.PUT,
                new HttpEntity<>(request), Order.class, urlVariables);
    }

    public static void 주문_생성됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    public static void 주문_생성시_조리상태_확인(ResponseEntity<Order> response) {
        Order order = response.getBody();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_생성_실패됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 주문_목록_응답됨(ResponseEntity<List<Order>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 주문_상태_변경됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 주문_상태_변경_실패됨(ResponseEntity<Order> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 주문_목록_주문에_주문_항목이_포함됨(ResponseEntity<List<Order>> response, Menu... menus) {
        List<Long> menuIds = response.getBody()
                .stream()
                .flatMap(o -> o.getOrderLineItems().stream())
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        List<Long> expectedIds = Arrays.stream(menus)
                .map(Menu::getId)
                .collect(Collectors.toList());
        assertThat(menuIds).containsExactlyElementsOf(expectedIds);
    }
}
