package kitchenpos.order.application;

import kitchenpos.order.application.exception.OrderNotFoundException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 테이블;
    private Order 주문;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        테이블 = OrderTable.of(2, new TableState(false));
        주문항목 = OrderLineItem.of(1L, 1L);
        주문 = Order.of(1L, COOKING, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        OrderLineItemRequest 주문항목 = OrderLineItemRequest.of(1L, 1L);
        OrderRequest 주문요청 = new OrderRequest(1L, Collections.singletonList(주문항목));

        when(orderRepository.save(any())).thenReturn(주문);
        OrderResponse response = orderService.create(주문요청);

        verify(orderRepository, times(1)).save(any(Order.class));
        assertThat(response).extracting("orderStatus").isEqualTo(COOKING.name());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(주문));

        List<OrderResponse> orders = orderService.list();

        verify(orderRepository, times(1)).findAll();
        assertThat(orders).hasSize(1);
        assertThat(orders)
                .extracting("orderStatus")
                .containsExactly(주문.getOrderStatus().name());
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderStatusRequest 주문상태 = new OrderStatusRequest("MEAL");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));

        OrderResponse response = orderService.changeOrderStatus(1L, 주문상태);

        assertThat(response.getOrderStatus()).isEqualTo(MEAL.name());
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 번호를 조회할 수 없는 경우 예외가 발생한다.")
    void validateOrderId() {
        OrderStatusRequest 주문상태 = new OrderStatusRequest("MEAL");
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문상태))
                .isInstanceOf(OrderNotFoundException.class);
        verify(orderRepository, times(1)).findById(anyLong());
    }
}