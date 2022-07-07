package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    @Test
    @DisplayName("주문 상품 추가")
    void addOrderLineItem() {
        // given
        final Order order = Order.of(1L, Arrays.asList());
        // when
        order.addOrderLineItem(OrderLineItem.of(Menu.of("햄버거", BigDecimal.valueOf(1_000), MenuGroup.of("햄버거메뉴")), 5));
        //then
        assertThat(order.getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 업데이트")
    void updateOrderStatus() {
        // given
        final Order order = Order.of(1L, Arrays.asList());
        // when
        order.updateOrderStatus(OrderStatus.COMPLETION);
        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
