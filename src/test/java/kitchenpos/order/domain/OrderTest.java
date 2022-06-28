package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    private OrderTable 주문테이블;
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
        주문항목 = createOrderLineItem(null, 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Order order = Order.from(주문테이블.id(), OrderLineItems.from(Lists.newArrayList(주문항목)));
        assertThat(order).isEqualTo(order);
    }
}
