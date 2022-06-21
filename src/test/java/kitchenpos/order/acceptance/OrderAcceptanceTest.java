package kitchenpos.order.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능 인수테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    private MenuGroup 추천메뉴;
    private Product 허니콤보;
    private Product 레드콤보;
    private Menu 허니레드콤보;
    private OrderTable 손님_4명_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴 = 메뉴_그룹_등록_요청("추천메뉴").getBody();
        허니콤보 = 상품_등록_요청("허니콤보", 20_000L).getBody();
        레드콤보 = 상품_등록_요청("레드콤보", 19_000L).getBody();
        허니레드콤보 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보).getBody();
        손님_4명_테이블 = 테이블_등록_요청(4, false).getBody();
    }

    /**
     * Feature 주문 관련 기능
     *
     * Backgroud
     * Given 메뉴그룹 등록되어 있음
     * And 상품 등록되어 있음
     * And 메뉴 등록되어 있음
     * And 테이블 등록되어 있음
     *
     * Screnario 주문 관련 기능
     * When 주문 등록 요청
     * Then 주문 등록됨
     * When 주문 목록 조회 요청
     * Then 주문 목록 조회됨
     * When 주문 상태 변경 요청
     * Then 주문 상태 변경됨
     * 
     * Given 주문 계산 완료 상태
     * When 주문 상태 변경 요청
     * Then 주문 상태 변경 실패됨
     */
    @Test
    @DisplayName("주문 관련 기능")
    void integrationTest() {
        //when
        ResponseEntity<Order> 주문_등록_요청_결과 = 주문_등록_요청(손님_4명_테이블, 허니레드콤보);
        Order 주문 = 주문_등록_요청_결과.getBody();
        //then
        주문_등록됨(주문_등록_요청_결과);

        //when
        ResponseEntity<List<Order>> 주문_목록_조회_요청_결과 = 주문_목록_조회_요청();
        //then
        주문_목록_조회됨(주문_목록_조회_요청_결과, 허니레드콤보);

        //when
        ResponseEntity<Order> 주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION.name());
        //then
        주문_상태_변경됨(주문_상태_변경_요청_결과);

        //when
        ResponseEntity<Order> 계산_완료_주문_상태_변경_요청_결과 = 주문_상태_변경_요청(주문, OrderStatus.MEAL.name());
        //then
        주문_상태_변경_실패됨(계산_완료_주문_상태_변경_요청_결과);
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
