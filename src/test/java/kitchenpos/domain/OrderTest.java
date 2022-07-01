package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static kitchenpos.domain.OrderLineItemsTest.createDuplicateOrderLineItems;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void 중복된_메뉴가_있으면_주문할_수_없다() {
        // given
        Order order = new Order(1L, createDuplicateOrderLineItems().elements());

        // when & then
        assertThatThrownBy(() ->
                order.validateDuplicateMenu(1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 메뉴가 있습니다.");
    }
}
