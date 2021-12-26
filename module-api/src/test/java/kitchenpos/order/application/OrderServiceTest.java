package kitchenpos.order.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
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
    MenuRepository menuRepository;

    @DisplayName("주문을 등록한다.")
    @Test
    void createTest(){
        // given
        OrderLineItemRequest orderLineItemRequest = mock(OrderLineItemRequest.class);

        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getId()).thenReturn(1L);
        when(menuRepository.findById(orderLineItemRequest.getMenuId())).thenReturn(Optional.of(new Menu()));


        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), null, Arrays.asList(orderLineItemRequest));

        Order savedOrder = mock(Order.class);
        when(savedOrder.getId()).thenReturn(1L);
        when(orderRepository.save(any())).thenReturn(savedOrder);
        OrderService orderService = new OrderService(orderRepository, menuRepository, eventPublisher);

        // when
        OrderResponse createdOrder = orderService.create(orderRequest);

        // then
        assertThat(createdOrder.getId()).isNotNull();
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list(){

        Order order = mock(Order.class);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        OrderService orderService = new OrderService(orderRepository, menuRepository, eventPublisher);

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).contains(OrderResponse.from(order));
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

        OrderService orderService = new OrderService(orderRepository, menuRepository, eventPublisher);
        // when
        OrderResponse savedOrder = orderService.changeOrderStatus(1L, orderRequest);
        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());

    }

}
