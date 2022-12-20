package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 그룹 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private Product 삼겹살;
    private Product 김치;
    private MenuGroup 한식;
    private Menu 삼겹살세트메뉴;
    private MenuProduct 삼겹살메뉴싱품;
    private MenuProduct 김치상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 삼겹살세트메뉴주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        삼겹살 = 상품_생성_요청(new Product(1L, "삼겹살", BigDecimal.valueOf(5_000))).as(Product.class);
        김치 = 상품_생성_요청(new Product(2L, "김치", BigDecimal.valueOf(3_000))).as(Product.class);
        한식 = 메뉴_그룹_생성_요청(new MenuGroup(1L, "한식")).as(MenuGroup.class);
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식);
        삼겹살메뉴싱품 = new MenuProduct(1L, 삼겹살세트메뉴, 삼겹살, 1L);
        김치상품 = new MenuProduct(2L, 삼겹살세트메뉴, 김치, 1L);
        삼겹살세트메뉴.getMenuProducts().setMenuProducts(Arrays.asList(삼겹살메뉴싱품, 김치상품));
        주문테이블 = 주문_테이블_생성_요청(new OrderTable(null, null, 0, false)).as(OrderTable.class);
        삼겹살세트메뉴주문 = new OrderLineItem(null, null, 삼겹살세트메뉴.getId(), 1);
        주문 = new Order(null, 주문테이블.getId(), null, null, Arrays.asList(삼겹살세트메뉴주문));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        주문 = 주문_생성_요청(주문).as(Order.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response);
        주문_목록_확인됨(response, Arrays.asList(주문.getId()));
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void updaeOrderStatus() {
        // given
        String expectOrderStatus = OrderStatus.MEAL.name();
        Order order = 주문_생성_요청(주문).as(Order.class);
        Order changeOrder = new Order(주문.getId(), 주문.getOrderTableId(), expectOrderStatus, 주문.getOrderedTime(), 주문.getOrderLineItems());

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(order.getId(), changeOrder);

        // then
        주문_상태_수정됨(response, expectOrderStatus);
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_목록_확인됨(ExtractableResponse<Response> response, List<Long> orderIds) {
        List<Long> resultIds = response.jsonPath().getList(".", Order.class)
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(resultIds).containsAll(orderIds);
    }

    private void 주문_상태_수정됨(ExtractableResponse<Response> response, String expectedOrderStatus) {
        String result = response.jsonPath().getString("orderStatus");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expectedOrderStatus)
        );
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Order order) {
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, Order order) {
        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

}
