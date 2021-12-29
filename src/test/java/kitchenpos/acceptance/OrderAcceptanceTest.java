package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.TestApiClient;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 테이블;
    private Menu 메뉴;
    private Product 소고기한우;
    private MenuGroup 추천메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroup.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(Product.of("소고기한우", 30000));
        메뉴 = MenuAcceptanceTest.메뉴_등록되어있음("소고기+소고기", 50000, 추천메뉴, Arrays.asList(MenuProduct.of(소고기한우, 2L)));
        테이블 = TableAcceptanceTest.테이블_등록되어_있음(OrderTable.of(4, false));
    }

    @DisplayName("주문 관리")
    @Test
    void handleOrder() {
        // 주문 생성
        Order order = Order.of(
                테이블,
                Arrays.asList(
                        OrderLineItem.of(메뉴, 2)
                )
        );
        ExtractableResponse<Response> createResponse = 주문_생성_요청(order);
        Order savedOrder = 주문_생성_확인(createResponse);

        // 주문 조회
        ExtractableResponse<Response> findResponse = 모든_주문_조회_요청();
        모든_주문_조회_확인(findResponse, savedOrder);

        // 주문 상태 변경
        Order changeOrder = Order.of(OrderStatus.COMPLETION.name());
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(savedOrder.getId(), changeOrder);
        주문_상태_변경_확인(updateResponse, changeOrder);
    }

    public static void 주문_상태_변경_확인(ExtractableResponse<Response> updateResponse, Order changeOrder) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = updateResponse.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
    }

    private void 모든_주문_조회_확인(ExtractableResponse<Response> findResponse, Order expected) {
        assertAll(
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    List<Order> orders = findResponse.jsonPath().getList(".", Order.class);
                    List<OrderLineItem> orderLineItems = orders.stream()
                            .map(Order::getOrderLineItems)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    assertThat(orders).contains(expected);
                    assertThat(orderLineItems).containsAll(expected.getOrderLineItems());
                }
        );
    }

    private Order 주문_생성_확인(ExtractableResponse<Response> createResponse) {
        Order savedOrder = createResponse.as(Order.class);
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> {
                    assertThat(savedOrder.getOrderTableId()).isEqualTo(테이블.getId());
                    assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                    assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
                }
        );
        return savedOrder;
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long id, Order changeOrder) {
        return TestApiClient.update(changeOrder, "/api/orders/" + id + "/order-status");
    }

    private ExtractableResponse<Response> 모든_주문_조회_요청() {
        return TestApiClient.get("/api/orders");
    }

    private static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return TestApiClient.create(order, "/api/orders");
    }

    public static Order 주문_생성됨(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = Order.of(orderTable, orderLineItems);
        return 주문_생성_요청(order).as(Order.class);
    }

}
