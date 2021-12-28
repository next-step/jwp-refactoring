package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

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
        verify(order).updateOrderStatus(OrderStatus.MEAL);
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
        verify(order).updateOrderStatus(OrderStatus.COMPLETION);
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
    void modifyCompletionToCookingOrderStatusErrorTest() {

        //given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L, 5)));

        Order order = new Order(new OrderTable(4, false), orderLineItems);
        order.endOrder();
        //when
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, OrderStatus.COOKING);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }

    @Test
    @DisplayName("주문상태를 변경한다. 완료 후에 다시 상태를 변경 할 수 없다.  완료 -> 식사중")
    void modifyCompletionToEatingOrderStatusErrorTest() {

        //given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L, 5)));

        Order order = new Order(new OrderTable(4, false), orderLineItems);
        order.endOrder();
        //when
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(1L, OrderStatus.MEAL);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }
}
