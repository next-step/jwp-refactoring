package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문항목 객체가 같은지 검증")
    void verifyEqualsOrderLineItem() {
        final Menu menu = new Menu.Builder("menu")
                .setPrice(Price.of(1_000L))
                .build();
        final Orders order = new Orders.Builder(1L)
                .build();
        final OrderLineItem orderLineItem = new OrderLineItem.Builder(order)
                .setSeq(1L)
                .setMenu(menu)
                .setQuantity(Quantity.of(1L))
                .builder();

        assertThat(orderLineItem).isEqualTo(new OrderLineItem.Builder(order)
                .setSeq(1L)
                .setMenu(menu)
                .setQuantity(Quantity.of(1L))
                .builder());
    }
}
