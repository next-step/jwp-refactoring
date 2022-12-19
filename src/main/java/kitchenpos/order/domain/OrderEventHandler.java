package kitchenpos.order.domain;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderEventHandler {

    private OrderLineItemRepository orderLineItemRepository;

    public OrderEventHandler(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @EventListener
    public void createOrderLineItems(OrderCreatedEvent event) {
        final List<OrderLineItem> orderLineItemList = new ArrayList<>();
        for(OrderLineItemRequest orderLineItemRequest: event.getOrderLineItemRequests()) {
            final Quantity quantity = new Quantity(orderLineItemRequest.getQuantity());
            final OrderLineItem orderLineItem = new OrderLineItem(event.getOrder(), orderLineItemRequest.getMenuId(),
                    quantity);
            orderLineItemList.add(orderLineItem);
        }
        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        orderLineItemRepository.saveAll(orderLineItemList);
    }

}
