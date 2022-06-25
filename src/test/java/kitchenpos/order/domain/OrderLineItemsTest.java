package kitchenpos.order.domain;

import static kitchenpos.utils.DomainFixtureFactory.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {
    OrderLineItem 주문항목;

    @BeforeEach
    void setUp() {
        주문항목 = createOrderLineItem(1L, null, null, 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        OrderLineItems orderLineItems = OrderLineItems.from(Lists.newArrayList(주문항목));
        assertThat(orderLineItems.readOnlyOrderLineItems()).isEqualTo(Lists.newArrayList(주문항목));
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> OrderLineItems.from(null))
                .withMessage("주문 항목이 필요합니다.");
    }
}
