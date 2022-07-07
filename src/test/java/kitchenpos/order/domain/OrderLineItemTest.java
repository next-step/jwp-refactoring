package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineItemTest {
    @Test
    @DisplayName("주문 업데이트")
    void updateOrder() {
        // given
        final OrderLineItem 주문라인 = OrderLineItem.of(MenuTest.햄버거메뉴, 1L);
        final Order order = Order.of(1L, Arrays.asList(OrderLineItem.of(MenuTest.햄버거메뉴, 1L)));
        // when
        주문라인.updateOrder(order);
        // then
        System.out.println(주문라인);
        assertThat(주문라인.getOrder()).isEqualTo(order);
    }
}
