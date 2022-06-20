package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderFixture {

    public static Order 주문_데이터_생성(Long id, long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> requestOrderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, requestOrderLineItems);
    }

    public static Order 주문_요청_데이터_생성(List<OrderLineItem> requestOrderLineItems) {
        return new Order(null, 1L, null, null, requestOrderLineItems);
    }

    public static Order 주문수정_요청_데이터_생성(String orderStatus) {
        return new Order(null, null, orderStatus, null, null);
    }

    public static void 주문_데이터_확인(Order order, Long id, Long tableId, OrderStatus orderStatus) {
        assertAll(
                () -> assertEquals(id, order.getId()),
                () -> assertEquals(tableId, order.getOrderTableId()),
                () -> assertEquals(orderStatus.name(), order.getOrderStatus()),
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderLineItems()).isNotEmpty()
        );
    }
}
