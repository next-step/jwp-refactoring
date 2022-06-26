package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
        Order order = Order.from(주문테이블, OrderLineItems.from(Lists.newArrayList(주문항목)), 1);
        assertThat(order).isEqualTo(order);
    }

    @DisplayName("주문항목들 초기화시 사이즈 불일치 테스트")
    @Test
    void validateOrderLineItemsSize() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Order.from(주문테이블, OrderLineItems.from(Lists.newArrayList(주문항목)), 2))
                .withMessage("비교하는 수와 주문 항목의 수가 일치하지 않습니다.");
    }

    @DisplayName("주문테이블 초기화시 주문테이블 비어있는 경우 테스트")
    @Test
    void validateNotEmpty() {
        OrderTable orderTable = createOrderTable(1L, 2, true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Order.from(orderTable, OrderLineItems.from(Lists.newArrayList(주문항목)), 1))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }
}
