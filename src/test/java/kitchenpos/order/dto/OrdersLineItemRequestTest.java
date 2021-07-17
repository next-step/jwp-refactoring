package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목 요청 테스트")
class OrdersLineItemRequestTest {

    @Test
    void 주문_항목_객체를_이용하여_주문_항목_entity_생성() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
        assertThat(orderLineItem.getMenuId()).isEqualTo(orderLineItemRequest.getMenuId());
        assertThat(orderLineItem.getQuantity()).isEqualTo(orderLineItemRequest.getQuantity());
    }
}
