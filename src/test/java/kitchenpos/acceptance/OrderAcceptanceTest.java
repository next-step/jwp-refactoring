package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {
    private Product 불고기;
    private Product 김치;
    private Product 공기밥;
    private MenuGroup 한식;
    private MenuProductRequest 불고기상품;
    private MenuProductRequest 김치상품;
    private MenuProductRequest 공기밥상품;
    private MenuRequest 불고기정식;
    private MenuResponse 불고기정식응답;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderLineItem 불고기정식주문;
    private OrderLineItemRequest 불고기정식주문요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        불고기 = 상품_생성_요청(new Product(1L, "불고기", BigDecimal.valueOf(10_000))).as(Product.class);
        김치 = 상품_생성_요청(new Product(2L, "김치", BigDecimal.valueOf(1_000))).as(Product.class);
        공기밥 = 상품_생성_요청(new Product(3L, "공기밥", BigDecimal.valueOf(1_000))).as(Product.class);
        한식 = 메뉴그룹_생성_요청(new MenuGroup(1L, "한식")).as(MenuGroup.class);
        불고기상품 = MenuProductRequest.of(불고기.getId(), 1L);
        김치상품 = MenuProductRequest.of(김치.getId(), 1L);
        공기밥상품 = MenuProductRequest.of(공기밥.getId(), 1L);
        불고기정식응답 = 메뉴_생성_요청(MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(12_000L),
                한식.getId(),
                Arrays.asList(불고기상품, 김치상품, 공기밥상품)
        )).as(MenuResponse.class);

        주문테이블 = 주문테이블_생성_요청(new OrderTable(null, 0, false))
                .as(OrderTable.class);
        불고기정식주문 = new OrderLineItem(null, 불고기정식응답.getId(), 1);
        주문 = new Order(주문테이블, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(불고기정식주문));
        불고기정식주문요청 = new OrderLineItemRequest(불고기정식응답.getId(), 1L);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // when
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));
        ExtractableResponse<Response> response = 주문_생성_요청(request);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));
        OrderResponse 생성된_주문 = 주문_생성_요청(request).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        // then
        주문_목록_응답됨(response, Arrays.asList(생성된_주문.getId()));
    }

    @DisplayName("주문 상태를 수정할 수 있다.")
    @Test
    void updateOrderStatus() {
        // given
        OrderStatus expectedOrderStatus = OrderStatus.MEAL;
        OrderRequest request = OrderRequest.of(주문테이블.getId(), Arrays.asList(불고기정식주문요청));
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
