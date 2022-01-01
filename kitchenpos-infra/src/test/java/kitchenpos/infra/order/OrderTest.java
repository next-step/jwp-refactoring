package kitchenpos.infra.order;

import kitchenpos.core.domain.Order;
import kitchenpos.core.domain.OrderLineItem;
import kitchenpos.core.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class OrderTest {
    @Autowired
    private JpaOrderRepository orderRepository;

    @DisplayName("주문은 아이디, 주문 테이블의 아이디, 주문 상태, 주문 시간, 주문 항목들로 구성되어 있다.")
    @Test
    void of() {
        // given
        final Order order = Order.of(1L, Collections.singletonList(
                OrderLineItem.of(1L, 13))
        );
        // when
        final Order actual = orderRepository.saveAndFlush(order);
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void updateOrderStatus() {
        // given
        final Order order = Order.of(1L, Collections.singletonList(
                OrderLineItem.of(1L, 13))
        );
        // when
        order.updateOrderStatus("MEAL");
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void isCompletion() {
        // given
        final Order order = Order.of(1L, Collections.singletonList(
                OrderLineItem.of(1L, 13))
        );
        // when
        order.updateOrderStatus("COMPLETION");
        // then
        assertTrue(order.isCompletion());
    }
}
