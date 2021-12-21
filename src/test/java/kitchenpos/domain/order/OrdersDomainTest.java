package kitchenpos.domain.order;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.order.NotChangableOrderStatusException;
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

    @DisplayName("계산완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_() {
        // given
        OrderTable 주문테이블 = OrderTable.of(3, false);
        OrderLineItems 주문항목 = OrderLineItems.of(List.of(OrderLineItem.of(MenuId.of(1L), 1L)));
        Orders 주문 = Orders.of(OrderTableId.of(주문테이블), OrderStatus.COMPLETION, 주문항목);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotChangableOrderStatusException.class)
                    .isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL));
    }
}
