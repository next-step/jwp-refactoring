package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 주문 도메인 테스트")
class OrderTest {

    private Long orderTableId;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        orderTableId = 1L;
        orderLineItem1 = OrderLineItem.of(1L, 1L);
        orderLineItem2 = OrderLineItem.of(1L, 2L);
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create01() {
        // given & when
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));

        // then
        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }
}
