package kitchenpos.order;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private Menu menu;

    private OrderTable orderTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        final MenuGroup menuGroup = menuGroupDao.save(MenuGroup.builder().name("추천메뉴").build());
        menu = menuDao.save(Menu.builder().name("후라이드+후라이드").price(BigDecimal.valueOf(17000)).menuGroup(menuGroup).build());
        orderTable = orderTableDao.save(OrderTable.builder().build());
    }

    @Test
    @DisplayName("주문을 할 수 있다.")
    void createOrder() {
        // given
        final OrderCreateRequest OrderCreateRequest = new OrderCreateRequest(orderTable.getId(), Arrays.asList(new OrderCreateRequest.OrderLineItem(menu.getId(), 1L)));

        // when
        final ExtractableResponse<Response> 주문_요청_응답 = 주문_요청(OrderCreateRequest);

        // then
        주문_완료_됨(주문_요청_응답);
    }

    private void 주문_완료_됨(final ExtractableResponse<Response> response) {
        final OrderResponse 완료된_주문 = response.as(OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(완료된_주문.getId()).isNotNull(),
                () -> assertThat(완료된_주문.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(완료된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(완료된_주문.getOrderedTime()).isBefore(LocalDateTime.now()),
                () -> assertThat(완료된_주문.getOrderLineItems().size()).isOne()
        );
    }

    public ExtractableResponse<Response> 주문_요청(final OrderCreateRequest request) {
        return post("/api/orders", request);
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), Arrays.asList(new OrderCreateRequest.OrderLineItem(menu.getId(), 1L)));

        final OrderResponse savedOrder = 주문(orderCreateRequest);

        // when
        final ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(savedOrder);

        // then
        주문_상태_변경_됨(주문_상태_변경_요청_응답);
    }

    private void 주문_상태_변경_됨(final ExtractableResponse<Response> response) {
        final OrderResponse 변경된_주문_상태 = response.as(OrderResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_주문_상태.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name())
        );
    }

    public ExtractableResponse<Response> 주문_상태_변경_요청(final OrderResponse order) {
        return put("/api/orders/{orderId}/order-status", new OrderStatusChangeRequest(OrderStatus.COMPLETION), order.getId());
    }

    private OrderResponse 주문(final OrderCreateRequest request) {
        return 주문_요청(request).as(OrderResponse.class);
    }
}
