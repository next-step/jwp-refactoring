package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {
    private static final String ORDER_URL = "/api/orders";

    private OrderTable 주문테이블;
    private Menu 불고기_새우버거_메뉴;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        주문테이블 = TableAcceptanceTest.테이블_추가_되어_있음(orderTable);

        final MenuGroup 메뉴_그룹 = MenuGroupAcceptanceTest.메뉴_그룹_추가_되어_있음(MenuGroupAcceptanceTest.햄버거_메뉴);
        final ProductResponse 불고기버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.불고기버거);
        final ProductResponse 새우버거 = ProductAcceptanceTest.제품_추가_되어_있음(ProductAcceptanceTest.새우버거);

        final MenuProduct 불고기 = new MenuProduct();
        불고기.setProductId(불고기버거.getId());
        불고기.setQuantity(1L);

        final MenuProduct 새우 = new MenuProduct();
        새우.setProductId(새우버거.getId());
        새우.setQuantity(1L);

        Menu 불고기버거_새우버거 = new Menu();
        불고기버거_새우버거.setName("불고기버거 + 새우버거");
        불고기버거_새우버거.setPrice(BigDecimal.valueOf(2000.0));
        불고기버거_새우버거.setMenuGroupId(메뉴_그룹.getId());
        불고기버거_새우버거.setMenuProducts(Arrays.asList(불고기, 새우));
        불고기_새우버거_메뉴 = MenuAcceptanceTest.메뉴가_추가_되어_있음(불고기버거_새우버거);
    }

    /**
     * given 테이블에 손님이 setting 되어 있음
     *       메뉴가 등록 되어 있음
     * when 주문 요청을 함
     * then 주문 요청이 됨
     *
     * when 전체 주문 요청 조회를 함
     * then 주무한 요청이 조회 됨
     *
     * when 요청
     * then 요청한 주문이 변경 됨
     */
    @Test
    @DisplayName("주문 관리 테스트")
    void order() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(불고기_새우버거_메뉴.getId());
        orderLineItem.setQuantity(1);

        final Order order = new Order();
        order.setOrderTableId(주문테이블.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        // when
        final ExtractableResponse<Response> 주문_요청 = 주문_요청(order);
        // then
        final Order 주문_요청_됨 = 주문_요청_됨(주문_요청);

        // when
        ExtractableResponse<Response> 주문_전체_조회 = 주문_전체_조회();
        // then
        final List<Order> 주문_조회_됨 = 주문_조회_됨(주문_전체_조회);
        assertThat(주문_조회_됨).contains(주문_요청_됨);

        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        // then
        final ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(주문_요청_됨.getId(), order);
        // when
        final Order 주문_상태_변경_됨 = 주문_상태_변경_됨(주문_상태_변경_요청);
        assertThat(주문_상태_변경_됨.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    public static ExtractableResponse<Response> 주문_요청(final Order order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post(ORDER_URL)
                .then().log().all()
                .extract();
    }

    public static Order 주문_요청_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Order.class);
    }

    public static Order 주문_요청_되어_있음(final Order order) {
        return 주문_요청_됨(주문_요청(order));
    }

    public static ExtractableResponse<Response> 주문_전체_조회() {
        return RestAssured.given()
                .when().get(ORDER_URL)
                .then()
                .extract();
    }

    public static List<Order> 주문_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", Order.class);
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final Long orderId, final Order order) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().put(ORDER_URL + "/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static Order 주문_상태_변경_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.as(Order.class);
    }
}
