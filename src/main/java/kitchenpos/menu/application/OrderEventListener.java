package kitchenpos.menu.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderGeneratedEvent;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class OrderEventListener {
    private static final String ITEM_SIZE_NOT_MATCH = "주문 항목이 일치하지 않습니다.";
    private final MenuService menuService;

    public OrderEventListener(MenuService menuService) {
        this.menuService = menuService;
    }

    @EventListener
    public void generateOrder(OrderGeneratedEvent orderGeneratedEvent) {
        Order order = orderGeneratedEvent.getOrder();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        List<Long> menuIds = getMenuIds(orderLineItems);
        int menuExistedSize = menuService.getMenuExistCount(menuIds);
        int orderLineItemsSize = orderLineItems.size();

        if (menuExistedSize != orderLineItemsSize) {
            throw new MenuNotMatchException(ITEM_SIZE_NOT_MATCH);
        }
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList());
    }
}
