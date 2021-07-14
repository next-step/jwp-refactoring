package kitchenpos.order.event.order;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static kitchenpos.order.event.order.OrderLineItemValidator.validOrderLIneItemCount;
import static kitchenpos.order.event.order.OrderLineItemValidator.validOrderLineItemEmpty;

@Component
public class OrderLineItemsEventHandler {
    private final MenuRepository menuRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemsEventHandler(MenuRepository menuRepository, OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Async
    @EventListener
    public void savedOrderLineItems(OrderCreatedEvent orderCreatedEvent) {
        List<OrderLineItem> orderLineItems = orderCreatedEvent.getOrderLineItems();
        validOrderLineItemEmpty(orderLineItems);
        List<Menu> menus = findMenuAllById(orderLineItems);
        validOrderLIneItemCount(orderLineItems, menus.size());
        orderLineItemRepository.saveAll(orderLineItems);
    }

    private List<Menu> findMenuAllById(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        return menuRepository.findAllById(menuIds);
    }
}
