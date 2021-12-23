package kitchenpos.application.unit.order;


import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.OrderValidator;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    OrderValidator orderValidator;

    @DisplayName("주문을 등록한다.")
    @Test
    void createTest(){
        // given
        OrderLineItemRequest orderLineItemRequest = mock(OrderLineItemRequest.class);

        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getId()).thenReturn(1L);

        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), null, Arrays.asList(orderLineItemRequest));

        Order savedOrder = mock(Order.class);
        when(savedOrder.getId()).thenReturn(1L);
        when(orderRepository.save(any())).thenReturn(savedOrder);
        OrderService orderService = new OrderService(orderRepository, orderValidator, eventPublisher);

        // when
        Order createdOrder = orderService.create(orderRequest);

        // then
        assertThat(createdOrder.getId()).isNotNull();
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list(){

        Order order = mock(Order.class);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        OrderService orderService = new OrderService(orderRepository, orderValidator, eventPublisher);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).contains(order);
    }
  
    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {

        // given
        OrderRequest orderRequest = new OrderRequest(null, OrderStatus.COOKING, null);

        Order expectedOrder = mock(Order.class);
        when(expectedOrder.getId()).thenReturn(1L);
        when(expectedOrder.getOrderStatus()).thenReturn(OrderStatus.COOKING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(expectedOrder));

        OrderService orderService = new OrderService(orderRepository, orderValidator, eventPublisher);
        // when
        Order savedOrder = orderService.changeOrderStatus(1L, orderRequest);
        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());

    }

}
