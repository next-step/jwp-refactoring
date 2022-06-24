package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴그룹_등록_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_등록_요청;
import static kitchenpos.product.acceptance.ProductRestAssured.상품_등록_요청;
import static kitchenpos.order.acceptance.TableRestAssured.주문테이블_등록_요청;
import static kitchenpos.utils.DomainFixtureFactory.createMenu;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createOrder;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.utils.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {
    private Order 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroup 한마리메뉴 = 메뉴그룹_등록_요청(createMenuGroup(1L, "한마리메뉴")).as(MenuGroup.class);
        Product 양념 = 상품_등록_요청(createProduct(1L, "양념", BigDecimal.valueOf(20000L))).as(Product.class);
        Menu 양념치킨 = 메뉴_등록_요청(createMenu(1L, "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴,
                Lists.newArrayList(createMenuProduct(1L, null, 양념, 2L)))).as(Menu.class);
        OrderTable 주문테이블 = 주문테이블_등록_요청(createOrderTable(null, null, 2, false)).as(OrderTable.class);
        주문 = createOrder(null, 주문테이블, null, Lists.newArrayList(createOrderLineItem(1L, null, 양념치킨.id(), 2L)));
    }

    /**
     * When 주문을 등록 요청하면
     * Then 주문이 등록 됨
     */
    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 주문_등록_요청(주문);

        // then
        주문_등록됨(response);
    }

    /**
     * Given 주문을 등록하고
     * When 주문을 조회 하면
     * Then 주문 목록 조회 됨
     */
    @DisplayName("주문 목록을 조회 한다.")
    @Test
    void lists() {
        // given
        Order 등록한_주문 = 주문_등록_요청(주문).as(Order.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response, Lists.newArrayList(등록한_주문));
    }

    /**
     * Given 주문을 등록하고
     * When 주문 상태를 변경 요청하면
     * Then 주문 상태 변경 된다.
     */
    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order 등록한_주문 = 주문_등록_요청(주문).as(Order.class);

        // when
        OrderStatus orderStatus = OrderStatus.COMPLETION;
        ExtractableResponse<Response> response = 주문_상태_변경_요청(등록한_주문, orderStatus);

        // then
        주문_상태_변경됨(response, orderStatus);
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response, List<Order> expectedOrders) {
        List<Order> orders = response.jsonPath().getList(".", Order.class);
        assertThat(orders).containsExactlyElementsOf(expectedOrders);
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.as(Order.class).orderStatus()).isEqualTo(orderStatus);
    }

    private ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_상태_변경_요청(Order targetOrder, OrderStatus orderStatus) {
        Order order = createOrder(1L, null, orderStatus, null);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status", targetOrder.id())
                .then().log().all()
                .extract();
    }
}
