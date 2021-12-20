package kitchenpos.ui;

import static kitchenpos.ui.TableAcceptanceTest.주문테이블_생성;
import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static kitchenpos.utils.AcceptanceTestUtil.put;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    private OrderLineItem 후라이드치킨;
    private OrderLineItem 양념치킨;
    private List<OrderLineItem> 후라이드_양념치킨_주문_항목;
    private List<OrderLineItem> 양념치킨_주문_항목;
    private List<OrderLineItem> 후라이드치킨_주문_항목;
    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨 = OrderLineItem.of(1L, 1L, 1);
        양념치킨 = OrderLineItem.of(1L, 2L, 1);
        후라이드_양념치킨_주문_항목 = Lists.newArrayList(후라이드치킨, 양념치킨);
        양념치킨_주문_항목 = Lists.newArrayList(양념치킨);
        후라이드치킨_주문_항목 = Lists.newArrayList(후라이드치킨);
        테이블1 = 주문테이블_생성(OrderTable.of(2)).as(OrderTable.class);
        테이블2 = 주문테이블_생성(OrderTable.of(3)).as(OrderTable.class);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        // given
        Order order = Order.of(테이블1.getId(), "COOKING", 후라이드_양념치킨_주문_항목);

        // when
        ExtractableResponse<Response> 주문생성_응답 = 주문_생성(order);

        // then
        주문_생성됨(주문생성_응답);
    }

    @DisplayName("주문목록을 조회한다")
    @Test
    void readOrders() {
        // given
        Order 후라이드치킨_주문 = Order.of(테이블1.getId(), "COOKING", 후라이드치킨_주문_항목);
        Order 양념치킨_주문 = Order.of(테이블2.getId(), "COOKING", 양념치킨_주문_항목);

        후라이드치킨_주문 = 주문_생성(후라이드치킨_주문).as(Order.class);
        양념치킨_주문 = 주문_생성(양념치킨_주문).as(Order.class);

        // when
        ExtractableResponse<Response> 주문목록_조회_응답 = 주문목록_조회();

        // then
        주문목록_조회됨(주문목록_조회_응답);
        주문목록에_주문내역_포함됨(주문목록_조회_응답, 후라이드치킨_주문, 양념치킨_주문);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        Order order = Order.of(테이블1.getId(), "COOKING", 후라이드_양념치킨_주문_항목);
        Order 주문생성_응답 = 주문_생성(order).as(Order.class);

        // when
        Order orderForUpdate = new Order(주문생성_응답.getId(), 테이블1.getId(), "MEAL",
            주문생성_응답.getOrderedTime(), 후라이드_양념치킨_주문_항목);

        ExtractableResponse<Response> 주문상태_변경_응답 = 주문상태_변경(orderForUpdate);

        // then
        주문상태_변경됨(주문상태_변경_응답);
    }

    private ExtractableResponse<Response> 주문_생성(Order order) {
        return post("/api/orders", order);
    }

    private void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo("COOKING");
        assertThat(order.getOrderLineItems()).hasSize(2);
    }

    private ExtractableResponse<Response> 주문목록_조회() {
        return get("/api/orders");
    }

    private void 주문목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문목록에_주문내역_포함됨(ExtractableResponse<Response> response,
        Order 후라이드치킨_주문, Order 양념치킨_주문) {
        List<Order> orders = Lists.newArrayList(response.as(Order[].class));
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getId)
            .contains(양념치킨_주문.getId(), 후라이드치킨_주문.getId());
    }

    private ExtractableResponse<Response> 주문상태_변경(Order order) {
        String url = String.format("/api/orders/%s/order-status", order.getId());
        return put(url, order);
    }

    private void 주문상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Order order = response.as(Order.class);
        assertThat(order.getOrderStatus()).isEqualTo("MEAL");
    }

}