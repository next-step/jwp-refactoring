package kitchenpos.order.dto;

import kitchenpos.order.enums.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 반환 테스트")
class OrdersResponseTest {

    private Orders 주문;
    private List<OrderLineItem> 주문_항목;

    @BeforeEach
    void setUp() {
        주문 = new Orders(1L, 1L, OrderStatus.COOKING, LocalDateTime.now());
        주문_항목 = Arrays.asList(
                new OrderLineItem(1L, 1L, 1),
                new OrderLineItem(2L, 2L, 1)
        );
    }

    @Test
    void 주문_객체를_이용하여_주문_반환_객체_생성() {
        주문.addOrderLineItems(주문_항목);
        OrderResponse orderResponse = OrderResponse.of(주문);
        assertThat(orderResponse.getId()).isEqualTo(주문.getId());
        assertThat(orderResponse.getOrderTableId()).isEqualTo(주문.getOrderTableId());
        assertThat(orderResponse.getOrderedTime()).isEqualTo(주문.getOrderedTime());
        assertThat(orderResponse.getOrderStatus()).isEqualTo(주문.getOrderStatus());
        assertThat(orderResponse.getOrderLineItemResponses().size()).isEqualTo(2);
    }
}
