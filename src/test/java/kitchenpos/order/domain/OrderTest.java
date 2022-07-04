package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void 주문의_상태를_변경할_수_있어야_한다() {
        // given
        final OrderStatus targetStatus = OrderStatus.COMPLETION;

        // when
        final Order order = new Order();
        order.changeOrderStatus(targetStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(targetStatus);
    }

    @Test
    void 변경하려는_주문의_상태값이_null이면_에러가_발생해야_한다() {
        // given
        final Order given = new Order();

        // when and then
        assertThatThrownBy(() -> given.changeOrderStatus(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 완료된_주문의_상태_변경_시_에러가_발생해야_한다() {
        // given
        final Order given = new Order(OrderStatus.COMPLETION);

        // when and then
        assertThatThrownBy(() -> given.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 변경하려는_주문의_상태가_현재_주문의_상태와_같으면_에러가_발생해야_한다() {
        // given
        final Order given = new Order(OrderStatus.MEAL);

        // when and then
        assertThatThrownBy(() -> given.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }
}
