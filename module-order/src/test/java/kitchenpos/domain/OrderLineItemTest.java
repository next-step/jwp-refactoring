package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineItemTest {
    @Test
    @DisplayName("주문 업데이트")
    void updateOrder() {
        // given
        final Menu menu = Menu.of("햄버거", BigDecimal.valueOf(1_000), MenuGroup.of("햄버거메뉴").getId());
        final OrderLineItem 주문라인 = OrderLineItem.of(menu, 1L);
        final Order order = Order.of(1L, Arrays.asList(OrderLineItem.of(menu, 1L)));
        // when
        주문라인.updateOrder(order);
        // then
        assertThat(주문라인.getOrder()).isEqualTo(order);
    }
}
