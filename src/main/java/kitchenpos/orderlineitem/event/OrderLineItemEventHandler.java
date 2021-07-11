package kitchenpos.orderlineitem.event;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.event.OrderCreatedEvent;
import kitchenpos.orderlineitem.domain.OrderLineItem;
import kitchenpos.orderlineitem.domain.OrderLineItemRepository;
import kitchenpos.orderlineitem.exception.EmptyOrderLineItemsException;
import kitchenpos.orderlineitem.exception.MenuAndOrderLineItemSizeNotMatchException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderLineItemEventHandler {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderLineItemEventHandler(OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void saveOrderLineItem(OrderCreatedEvent orderCreatedEvent) {
        List<OrderLineItem> orderLineItems = orderCreatedEvent.getOrderLineItemRequests()
                .stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()
                )).collect(Collectors.toList());

        validateOrderLineItemsEmpty(orderLineItems);

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        validateSizeAndMenuCountDifferent(orderLineItems, menuRepository.countByIdIn(menuIds));

        orderLineItemRepository.saveAll(orderLineItems);
    }

    private void validateOrderLineItemsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException();
        }
    }

    private void validateSizeAndMenuCountDifferent(List<OrderLineItem> orderLineItems, long menuCount) {
        if (isSizeDifferentFromMenuCount(orderLineItems, menuCount)) {
            throw new MenuAndOrderLineItemSizeNotMatchException();
        }
    }

    private boolean isSizeDifferentFromMenuCount(List<OrderLineItem> orderLineItems, long menuCount) {
        return orderLineItems.size() != menuCount;
    }
}
