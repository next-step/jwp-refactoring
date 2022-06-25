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
    private OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문항목 = createOrderLineItem(null, 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Order order = new Order();
        assertThat(order).isEqualTo(order);
    }

    @DisplayName("주문항목들 추가 테스트")
    @Test
    void addOrderLineItems() {
        Order order = new Order();
        order.addOrderLineItems(OrderLineItems.from(Lists.newArrayList(주문항목)), 1);
        assertThat(order.readOnlyOrderLineItems()).isEqualTo(Lists.newArrayList(주문항목));
    }

    @DisplayName("주문항목들 추가시 사이즈 불일치 테스트")
    @Test
    void validateOrderLineItemsSize() {
        Order order = new Order();
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.addOrderLineItems(OrderLineItems.from(Lists.newArrayList(주문항목)), 2))
                .withMessage("비교하는 수와 주문 항목의 수가 일치하지 않습니다.");
    }

    @DisplayName("주문테이블 추가시 주문테이블 비어있는 경우 테스트")
    @Test
    void validateNotEmpty() {
        Order order = new Order();
        OrderTable orderTable = createOrderTable(1L, 2, true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.addOrderTable(orderTable))
                .withMessage("주문테이블이 비어있으면 안됩니다.");
    }
}
