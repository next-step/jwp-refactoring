package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderValidator orderValidator;

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        Order order = new Order(1L);
        order.addOrderLineItem(new OrderLineItem(1L, 1));

        // when
        orderService.create(order);

        // then
        verify(orderValidator).checkOrderLineItems(order);
        verify(orderValidator).checkOrderTable(order);
        verify(orderRepository).save(order);
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        orderService.list();

        // then
        verify(orderRepository).findAllWithItem();
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus() {
        // given
        Long orderId = 1L;
        Order order = new Order(1L);
        when(orderRepository.findByIdWithItem(orderId)).thenReturn(Optional.of(order));

        // when
        Order actual = orderService.changeOrderStatus(orderId, OrderStatus.MEAL);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("없는 주문은 상태를 변경할 수 없다")
    @Test
    void order_is_exists() {
        // given
        Long notExistsOrderId = 1000L;

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 된 주문은 상태를 변경할 수 없다")
    @Test
    void completion_order_cannot_change() {
        // given
        Long completionOrderId = 1000L;
        Order completionOrder = new Order(1L, OrderStatus.COMPLETION);
        when(orderRepository.findByIdWithItem(completionOrderId)).thenReturn(Optional.of(completionOrder));

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
