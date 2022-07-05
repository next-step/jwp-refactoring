package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문")
class OrderTest {
    @DisplayName("계산 완료된 주문은 상태를 변경할 수 없다.")
    @Test
    void 계산_완료된_주문_상태변경_불가() {
        Menu 메뉴 = new Menu(1L, "무료 메뉴", BigDecimal.ZERO, new MenuGroup("무료 그룹"));
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴, 1L);
        OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));
        Order 계산_완료된_주문 = new Order.Builder()
                .orderStatus(OrderStatus.COMPLETION)
                .orderTable(OrderTable.of(5, false))
                .orderLineItems(주문_항목들)
                .build();

        assertThatThrownBy(() -> 계산_완료된_주문.updateOrderStatus(OrderStatus.MEAL.name())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 없는 주문은 생성할 수 없다.")
    @Test
    void 빈_주문_생성불가() {
        assertThatThrownBy(
                () -> new Order.Builder()
                        .orderStatus(OrderStatus.COMPLETION)
                        .orderTable(OrderTable.of(5, false))
                        .orderLineItems(OrderLineItems.create()).build()).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 주문을 생성할 수 없다.")
    @Test
    void 빈_테이블_주문_생성불가() {
        Menu 메뉴 = new Menu(1L, "무료 메뉴", BigDecimal.ZERO, new MenuGroup("무료 그룹"));
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴, 1L);
        OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));
        assertThatThrownBy(
                () -> new Order.Builder()
                .orderStatus(OrderStatus.COMPLETION)
                .orderLineItems(주문_항목들)
                .build()).isInstanceOf(IllegalArgumentException.class);

    }
}
