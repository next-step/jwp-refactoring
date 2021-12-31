package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@DisplayName("주문 서비스 테스트")
@SpringBootTest(classes = OrderValidator.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 내역을 조회한다.")
    void findOrderTest() {
        //given
        Order order = Mockito.mock(Order.class);
        //when
        Mockito.when(orderRepository.findAll())
                .thenReturn(Arrays.asList(order));
        //then
        List<OrderResponse> orders = orderService.findAll();

        Assertions.assertThat(orders.size()).isEqualTo(1);
        Mockito.verify(orderRepository).findAll();

    }

    @Test
    @DisplayName("주문상태를 변경한다. 완료 후에 다시 상태를 변경 할 수 없다.  완료 -> 요리중")
    void modifyCompletionToCookingOrderStatusErrorTest() {

        //given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L, 5)));
        Order order = new Order(1L, orderLineItems);
        order.endOrder();

        Assertions.assertThatThrownBy(() -> {
            orderValidator.isAlreadyCompletionOrder(order);
            orderService.changeOrderStatus(1L, OrderStatus.COOKING);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }

    @Test
    @DisplayName("주문상태를 변경한다. 완료 후에 다시 상태를 변경 할 수 없다.  완료 -> 식사중")
    void modifyCompletionToEatingOrderStatusErrorTest() {

        //given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(new OrderLineItem(1L, 5)));
        Order order = new Order(1L, orderLineItems);
        order.endOrder();

        Assertions.assertThatThrownBy(() -> {
            orderValidator.isAlreadyCompletionOrder(order);
            orderService.changeOrderStatus(1L, OrderStatus.MEAL);
        }).isInstanceOf(InputOrderDataException.class)
                .hasMessageContaining(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER.errorMessage());
    }
}
