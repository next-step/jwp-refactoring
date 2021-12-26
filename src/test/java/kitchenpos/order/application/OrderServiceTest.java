package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문상태를 변경한다. 요리중 -> 식사중")
    void modifyOrderStatusMealTest() {
        //given
        Order order = mock(Order.class);
        when(order.getOrderStatus()).thenReturn(OrderStatus.MEAL.name());
        //when
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        orderService.changeOrderStatus(1L, OrderStatus.MEAL);
        verify(order).startMeal();
    }

    @Test
    @DisplayName("주문상태를 변경한다. 식사중 -> 완료")
    void modifyOrderStatusCompletionTest() {
        //given
        Order order = mock(Order.class);
        when(order.getOrderStatus()).thenReturn(OrderStatus.MEAL.name());
        //when
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        orderService.changeOrderStatus(1L, OrderStatus.COMPLETION);
        verify(order).endOrder();
    }

    @Test
    @DisplayName("주문 내역을 조회한다.")
    void findOrderTest() {
        //given
        Order order = mock(Order.class);
        //when
        when(orderRepository.findAll())
                .thenReturn(Arrays.asList(order));
        //then
        List<OrderResponse> orders = orderService.findAll();

        assertThat(orders.size()).isEqualTo(1);
        verify(orderRepository).findAll();

    }

    @Test
    @DisplayName("주문상태를 변경한다. 완료 후에 다시 상태를 변경 할 수 없다.  완료 -> 요리중")
    void modifyOrderStatusErrorTest() {

        //given
        Order order = mock(Order.class);
        when(order.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());
        //when
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        orderService.changeOrderStatus(1L, OrderStatus.MEAL);
        verify(order).startMeal();
//        //given
//        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L, 5)));
//
//        Order order = new Order(new OrderTable(4, true), orderLineItems);
//        order.endOrder();
//        //when
//        when(orderRepository.findById(anyLong()))
//                .thenReturn(Optional.of(order));
//
//        assertThatThrownBy(() -> {
//            orderService.changeOrderStatus(1L, OrderStatus.MEAL);
//        }).isInstanceOf(InputOrderDateException.class)
//                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }
}
