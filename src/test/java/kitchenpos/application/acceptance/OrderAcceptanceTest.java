package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.application.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.application.acceptance.OrderTableAcceptanceTest.주문_테이블_생성_요청;
import static kitchenpos.application.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends BaseAcceptanceTest {

    private OrderTable 주문_테이블;

    private MenuGroup 메뉴_그룹;

    private Product 상품;

    private MenuProduct 메뉴_상품;

    private Menu 메뉴;

    private OrderLineItem 주문_항목;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        주문_테이블 = 주문_테이블_생성_요청(3, false).as(OrderTable.class);

        메뉴_그룹 = 메뉴_그룹_생성_요청("신메뉴").as(MenuGroup.class);
        상품 = 상품_생성_요청("녹두빈대떡", new BigDecimal(7000)).as(Product.class);
        메뉴_상품 = new MenuProduct();
        메뉴_상품.setProductId(상품.getId());
        메뉴_상품.setQuantity(1);

        메뉴 = 메뉴_생성_요청("녹두빈대떡", new BigDecimal(7000), 메뉴_그룹.getId(), Arrays.asList(메뉴_상품)).as(Menu.class);
        주문_항목 = new OrderLineItem();
        주문_항목.setMenuId(메뉴.getId());
        주문_항목.setQuantity(1);
    }

    /**
     * When 주문을 생성하면
     * Then 주문 상태가 COOKING 인 주문 데이터가 리턴된다
     */
    @DisplayName("기본 주문 생성")
    @Test
    void createOrder() {
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목));

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 주문_생성_요청_응답.statusCode()),
                () -> assertNotNull(주문_생성_요청_응답.as(Order.class).getId()),
                () -> assertEquals(OrderStatus.COOKING.name(), 주문_생성_요청_응답.as(Order.class).getOrderStatus())
        );
    }

    /**
     * Given 존재하지 않는 메뉴ID 를 이용하여 주문 항목을 생성한 후
     * When 주문 항목을 이용하여 주문을 생성하면
     * Then 주문 항목의 수와 메뉴의 수가 일치하지 않아 에러가 발생한다.
     */
    @DisplayName("메뉴 정보가 존재하지 않는 주문 항목을 이용한 주문 생성")
    @Test
    void createOrderWithoutOrderLineItem() {
        // given
        OrderLineItem 메뉴가_존재하지_않는_주문_항목 = new OrderLineItem();
        메뉴가_존재하지_않는_주문_항목.setMenuId(100L);
        메뉴가_존재하지_않는_주문_항목.setQuantity(1);

        // when
        ExtractableResponse<Response> 주문_생성_요청_응답
                = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목, 메뉴가_존재하지_않는_주문_항목));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_생성_요청_응답.statusCode());
    }

    /**
     * Given 비어 있는 주문 테이블을 생성하고
     * When 주문을 생성하면
     * Then 에러가 발생한다.
     */
    @DisplayName("비어 있는 테이블을 포함한 주문 생성")
    @Test
    void createOrderWithEmptyOrderTable() {
        // given
        OrderTable 빈_주문_테이블 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 주문_생성_요청_응답
                = 주문_생성_요청(빈_주문_테이블.getId(), Arrays.asList(주문_항목));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_생성_요청_응답.statusCode());
    }

    /**
     * When 주문을 생성하면
     * Then 주문 테이블의 ID 와 주문의 테이블 ID 가 동일하게 지정된다.
     */
    @DisplayName("주문 테이블과 주문의 테이블ID 일치")
    @Test
    void createOrderWithOrderTableId() {
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답
                = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목));

        // then
        assertEquals(주문_테이블.getId(), 주문_생성_요청_응답.as(Order.class).getOrderTableId());
    }

    /**
     * When 주문을 생성하면
     * Then 주문 시간이 등록된다.
     */
    @DisplayName("주문 시간 등록")
    @Test
    void setOrderedTime() {
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답
                = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목));

        // then
        assertNotNull(주문_생성_요청_응답.as(Order.class).getOrderedTime());
    }

    /**
     * When 주문을 생성하면
     * Then 주문 항목이 조회된다.
     */
    @DisplayName("주문 생성 후 주문 항목 리턴 여부 확인")
    @Test
    void hasOrderLineItem() {
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답
                = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목));

        // then
        assertThat(주문_생성_요청_응답.as(Order.class).getOrderLineItems()).hasSize(1);
    }


    /**
     * Given 주문 1건을 등록한 후
     * When 주문 목록을 조회하면
     * Then 주문별 주문 항목이 함께 조회된다.
     */
    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        // given
        주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목));

        // when
        ExtractableResponse<Response> 주문_목록_조회_요청_응답 = 주문_목록_조회_요청();

        // then
        assertThat(주문_목록_조회_요청_응답.jsonPath().getList("orderLineItem")).hasSize(1);
    }

    /**
     * Given 주문 1건을 등록한 후
     * When 주문 상태를 MEAL 로 변경하면
     * Then 주문의 상태가 MEAL 로 변경된다.
     */
    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        Order 주문 = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목)).as(Order.class);

        // when
        Order 상태_변경된_주문 = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL).as(Order.class);

        // then
        assertEquals(OrderStatus.MEAL.name(), 상태_변경된_주문.getOrderStatus());
    }

    /**
     * Given 주문 1건을 등록한 후
     * When 등록한 주문이 아닌 임의의 주문ID 로 상태를 변경하면
     * Then 오류가 발생한다.
     */
    @DisplayName("등록되지 않은 주문ID 를 이용한 상태 변경")
    @Test
    void changeOrderStatusWithUnavailableOrderId() {
        // given
        Order 주문 = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목)).as(Order.class);

        // when
        ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(200L, OrderStatus.MEAL);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_상태_변경_요청_응답.statusCode());
    }

    /**
     * Given 주문 1건을 등록하고
     *       And 상태를 COMPLETION 으로 변경한 후
     * When 다시 주문 상태를 COOKING 으로 변경하면
     * Then 오류가 발생한다.
     */
    @DisplayName("상태가 COMPLETION 인 주문")
    @Test
    void changeOrderStatusWithCompletionStatus() {
        // given
        Order 주문 = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목)).as(Order.class);

        // when
        주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);
        ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(주문.getId(), OrderStatus.COOKING);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 주문_상태_변경_요청_응답.statusCode());
    }

    /**
     * Given 주문 1건을 등록한 후
     * When 주문 상태를 변경하면
     * Then 주문 항목도 함께 리턴된다.
     */
    @DisplayName("주문 항목 리턴 확인")
    @Test
    void hasOrderLineItemAfterChangingStatus() {
        // given
        Order 주문 = 주문_생성_요청(주문_테이블.getId(), Arrays.asList(주문_항목)).as(Order.class);

        // when
        Order 상태_변경된_주문 = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL).as(Order.class);

        // then
        assertThat(상태_변경된_주문.getOrderLineItems()).hasSize(1);
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(long orderId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());

        return RestAssured
                .given().log().all()
                .body(order)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }
}
