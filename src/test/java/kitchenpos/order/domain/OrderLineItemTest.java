package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {
    @Test
    void 주문과의_연관관계를_설정할_수_있어야_한다() {
        // given
        final OrderLineItem given = new OrderLineItem(1L, new Quantity(10));

        // when
        final Order order = new Order(1L, 1L);
        given.relateToOrder(order);

        // then
        assertThat(given.getOrder()).isEqualTo(order);
    }

    @Test
    void 이미_연관관계가_있는_상태라면_주문과의_연관관계_설정_시_에러가_발생해야_한다() {
        // given
        final Order order = new Order(1L, 1L);
        final OrderLineItem given = new OrderLineItem(1L, order, 1L, new Quantity(10));

        // when and then
        assertThatThrownBy(() -> given.relateToOrder(order))
                .isInstanceOf(IllegalStateException.class);
    }
}
