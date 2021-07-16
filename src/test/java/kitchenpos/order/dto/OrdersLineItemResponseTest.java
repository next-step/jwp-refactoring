package kitchenpos.order.dto;

import kitchenpos.order.enums.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목 반환 테스트")
class OrdersLineItemResponseTest {

    @Test
    void 주문_항목_entity를_이용하여_주문_항목_반환_객체_생성() {
        Orders orders = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        OrderLineItem orderLineItem = new OrderLineItem(1L, orders, 1L, 1);
        OrderLineItemResponse orderLineItemResponse = OrderLineItemResponse.of(orderLineItem);
        assertThat(orderLineItemResponse.getSeq()).isEqualTo(orderLineItem.getSeq());
        assertThat(orderLineItemResponse.getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(orderLineItemResponse.getQuantity()).isEqualTo(orderLineItem.getQuantity());
        assertThat(orderLineItemResponse.getOrderId()).isEqualTo(orders.getId());
    }
}
