package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문항목 객체가 같은지 검증")
    void verifyEqualsOrderLineItem() {
        final Orders order = new Orders.Builder(1L)
                .build();
        final OrderLineItem orderLineItem = new OrderLineItem.Builder(order)
                .setSeq(1L)
                .setMenuId(1L)
                .setQuantity(Quantity.of(1L))
                .builder();

        assertThat(orderLineItem).isEqualTo(new OrderLineItem.Builder(order)
                .setSeq(1L)
                .setMenuId(1L)
                .setQuantity(Quantity.of(1L))
                .builder());
    }
}
