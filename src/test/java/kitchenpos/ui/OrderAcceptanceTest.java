package kitchenpos.ui;

import static kitchenpos.ui.MenuAcceptanceTest.메뉴_생성;
import static kitchenpos.ui.MenuGroupAcceptanceTest.메뉴그룹_생성;
import static kitchenpos.ui.TableAcceptanceTest.주문테이블_생성;
import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static kitchenpos.utils.AcceptanceTestUtil.put;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 추천메뉴;
    private ProductResponse 후라이드치킨_상품;
    private ProductResponse 양념치킨_상품;
    private MenuProductRequest 후라이드치킨_메뉴;
    private MenuProductRequest 양념치킨_메뉴;

    private OrderLineItemRequest 후라이드치킨;
    private OrderLineItemRequest 양념치킨;
    private List<OrderLineItemRequest> 후라이드_양념치킨_주문_항목;
    private List<OrderLineItemRequest> 양념치킨_주문_항목;
    private List<OrderLineItemRequest> 후라이드치킨_주문_항목;
    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        추천_메뉴_그룹_생성();
        양념치킨_후라이드치킨_상품_생성();
        양념치킨_후라이드치킨_메뉴_생성();
        테이블_2개_생성();

        후라이드치킨 = new OrderLineItemRequest(1L, 1);
        양념치킨 = new OrderLineItemRequest(2L, 1);
        후라이드_양념치킨_주문_항목 = Lists.newArrayList(후라이드치킨, 양념치킨);
        양념치킨_주문_항목 = Lists.newArrayList(양념치킨);
        후라이드치킨_주문_항목 = Lists.newArrayList(후라이드치킨);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        // given
        final OrderRequest 테이블1에서_후라이드_양념_메뉴주문 = new OrderRequest(테이블1.getId(), 후라이드_양념치킨_주문_항목);

        // when
        ExtractableResponse<Response> 주문생성_응답 = 주문_생성(테이블1에서_후라이드_양념_메뉴주문);

        // then
        주문_생성됨(주문생성_응답);
    }

    @DisplayName("주문목록을 조회한다")
    @Test
    void readOrders() {
        // given
        final OrderRequest 후라이드치킨_주문 = new OrderRequest(테이블1.getId(), 후라이드치킨_주문_항목);
        final OrderRequest 양념치킨_주문 = new OrderRequest(테이블2.getId(), 양념치킨_주문_항목);

        OrderResponse 후라이드치킨_주문_응답 = 주문_생성(후라이드치킨_주문).as(OrderResponse.class);
        OrderResponse 양념치킨_주문_응답 = 주문_생성(양념치킨_주문).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> 주문목록_조회_응답 = 주문목록_조회();

        // then
        주문목록_조회됨(주문목록_조회_응답);
        주문목록에_주문내역_포함됨(주문목록_조회_응답, 후라이드치킨_주문_응답, 양념치킨_주문_응답);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        final OrderRequest order = new OrderRequest(테이블1.getId(), 후라이드_양념치킨_주문_항목);
        OrderResponse 주문생성_응답 = 주문_생성(order).as(OrderResponse.class);

        // when
        OrderStatusRequest 식사_상태로_변경 = new OrderStatusRequest(OrderStatus.MEAL);

        ExtractableResponse<Response> 주문상태_변경_응답 = 주문상태_변경(주문생성_응답, 식사_상태로_변경);

        // then
        주문상태_변경됨(주문상태_변경_응답);
    }

    private ExtractableResponse<Response> 주문_생성(OrderRequest order) {
        return post("/api/orders", order);
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        OrderResponse order = response.as(OrderResponse.class);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    private ExtractableResponse<Response> 주문목록_조회() {
        return get("/api/orders");
    }

    private void 주문목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문목록에_주문내역_포함됨(ExtractableResponse<Response> response,
        OrderResponse 후라이드치킨_주문, OrderResponse 양념치킨_주문) {
        List<OrderResponse> orders = Lists.newArrayList(response.as(OrderResponse[].class));
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(OrderResponse::getId)
            .contains(양념치킨_주문.getId(), 후라이드치킨_주문.getId());
    }

    private ExtractableResponse<Response> 주문상태_변경(OrderResponse 주문생성_응답,
        OrderStatusRequest order) {
        String url = String.format("/api/orders/%s/order-status", 주문생성_응답.getId());
        return put(url, order);
    }

    private void 주문상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    private void 추천_메뉴_그룹_생성() {
        추천메뉴 = 메뉴그룹_생성(new MenuGroupRequest("추천메뉴")).as(MenuGroupResponse.class);
    }

    private void 테이블_2개_생성() {
        테이블1 = 주문테이블_생성(2, false);
        테이블2 = 주문테이블_생성(3, false);
    }

    private void 양념치킨_후라이드치킨_메뉴_생성() {
        후라이드치킨_메뉴 = new MenuProductRequest(후라이드치킨_상품.getId(), 1);
        양념치킨_메뉴 = new MenuProductRequest(양념치킨_상품.getId(), 1);

        List<MenuProductRequest> menuProducts1 = Lists.newArrayList(후라이드치킨_메뉴, 양념치킨_메뉴);
        List<MenuProductRequest> menuProducts2 = Lists.newArrayList(후라이드치킨_메뉴);
        List<MenuProductRequest> menuProducts3 = Lists.newArrayList(양념치킨_메뉴);
        MenuRequest menu1 = new MenuRequest("후라이드+양념치킨", 34000, 추천메뉴.getId(), menuProducts1);
        MenuRequest menu2 = new MenuRequest("후라이드", 17000, 추천메뉴.getId(), menuProducts2);
        MenuRequest menu3 = new MenuRequest("양념치킨", 18000, 추천메뉴.getId(), menuProducts3);

        메뉴_생성(menu1);
        메뉴_생성(menu2);
        메뉴_생성(menu3);
    }

    private void 양념치킨_후라이드치킨_상품_생성() {
        후라이드치킨_상품 = ProductAcceptanceTest.상품_생성(new ProductRequest("후라이드치킨", 17000)).as(ProductResponse.class);
        양념치킨_상품 = ProductAcceptanceTest.상품_생성(new ProductRequest("양념치킨", 18000)).as(ProductResponse.class);
    }
}