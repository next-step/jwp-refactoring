package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class OrderTest {

    private List<OrderLineItem> orderLineItems;
    OrderLineItem orderLineItem1;
    OrderLineItem orderLineItem2;
    @BeforeEach
    void setUp() {
        orderLineItem1 = OrderLineItem.of(1L, BigDecimal.valueOf(100), "메뉴", 3);
        orderLineItem2 = OrderLineItem.of(1L, BigDecimal.valueOf(100), "메뉴1", 2);

        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("주문 테이블은 존재 해야 한다.")
    void orderTableIsNull() {
        OrderLineItems orderLineItems = OrderLineItems.from(Collections.singletonList(orderLineItem1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(null, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
    }

    @Test
    @DisplayName("주문목록은 존재 해야 한다.")
    void orderLineItemsIsNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(1L, OrderStatus.COOKING, LocalDateTime.now(), null));
    }

    @Test
    @DisplayName("주문 상태가 완료이면 변경 할 수 없다.")
    void orderCompleteIsNotChange() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.from(orderLineItems));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changOrderStatus(OrderStatus.COOKING));
    }

    @Test
    @DisplayName("변경할 상태값이 없으면 변경이 되지 않는다")
    void orderStatusIsNotNull() {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.from(orderLineItems));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changOrderStatus(null));
    }
}
