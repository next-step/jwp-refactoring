package kitchenpos.order.application;

import kitchenpos.util.TestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class OrderServiceTest extends TestSupport {
    @Autowired
    private OrderService orderService;

    private Menu menu;
    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        menu = 메뉴_등록되어있음("메뉴", BigDecimal.valueOf(1000));
        orderTable = 테이블_등록되어있음(5, false);
    }

    @DisplayName("주문 등록")
    @Test
    public void 주문_등록_확인() throws Exception {
        //given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));

        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertThat(orderResponse.getId()).isNotNull();
    }

    @DisplayName("주문 목록 조회")
    @Test
    public void 주문목록_조회_확인() throws Exception {
        //given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));
        orderService.create(orderRequest);
        orderService.create(orderRequest);
        orderService.create(orderRequest);

        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @DisplayName("주문상태 변경 예외 - 주문이 없는 경우")
    @Test
    public void 주문이없는경우_주문상태_변경_예외() throws Exception {
        //given
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL);

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, orderStatusRequest))
                .hasMessage("주문이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태 변경")
    @Test
    public void 주문상태_변경_확인() throws Exception {
        //given
        Order order = 주문_등록되어있음(orderTable, Arrays.asList(new OrderLineItem(menu.getId(), 2L)));

        //when
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL);
        OrderResponse changeOrderResponse = orderService.changeOrderStatus(order.getId(), orderStatusRequest);

        //then
        assertThat(changeOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
