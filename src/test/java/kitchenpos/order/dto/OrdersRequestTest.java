package kitchenpos.order.dto;

import kitchenpos.order.enums.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 요청 테스트")
class OrdersRequestTest {

    private OrderRequest 주문_요청;

    @BeforeEach
    void setUp() {
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(1L, 1),
                new OrderLineItemRequest(2L, 1)
        );
        주문_요청 = new OrderRequest(1L, null, null, orderLineItemRequests);
    }

    @Test
    void 주문_항목_아이디_값_리스트_생성() {
        List<Long> menuIds = 주문_요청.toMenuIds();
        assertThat(menuIds.size()).isEqualTo(2);
        assertThat(menuIds).containsExactly(1L, 2L);
    }

    @Test
    void 주문_항목_객체를_이용하여_신규_주문_entity_생성() {
        Orders newOrders = 주문_요청.createNewOrder();
        assertThat(newOrders.getOrderTableId()).isEqualTo(주문_요청.getOrderTableId());
        assertThat(newOrders.getOrderedTime()).isNotNull();
        assertThat(newOrders.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void 주문_항목_객체를_이용하여_주문_항목_entity_생성() {
        List<OrderLineItem> orderLineItems = 주문_요청.toOrderLineItems();
        assertThat(orderLineItems.size()).isEqualTo(2);
        assertThat(orderLineItems.get(0).getMenuId()).isEqualTo(1L);
        assertThat(orderLineItems.get(1).getMenuId()).isEqualTo(2L);
        assertThat(orderLineItems.get(0).getQuantity()).isEqualTo(1);
        assertThat(orderLineItems.get(1).getQuantity()).isEqualTo(1);
    }
}
