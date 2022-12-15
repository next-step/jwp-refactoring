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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
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
    private Menu 순살치킨메뉴;
    private MenuProductRequest 순살치킨상품;
    private MenuProductRequest 후라이드치킨상품;
    private MenuResponse 두마리치킨세트_응답;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItemRequest 두마리치킨세트_요청;
    private OrderLineItem 두마리치킨세트_주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        순살치킨 = 상품_생성_요청(new Product(1L, "순살치킨", BigDecimal.valueOf(20_000))).as(Product.class);
        후라이드치킨 = 상품_생성_요청(new Product(2L, "후라이드치킨", BigDecimal.valueOf(18_000))).as(Product.class);
        치킨 = 메뉴그룹_생성_요청(new MenuGroup(1L, "치킨")).as(MenuGroup.class);
        순살치킨메뉴 = new Menu("불고기정식", BigDecimal.valueOf(12_000L), 치킨);
        순살치킨상품 = MenuProductRequest.of(순살치킨.getId(), 1L);
        후라이드치킨상품 = MenuProductRequest.of(후라이드치킨.getId(), 1L);
        두마리치킨세트_응답 = 메뉴_생성_요청(MenuRequest.of(
                "두마리치킨세트",
                BigDecimal.valueOf(38_000L),
                치킨.getId(),
                Arrays.asList(순살치킨상품, 후라이드치킨상품)
        )).as(MenuResponse.class);

        주문테이블 = 주문테이블_생성_요청(new OrderTable(null, 0, false))
                .as(OrderTable.class);
        두마리치킨세트_주문 = new OrderLineItem(null, 두마리치킨세트_응답.getId(), 순살치킨메뉴);
        두마리치킨세트_요청 = new OrderLineItemRequest(두마리치킨세트_응답.getId(), 1L);
        주문 = new Order(주문테이블, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(두마리치킨세트_주문));
    }

    @Test
    void 주문을_등록할_수_있다() {
        // when
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(두마리치킨세트_요청));
        ExtractableResponse<Response> response = 주문_생성_요청(request);

        // then
        주문_생성됨(response);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        // given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(두마리치킨세트_요청));
        OrderResponse 생성된_주문 = 주문_생성_요청(request).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response, Arrays.asList(생성된_주문.getId()));
    }

    @Test
    void 주문_상태를_수정할_수_있다() {
        // given
        OrderStatus expectedOrderStatus = OrderStatus.MEAL;
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(두마리치킨세트_요청));
        OrderResponse 생성된_주문 = 주문_생성_요청(request).as(OrderResponse.class);
        UpdateOrderStatusRequest updateRequest = UpdateOrderStatusRequest.of(OrderStatus.MEAL.name());

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(생성된_주문.getId(), updateRequest);

        // then
        주문_상태_수정됨(response, expectedOrderStatus.name());

    }

    private ExtractableResponse<Response> 주문_생성_요청(OrderRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
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

    private ExtractableResponse<Response> 주문_상태_수정_요청(long id, UpdateOrderStatusRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/orders/{id}/order-status", id)
                .then().log().all()
                .extract();
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_목록_응답됨(ExtractableResponse<Response> response, List<Long> orderIds) {
        List<Long> ids = response.jsonPath().getList(".", OrderResponse.class)
                        .stream()
                        .map(OrderResponse::getId)
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
