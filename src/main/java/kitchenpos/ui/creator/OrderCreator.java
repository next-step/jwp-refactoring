package kitchenpos.ui.creator;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCreator {
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemCreator orderLineItemCreator;

    public OrderCreator(OrderTableRepository orderTableRepository,
                        OrderLineItemCreator orderLineItemCreator) {
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemCreator = orderLineItemCreator;
    }

    private OrderTable setOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = null;
        Long orderTableId = orderRequest.getOrderTableId();
        if (orderTableId != null) {
            orderTable = orderTableRepository.findById(orderTableId).orElse(null);
        }
        return orderTable;
    }

    private OrderStatus setOrderStatus(OrderRequest orderRequest) {
        OrderStatus orderStatus = null;
        String orderStatusName = orderRequest.getOrderStatus();
        if (orderStatusName != null) {
            orderStatus = OrderStatus.valueOf(orderStatusName);
        }
        return orderStatus;
    }

    private OrderLineItems setOrderLineItems(OrderRequest orderRequest) {
        OrderLineItems orderLineItems = null;
        List<OrderLineItemRequest> lineItemRequests = orderRequest.getOrderLineItems();

        if (lineItemRequests != null) {
            List<OrderLineItem> collect = lineItemRequests.stream()
                    .map(orderLineItemCreator::toOrderLineItem)
                    .collect(Collectors.toList());
            orderLineItems = new OrderLineItems(collect);
        }
        return orderLineItems;
    }

    public Order toOrder(OrderRequest orderRequest) {
        OrderTable orderTable = setOrderTable(orderRequest);
        OrderStatus orderStatus = setOrderStatus(orderRequest);
        OrderLineItems orderLineItems = setOrderLineItems(orderRequest);
        return Order.builder().id(orderRequest.getId())
                .orderTable(orderTable)
                .orderStatus(orderStatus)
                .orderedTime(orderRequest.getOrderedTime())
                .orderLineItems(orderLineItems)
                .build();
    }
}
