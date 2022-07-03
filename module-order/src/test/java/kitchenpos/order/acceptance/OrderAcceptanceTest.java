package kitchenpos.order.acceptance;

import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_등록_요청;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroupRequest;
import static kitchenpos.utils.DomainFixtureFactory.createMenuRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItemRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderRequest;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTableRequest;
import static kitchenpos.utils.DomainFixtureFactory.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {
    private OrderRequest 주문;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroupResponse 한마리메뉴 = 메뉴그룹_등록_요청(createMenuGroupRequest("한마리메뉴")).as(MenuGroupResponse.class);
        ProductResponse 양념 = 상품_등록_요청(createProductRequest( "양념", BigDecimal.valueOf(20000L))).as(ProductResponse.class);
        MenuResponse 양념치킨 = 메뉴_등록_요청(createMenuRequest( "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.getId(),
                Lists.newArrayList(new MenuProductRequest(양념.getId(), 2L)))).as(MenuResponse.class);
        OrderTableResponse 주문테이블 = 주문테이블_등록_요청(createOrderTableRequest(2, false)).as(OrderTableResponse.class);
        주문 = createOrderRequest(주문테이블.getId(), null, Lists.newArrayList(createOrderLineItemRequest(양념치킨.getId(), 2L)));
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
        OrderResponse 등록한_주문 = 주문_등록_요청(주문).as(OrderResponse.class);

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
        OrderResponse 등록한_주문 = 주문_등록_요청(주문).as(OrderResponse.class);

        // when
        OrderStatus orderStatus = OrderStatus.COMPLETION;
        ExtractableResponse<Response> response = 주문_상태_변경_요청(등록한_주문, orderStatus);

        // then
        주문_상태_변경됨(response, orderStatus);
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_목록_조회됨(ExtractableResponse<Response> response, List<OrderResponse> expectedOrders) {
        List<OrderResponse> orders = response.jsonPath().getList(".", OrderResponse.class);
        Assertions.assertThat(orders).containsExactlyElementsOf(expectedOrders);
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> response, OrderStatus orderStatus) {
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(orderStatus);
    }

    private ExtractableResponse<Response> 주문_등록_요청(OrderRequest order) {
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

    private ExtractableResponse<Response> 주문_상태_변경_요청(OrderResponse targetOrder, OrderStatus orderStatus) {
        주문.setOrderStatus(orderStatus);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(주문)
                .when().put("/api/orders/{orderId}/order-status", targetOrder.getId())
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 상품_등록_요청(ProductRequest productRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
