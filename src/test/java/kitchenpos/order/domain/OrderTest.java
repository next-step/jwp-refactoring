package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("주문 도메인 테스트")
public class OrderTest {
    @DisplayName("주문 상태 변경")
    @Test
    void 주문_상태_변경() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);
        Order order = Order.of(orderTable);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("완료된 주문에 대한 주문 상태 변경 예외")
    @Test
    void 완료된_주문_상태_변경_예외() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);
        Order order = Order.of(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        Throwable thrown = catchThrowable(() -> order.changeOrderStatus(OrderStatus.COOKING));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }

    @DisplayName("주문 라인 아이템 추가")
    @Test
    void 주문_라인_아이템_추가() {
        OrderTable orderTable = OrderTable.of(5, true);
        Order order = Order.of(orderTable);

        OrderLineItem orderLineItem = OrderLineItem.of(Menu.of("치킨", Price.of(BigDecimal.valueOf(5000)), MenuGroup.of("치킨")), Quantity.of(5));
        order.addOrderLineItems(Collections.singletonList(orderLineItem));

        assertThat(order.getOrderLineItems().size()).isEqualTo(1);
    }

    @DisplayName("비어있는 주문 라인 아이템 추가 예외")
    @Test
    void 빈_주문_라인_아이템_추가() {
        OrderTable orderTable = OrderTable.of(5, true);
        Order order = Order.of(orderTable);

        Throwable thrown = catchThrowable(() -> order.addOrderLineItems(new ArrayList<>()));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 라인은 최소 1개 이상 필요합니다.");
    }
}
