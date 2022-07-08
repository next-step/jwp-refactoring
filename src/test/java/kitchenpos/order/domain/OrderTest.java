package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문")
class OrderTest {
    @DisplayName("계산 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void 계산_완료된_주문_상태변경_불가() {
        Long 메뉴_아이디 = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_아이디, 1L);
        OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));
        Order 계산_완료된_주문 = new Order.Builder()
                .orderTableId(1L)
                .orderStatus(OrderStatus.COMPLETION)
                .orderedTime(LocalDateTime.now())
                .orderLineItems(주문_항목들).build();

        assertThatThrownBy(() -> 계산_완료된_주문.updateOrderStatus(OrderStatus.MEAL.name())).isInstanceOf(
                IllegalArgumentException.class);
    }
}
