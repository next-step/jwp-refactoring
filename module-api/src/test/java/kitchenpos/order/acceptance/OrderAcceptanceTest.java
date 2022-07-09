package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menuGroup.acceptance.MenuGroupAcceptanceTest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.orderTable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성되어_있음;
import static kitchenpos.orderTable.acceptance.OrderTableAcceptanceTest.주문_테이블_생성되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 치킨_메뉴_그룹;
    private ProductResponse 후라이드치킨;
    private ProductResponse 양념치킨;
    private MenuRequest.ProductInfo 후라이트치킨_수량;
    private MenuRequest.ProductInfo 양념치킨_수량;
    private List<MenuRequest.ProductInfo> 상품_수량_정보;
    private MenuResponse 치킨메뉴;
    private OrderTableResponse 주문_테이블;

    @BeforeEach
    void setup() {
        super.setUp();

        치킨_메뉴_그룹 = MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음("치킨세트").as(MenuGroupResponse.class);
        후라이드치킨 = 상품_생성되어_있음("후라이드치킨", 15_000).as(ProductResponse.class);
        양념치킨 = 상품_생성되어_있음("양념치킨", 16_000).as(ProductResponse.class);
        후라이트치킨_수량 = new MenuRequest.ProductInfo(후라이드치킨.getProductId(), 1);
        양념치킨_수량 = new MenuRequest.ProductInfo(양념치킨.getProductId(), 1);
        상품_수량_정보 = Arrays.asList(후라이트치킨_수량, 양념치킨_수량);
        MenuRequest menuRequest = new MenuRequest("치킨세트", BigDecimal.valueOf(30_000), 치킨_메뉴_그룹.getMenuGroupId(), 상품_수량_정보);
        치킨메뉴 = 메뉴_생성되어_있음(menuRequest).as(MenuResponse.class);

        주문_테이블 = 주문_테이블_생성되어_있음(2, false).as(OrderTableResponse.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void creatOrder() {
        // given
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Collections.singletonList(new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1));

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문_테이블.getOrderTableId(), 메뉴_수량_정보);

        // then
        주문_생성_요청_됨(response, 주문_테이블.getOrderTableId(), 메뉴_수량_정보);
    }

    @ParameterizedTest(name = "주문항목이 1개 이상이여야 한다.")
    @EmptySource
    void creatOrder1(List<OrderRequest.MenuInfo> 메뉴_수량_정보) {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문_테이블.getOrderTableId(), 메뉴_수량_정보);

        // then
        주문_생성_요청_실패(response);
    }

    @DisplayName("주문항목과 메뉴의 갯수가 일치해야 한다.")
    @Test
    void creatOrder2() {
        // given
        OrderRequest.MenuInfo 치킨_메뉴_수량 = new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1);
        OrderRequest.MenuInfo 존재하지_않는_메뉴_수량 = new OrderRequest.MenuInfo(2L, 1);
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Arrays.asList(치킨_메뉴_수량, 존재하지_않는_메뉴_수량);

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문_테이블.getOrderTableId(), 메뉴_수량_정보);

        // then
        주문_생성_요청_실패(response);
    }

    @DisplayName("주문 테이블이 비여있으면 안된다.")
    @Test
    void creatOrder3() {
        // given
        OrderTableResponse 주문_테이블 = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Collections.singletonList(new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1));

        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문_테이블.getOrderTableId(), 메뉴_수량_정보);

        // then
        주문_생성_요청_실패(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void listOrder() {
        // given
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Collections.singletonList(new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1));
        OrderResponse orderResponse = 주문_생성되어_있음(주문_테이블.getOrderTableId(), 메뉴_수량_정보).as(OrderResponse.class);
        List<OrderResponse> orderResponses = Collections.singletonList(orderResponse);

        // when
        ExtractableResponse<Response> response = 주문_목록조회_요청();

        // then
        주문_목록조회_요청_됨(response, orderResponses.size());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Collections.singletonList(new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1));
        OrderResponse orderResponse = 주문_생성되어_있음(주문_테이블.getOrderTableId(), 메뉴_수량_정보).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_상태변경_요청(orderResponse.getOrderId(), OrderStatus.MEAL);

        // then
        주문_상태변경_요청_됨(response);
    }

    @DisplayName("주문의 현재 상태가 '식사' 또는 '조리' 이여만 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus1() {
        // given
        List<OrderRequest.MenuInfo> 메뉴_수량_정보 = Collections.singletonList(new OrderRequest.MenuInfo(치킨메뉴.getMenuId(), 1));
        OrderResponse orderResponse = 주문_생성되어_있음(주문_테이블.getOrderTableId(), 메뉴_수량_정보).as(OrderResponse.class);
        주문_상태변경_요청(orderResponse.getOrderId(), OrderStatus.MEAL);

        // when
        주문_상태변경_요청(orderResponse.getOrderId(), OrderStatus.COMPLETION);
        ExtractableResponse<Response> response = 주문_상태변경_요청(orderResponse.getOrderId(), OrderStatus.MEAL);

        // then
        주문_상태변경_요청_실패(response);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId, List<OrderRequest.MenuInfo> menuInfos) {
        OrderRequest orderRequest = new OrderRequest(orderTableId, menuInfos);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성되어_있음(Long orderTableId, List<OrderRequest.MenuInfo> menuInfos) {
        return 주문_생성_요청(orderTableId, menuInfos);
    }

    private static void 주문_생성_요청_됨(ExtractableResponse<Response> response, Long orderTableId, List<OrderRequest.MenuInfo> 메뉴_수량_정보) {
        OrderResponse orderResponse = response.as(OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderResponse.getOrderId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(orderTableId),
                () -> assertThat(orderResponse.getMenuIds()).hasSize(메뉴_수량_정보.size())
        );
    }

    private static void 주문_생성_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 주문_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private static void 주문_목록조회_요청_됨(ExtractableResponse<Response> response, int size) {
        List<OrderResponse> orderResponses = response.body()
                .jsonPath()
                .getList(".", OrderResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderResponses).hasSize(size)
        );
    }

    private static ExtractableResponse<Response> 주문_상태변경_요청(Long orderId, OrderStatus orderStatus) {
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(orderStatus);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeOrderStatusRequest)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then().log().all()
                .extract();
    }

    private void 주문_상태변경_요청_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_상태변경_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
