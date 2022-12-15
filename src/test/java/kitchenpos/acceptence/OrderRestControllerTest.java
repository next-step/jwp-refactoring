package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.type.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptence.MenuGroupRestControllerTest.메뉴그룹을_생성한다;
import static kitchenpos.acceptence.MenuRestControllerTest.메뉴를_생성한다;
import static kitchenpos.acceptence.ProductRestControllerTest.상품을_등록한다;
import static kitchenpos.acceptence.TableRestControllerTest.주문테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRestControllerTest extends AcceptanceSupport {
    private Product 후라이드치킨;
    private Product 제로콜라;
    private MenuGroup 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    private OrderLineItem 주문항목;

    private Order 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨 = 상품을_등록한다(new Product(1L, "후라이드치킨", BigDecimal.valueOf(3_000))).as(Product.class);
        제로콜라 = 상품을_등록한다(new Product(2L, "제로콜라", BigDecimal.valueOf(2_000))).as(Product.class);

        치킨 = 메뉴그룹을_생성한다(new MenuGroup(1L, "치킨")).as(MenuGroup.class);

        후라이드_이인분 = new MenuProduct(1L, 1L, 후라이드치킨.getId(), 2);
        제로콜라_삼인분 = new MenuProduct(1L, 1L, 제로콜라.getId(), 3);

        후치콜세트 = 메뉴를_생성한다(new Menu(1L, "후치콜세트", BigDecimal.valueOf(5_000), 치킨.getId(), Arrays.asList(제로콜라_삼인분, 후라이드_이인분))).as(Menu.class);

        주문테이블 = 주문테이블을_생성한다(new OrderTable(null, null, 0, false)).as(OrderTable.class);

        주문항목 = new OrderLineItem(null, null, 후치콜세트.getId(), 1);

        주문 = new Order(null, 주문테이블.getId(), null, null, Arrays.asList(주문항목));
    }

    @Test
    @DisplayName("주문을 등록 할 수 있다.")
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성을_요청한다(주문);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("주문 리스트를 받을 수 있다.")
    void getOrderList() {
        // given
        주문 = 주문_생성을_요청한다(주문).as(Order.class);

        // when
        ExtractableResponse<Response> response = 주문_리스트를_요청한다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문_리스트를_비교한다(response, Arrays.asList(주문.getId()));
    }

    @Test
    @DisplayName("주문 상태를 변경 할 수 있다.")
    void updateOrderStatus() {
        // given
        주문 = 주문_생성을_요청한다(주문).as(Order.class);
        Order 변경한_주문 = new Order(
                주문.getId(), 주문.getOrderTableId(), OrderStatus.COOKING.name(), 주문.getOrderedTime(), 주문.getOrderLineItems()
        );

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), 변경한_주문);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문_상태가_변경되었는지_확인한다(response, OrderStatus.COOKING.name());
    }

    private ExtractableResponse<Response> 주문_생성을_요청한다(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order).when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private ExtractableResponse<Response> 주문_리스트를_요청한다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_상태_수정_요청(long id, Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{id}/order-status", id)
                .then().log().all()
                .extract();
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> getId) {
        List<Order> result = response.jsonPath().getList(".", Order.class);
        List<Long> responseId = result.stream().map(Order::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(getId);
    }

    private void 주문_상태가_변경되었는지_확인한다(ExtractableResponse<Response> response, String expect) {
        String result = response.jsonPath().getString("orderStatus");
        assertThat(result).isEqualTo(expect);
    }
}
