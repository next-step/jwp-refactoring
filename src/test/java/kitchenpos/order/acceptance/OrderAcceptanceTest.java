package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.order.acceptance.OrderTableAcceptanceTest.주문테이블_빈테이블_여부_수정_요청;
import static kitchenpos.order.acceptance.OrderTableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련 기능 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {
    private Menu 후라이드치킨;
    private OrderTable 주문테이블;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // 메뉴 생성
        Product 상품 = 상품_생성_요청("후라이드", new BigDecimal(18_000)).as(Product.class);
        MenuGroup 메뉴그룹 = 메뉴그룹_생성_요청("한마리치킨").as(MenuGroup.class);
        후라이드치킨 = 메뉴_생성_요청("후라이드치킨",
                new BigDecimal(18_000),
                메뉴그룹.getId(),
                Lists.newArrayList(new MenuProduct(상품.getId(), 1))).as(Menu.class);

        // 주문테이블 생성
        주문테이블 = 주문테이블_생성_요청(2, false).as(OrderTable.class);
    }

    /**
     * Given : 주문테이블과 메뉴가 생성되어 있다.
     * When : 주문 생성을 요청한다.
     * Then : 주문이 생성된다.
     */
    @DisplayName("주문 생성 인수 테스트")
    @Test
    void creatOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문테이블, 후라이드치킨);

        // then
        주문_생성됨(response);
    }

    /**
     * Given : 주문테이블과 메뉴가 생성되어 있다.
     * When : 빈 테이블에서 주문 생성을 요청한다.
     * Then : 주문이 실패한다..
     */
    @DisplayName("주문 생성시 빈테이블 예외 인수 테스트")
    @Test
    void creatOrderExceptionByEmptyTable() {
        // given
        주문테이블 = 주문테이블_빈테이블_여부_수정_요청(주문테이블.getId(), true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문테이블, 후라이드치킨);

        // then
        주문_생성_실패됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고,
     * When : 주문 목록 조회를 요청한다.
     * Then : 주문 목록을 응답한다.
     */
    @DisplayName("주문 조회 인수 테스트")
    @Test
    void findOrders() {
        // given
        주문_생성_요청(주문테이블, 후라이드치킨);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_조회됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고,
     * When : 주문의 상태 변경을 요청한다.
     * Then : 주문의 상태가 변경된다.
     */
    @DisplayName("주문 상태 변경 요청 인수 테스트")
    @Test
    void changeOrderStatus() {
        // given
        Order 주문 = 주문_생성_요청(주문테이블, 후라이드치킨).as(Order.class);

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), "MEAL");

        // then
        주문_상태_수정됨(response);
    }

    /**
     * Given : 주문이 생성되어 있고, 주문의 상태가 완료되어 있다
     * When : 주문의 상태를 수정 요청하면
     * Then : 주문의 상태 변경이 실패한다.
     */
    @DisplayName("주문 상태 변경 요청 인수 테스트")
    @Test
    void changeOrderStatusException_이미완료된_주문() {
        // given
        Order 주문 = 주문_생성_요청(주문테이블, 후라이드치킨).as(Order.class);
        주문_상태_수정_요청(주문.getId(), "COMPLETION");

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), "COOKING");

        // then
        주문_상태_수정_실패됨(response);
    }


    public static ExtractableResponse<Response> 주문_상태_수정_요청(Long orderId, String OrderStatus) {
        Order orderRequest = new Order();
        orderRequest.setOrderStatus(OrderStatus);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(OrderTable orderTable, Menu menu) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        Order orderRequest = new Order(orderTable.getId(), Lists.newArrayList(orderLineItem));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
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

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_수정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
