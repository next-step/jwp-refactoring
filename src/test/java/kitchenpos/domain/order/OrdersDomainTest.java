package kitchenpos.domain.order;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.order.EmptyOrderLineItemOrderException;
import kitchenpos.exception.order.EmptyOrderTableOrderException;
import kitchenpos.exception.order.NotChangableOrderStatusException;
import kitchenpos.vo.MenuId;

public class OrdersDomainTest {
    @DisplayName("빈테이블로 주문 생성시 시 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);
        OrderLineItems 주문항목 = OrderLineItems.of(List.of(OrderLineItem.of(MenuId.of(1L), 1L)));

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderTableOrderException.class)
                    .isThrownBy(() -> Orders.of(주문테이블, OrderStatus.MEAL, 주문항목));
    }

    @DisplayName("주문항목없이 주문 생성시 시 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderLineItem() {
        // given
        OrderTable 주문테이블 = OrderTable.of(3, false);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderLineItemOrderException.class)
                    .isThrownBy(() -> Orders.of(주문테이블, OrderStatus.MEAL, OrderLineItems.of(List.of())));
    }

    @DisplayName("주문 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        OrderTable 주문테이블 = OrderTable.of(3, false);
        OrderLineItems 주문항목 = OrderLineItems.of(List.of(OrderLineItem.of(MenuId.of(1L), 1L)));
        Orders 주문 = Orders.of(주문테이블, OrderStatus.MEAL, 주문항목);

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
        Orders 주문 = Orders.of(주문테이블, OrderStatus.COMPLETION, 주문항목);

        // when
        // then
        Assertions.assertThatExceptionOfType(NotChangableOrderStatusException.class)
                    .isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL));
    }
}
