package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

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

    @DisplayName("변경하려는 주문은 존재해야 한다")
    @Test
    void orderIsExists() {
        // given
        Long notExistsOrderId = 1000L;

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
