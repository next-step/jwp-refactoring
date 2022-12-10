package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupRestAssured.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_등록되어_있음;
import static kitchenpos.acceptance.OrderRestAssured.주문_등록되어_있음;
import static kitchenpos.acceptance.OrderRestAssured.주문_목록_조회_요청;
import static kitchenpos.acceptance.OrderRestAssured.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderRestAssured.주문_생성_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 관련 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroup 두마리메뉴;
    private Product 후라이드;
    private Menu 후라이드치킨;
    private OrderTable 주문_테이블;
    private OrderTable 비어있는_주문_테이블;

    /**
     * Given 메뉴 그룹 등록되어 있음
     * And 상품 등록되어 있음
     * And 메뉴 등록되어 있음
     * And 주문 테이블 등록되어 있음
     * And 비어있는 주문 테이블 등록되어 있음
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        두마리메뉴 = 메뉴_그룹_등록되어_있음(MenuGroup.of(1L, "두마리메뉴")).as(MenuGroup.class);

        후라이드 = 상품_등록되어_있음(Product.of(1L, "후라이드", BigDecimal.valueOf(16_000))).as(Product.class);

        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(MenuProduct.of(1L, null, 후라이드.getId(), 2));
        후라이드치킨 = 메뉴_등록되어_있음(Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품_목록))
                .as(Menu.class);

        주문_테이블 = 주문_테이블_등록되어_있음(OrderTable.of(null, 2, false)).as(OrderTable.class);
        비어있는_주문_테이블 = 주문_테이블_등록되어_있음(OrderTable.of(null, 2, true)).as(OrderTable.class);

    }

    /**
     * When 주문 생성 요청
     * Then 주문 생성됨
     */
    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 주문 = Order.of(주문_테이블.getId(), 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        주문_생성됨(response);
    }

    /**
     * When 주문항목 없이 주문 생성 요청
     * Then 주문 생성 실패함
     */
    @DisplayName("주문 항목이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void createFail() {
        Order 주문 = Order.of(주문_테이블.getId(), Collections.emptyList());

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        주문_생성_실패함(response);
    }

    /**
     * When 메뉴에 등록되어 있지 않은 주문항목으로 주문 생성 요청
     * Then 주문 생성 실패함
     */
    @DisplayName("주문 항목이 메뉴에 등록되어 있지 않다면 주문을 생성할 수 없다.")
    @Test
    void createFail2() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(0L, 2));
        Order 주문 = Order.of(주문_테이블.getId(), 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        주문_생성_실패함(response);
    }

    /**
     * When 등록되어 있지 않은 주문 테이블에 주문 생성 요청
     * Then 주문 생성 실패함
     */
    @DisplayName("주문 테이블이 등록되어 있지 않다면 주문을 생성할 수 없다.")
    @Test
    void createFail3() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 주문 = Order.of(0L, 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        주문_생성_실패함(response);
    }

    /**
     * When 비어있는 주문 테이블에 주문 생성 요청
     * Then 주문 생성 실패함
     */
    @DisplayName("주문 테이블이 빈 테이블이면 주문을 생성할 수 없다.")
    @Test
    void createFail4() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 주문 = Order.of(비어있는_주문_테이블.getId(), 주문_항목);

        ExtractableResponse<Response> response = 주문_생성_요청(주문);

        주문_생성_실패함(response);
    }

    /**
     * Given 주문 등록되어 있음
     * When 주문 목록 조회 요청
     * Then 주문 목록 조회됨
     */
    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 주문 = Order.of(주문_테이블.getId(), 주문_항목);

        Order 등록된_주문 = 주문_등록되어_있음(주문).as(Order.class);

        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        주문_목록_조회됨(response, 등록된_주문);
    }

    /**
     * Given 주문 등록되어 있음
     * When 주문 상태 변경 요청
     * Then 주문 상태 변경됨
     */
    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 주문 = Order.of(주문_테이블.getId(), 주문_항목);

        Order 등록된_주문 = 주문_등록되어_있음(주문).as(Order.class);
        등록된_주문.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> response = 주문_상태_변경_요청(등록된_주문.getId(), 등록된_주문);

        주문_상태_변경됨(response, 등록된_주문);
    }

    /**
     * When 등록되지 않은 주문 상태 변경 요청
     * Then 주문 상태 변경 실패함
     */
    @DisplayName("주문이 없으면 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFail() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 등록되지_않은_주문 = Order.of(0L, 주문_테이블.getId(), 주문_항목);
        등록되지_않은_주문.setOrderStatus(OrderStatus.MEAL.name());

        ExtractableResponse<Response> response = 주문_상태_변경_요청(등록되지_않은_주문.getId(), 등록되지_않은_주문);

        주문_상태_변경_실패함(response);
    }

    /**
     * Given 주문 등록되어 있음
     * And 계산완료 주문 상태 변경됨
     * When 주문 상태 변경 요청
     * Then 주문 상태 변경 실패함
     */
    @DisplayName("주문 상태가 계산 완료이면 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFail2() {
        List<OrderLineItem> 주문_항목 = Arrays.asList(OrderLineItem.of(후라이드치킨.getId(), 2));
        Order 등록된_주문 = 주문_등록되어_있음(Order.of(주문_테이블.getId(), 주문_항목)).as(Order.class);

        등록된_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 계산완료된_주문 = 주문_상태_변경_요청(등록된_주문.getId(), 등록된_주문).as(Order.class);

        계산완료된_주문.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> response = 주문_상태_변경_요청(계산완료된_주문.getId(), 계산완료된_주문);

        주문_상태_변경_실패함(response);
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_생성_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response, Order order) {
        List<Order> orders = response.jsonPath().getList(".", Order.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0)).isEqualTo(order),
                () -> assertThat(orders.get(0).getOrderLineItems())
                        .containsExactlyInAnyOrder(order.getOrderLineItems().get(0))
        );
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, Order order) {

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(order.getOrderStatus())
        );
    }

    private void 주문_상태_변경_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
