package kitchenpos.order.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.ordertable.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {
    private Product 순살치킨;
    private Product 후라이드치킨;
    private MenuGroup 치킨;
    private MenuProduct 순살치킨상품;
    private MenuProduct 후라이드치킨상품;
    private Menu 두마리치킨세트;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItem 두마리치킨세트주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        순살치킨 = 상품_생성_요청(new Product(null, "순살치킨", BigDecimal.valueOf(20_000))).as(Product.class);
        후라이드치킨 = 상품_생성_요청(new Product(null, "후라이드치킨", BigDecimal.valueOf(18_000))).as(Product.class);
        치킨 = 메뉴그룹_생성_요청(new MenuGroup(null, "치킨")).as(MenuGroup.class);
        순살치킨상품 = new MenuProduct(null, 1L, 순살치킨, 두마리치킨세트);
        후라이드치킨상품 = new MenuProduct(null, 1L, 후라이드치킨, 두마리치킨세트);
        두마리치킨세트 = 메뉴_생성_요청(new Menu(
                null,
                "두마리치킨세트",
                BigDecimal.valueOf(38_000L),
                치킨,
                Arrays.asList(순살치킨상품, 후라이드치킨상품)
        )).as(Menu.class);

        주문테이블 = 주문테이블_생성_요청(new OrderTable(null, 0, false))
                .as(OrderTable.class);
        두마리치킨세트주문 = new OrderLineItem(null, 두마리치킨세트.getId(), 1);
        주문 = new Order(null, 주문테이블.getId(), null, null, Arrays.asList(두마리치킨세트주문));
    }

    @Test
    void 주문을_등록할_수_있다() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        // then
        주문_생성됨(response);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        // given
        주문 = 주문_생성_요청(주문).as(Order.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response, Arrays.asList(주문.getId()));
    }

    @Test
    void 주문_상태를_수정할_수_있다() {
        // given
        String expectedOrderStatus = OrderStatus.MEAL.name();
        주문 = 주문_생성_요청(주문).as(Order.class);
        Order 업데이트된_주문 = new Order(
                주문.getId(),
                주문.getOrderTableId(),
                expectedOrderStatus,
                주문.getOrderedTime(),
                주문.getOrderLineItems()
        );

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), 업데이트된_주문);

        // then
        주문_상태_수정됨(response, expectedOrderStatus);

    }

    private ExtractableResponse<Response> 주문_생성_요청(Order order) {
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

    private void 주문_목록_응답됨(ExtractableResponse<Response> response, List<Long> orderIds) {
        List<Long> ids = response.jsonPath().getList(".", Order.class)
                        .stream()
                        .map(Order::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(orderIds)
        );
    }

    private void 주문_상태_수정됨(ExtractableResponse<Response> response, String expect) {
        String result = response.jsonPath().getString("orderStatus");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expect)
        );
    }
}
