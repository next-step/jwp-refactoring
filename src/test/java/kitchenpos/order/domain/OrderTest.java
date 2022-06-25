package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    private OrderLineItem 주문항목;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문항목 = createOrderLineItem(1L, null, null, 2L);
        주문테이블 = createOrderTable(1L, null, 2, false);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Order order = Order.from(1L, 주문테이블, OrderStatus.COOKING);
        assertThat(order).isEqualTo(order);
    }
}
