package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.exception.KitchenposException;

class OrderTest {

    @DisplayName("주문 완료 상태에서 업데이트시 에러")
    @Test
    void updateOrderStatus() {
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);

        Order order = new Order(1L, new OrderTable(1L), OrderStatus.COMPLETION, LocalDateTime.now(),
            new OrderLineItems(Collections.singletonList(orderLineItem)));

        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> order.updateOrderStatus(OrderStatus.COOKING))
            .withMessage("완료된 주문의 상태를 바꿀 수 없습니다.");
    }
}