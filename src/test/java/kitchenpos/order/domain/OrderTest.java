package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.tablegroup.domain.TableGroup;
import table.domain.OrderTable;

class OrderTest {

    @DisplayName("주문 완료 상태에서 업데이트시 에러")
    @Test
    void updateOrderStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(new Menu(), 1);
        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L), 4, false);

        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now(),
            new OrderLineItems(Collections.singletonList(orderLineItem)));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> order.updateOrderStatus(OrderStatus.COOKING))
            .withMessage("완료된 주문의 상태를 바꿀 수 없습니다.");
    }
}