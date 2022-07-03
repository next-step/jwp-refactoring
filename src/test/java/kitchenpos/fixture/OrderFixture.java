package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixture {

    public static Order 주문_생성(Long id, Long orderTableId, OrderStatus orderStatus,
                              List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public static OrderRequest 주문요청_생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(OrderLineItemRequest::of)
                .collect(Collectors.toList());
        return new OrderRequest(orderTableId, orderLineItemRequests, null);
    }

    public static OrderLineItem 주문항목_생성(Long seq, Long menuId, Long quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }
}
