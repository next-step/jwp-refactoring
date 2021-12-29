package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.KitchenposException;

class OrderTest {

    @DisplayName("주문 완료 상태에서 업데이트시 에러")
    @Test
    void updateOrderStatus() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);

        Order order = new Order(1L, OrderStatus.COMPLETION,
            new OrderLineItems(Collections.singletonList(orderLineItem)));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> order.updateOrderStatus("COOKING"))
            .withMessage("완료된 주문의 상태를 바꿀 수 없습니다.");
    }
}