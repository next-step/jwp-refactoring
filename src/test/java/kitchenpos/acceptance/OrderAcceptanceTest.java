package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTable 테이블;
    private Menu 메뉴;
    private Product 소고기한우;
    private MenuGroup 추천메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 메뉴가 등록되어 있어야 한다.
        // 메뉴 그룹 등록되어 있어야 한다.
        // 상품이 등록되어어 있어야 한다.
        // 주문 테이블이 등록되어 있어야 한다.
        추천메뉴 = MenuAcceptanceTest.메뉴그룹_등록되어있음(MenuGroup.of("추천메뉴"));
        소고기한우 = MenuAcceptanceTest.상품_등록되어있음(Product.of("소고기한우", 30000));
        메뉴 = MenuAcceptanceTest.메뉴_등록되어있음(
                Menu.of(
                        "소고기+소고기",
                        BigDecimal.valueOf(50000),
                        추천메뉴.getId(),
                        Arrays.asList(MenuProduct.of(소고기한우.getId(), 2L)))
        );
        테이블 = TableAcceptanceTest.테이블_등록되어_있음(OrderTable.of(4, false));
    }


    @DisplayName("주문 관리")
    @Test
    void handleOrder() {
        // 주문 생성
        Order order = Order.of(
                테이블.getId(),
                Arrays.asList(
                        OrderLineItem.of(메뉴.getId(), 2)
                )
        );
        ExtractableResponse<Response> createResponse = 주문_생성_요청(order);
        Order savedOrder = 주문_생성_확인(createResponse);

        // 주문 조회
        ExtractableResponse<Response> findResponse = 주문_조회_요청();
        주문_조회_확인(findResponse, savedOrder.getOrderLineItems());

        // 주문 상태 변경
        Order changeOrder = Order.of(OrderStatus.COMPLETION.name());
        ExtractableResponse<Response> updateResponse = 주문_상태_변경_요청(savedOrder.getId(), changeOrder);
        주문_상태_변경_확인(updateResponse, changeOrder);
    }

    private void 주문_상태_변경_확인(ExtractableResponse<Response> updateResponse, Order changeOrder) {
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = updateResponse.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
    }

    private void 주문_조회_확인(ExtractableResponse<Response> findResponse, List<OrderLineItem> expected) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Order> orders = findResponse.jsonPath().getList(".", Order.class);
        List<OrderLineItem> orderLineItems = orders.stream()
                .map(Order::getOrderLineItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        assertThat(orderLineItems).containsAll(expected);
    }

    private Order 주문_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Order savedOrder = createResponse.as(Order.class);
        assertThat(savedOrder.getOrderTableId()).isEqualTo(테이블.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
        return savedOrder;
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(Long id, Order changeOrder) {
        return RestAssured
                .given().log().all()
                .body(changeOrder)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/" + id + "/order-status")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

}
