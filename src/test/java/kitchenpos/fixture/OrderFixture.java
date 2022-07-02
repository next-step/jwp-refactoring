package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderStatus orderStatus,
                              List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static OrderRequest 주문요청_생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(OrderLineItemRequest::of)
                .collect(Collectors.toList());
        return new OrderRequest(orderTableId, orderLineItemRequests, null);
    }

    public static OrderLineItem 주문항목_생성(Long seq, Menu menu, Long quantity) {
        return new OrderLineItem(seq, menu, quantity);
    }
}
