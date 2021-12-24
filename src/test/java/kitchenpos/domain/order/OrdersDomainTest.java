package kitchenpos.domain.order;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.vo.MenuId;
import kitchenpos.vo.OrderTableId;

public class OrdersDomainTest {
    @DisplayName("주문 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        OrderTable 주문테이블 = OrderTable.of(3, false);
        OrderLineItems 주문항목 = OrderLineItems.of(List.of(OrderLineItem.of(MenuId.of(1L), 1L)));
        Orders 주문 = Orders.of(OrderTableId.of(주문테이블), OrderStatus.MEAL, 주문항목);

        // when
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        Assertions.assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
