package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static kitchenpos.domain.OrderLineItemsTest.createDuplicateOrderLineItems;
import static kitchenpos.domain.OrderLineItemsTest.createOrderLineItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @Test
    void 주문을_처리한다() {
        // given
        Order order = new Order(1L, createOrderLineItems().elements());
        LocalDateTime orderedTime = LocalDateTime.now();

        // when
        order.order(orderedTime);

        // then
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime)
        );
    }

    @Test
    void 계산_완료한_주문은_상태를_변경할_수_없다() {
        // given
        Order order = new Order(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() ->
                order.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료한 주문은 상태를 변경할 수 없습니다.");
    }
}
