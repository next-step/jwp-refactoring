package kitchenpos.order.event;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.dto.OrderLineItemRequest;

@Component
public class OrderLineItemEventHandler {
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemEventHandler(final OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Async
    @EventListener
    public void saveOrderLineItems(final OrderLineItemCreateEvent event) {
        final List<OrderLineItem> orderLineItems = toOrderLineItems(event.getOrder(), event.getOrderLineItems());
        orderLineItemRepository.saveAll(orderLineItems);
    }

    private List<OrderLineItem> toOrderLineItems(final Order order,
        final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(it -> new OrderLineItem(order, it.getMenuId(), it.getQuantity()))
            .collect(Collectors.toList());
    }
}
