package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderGeneratedEvent;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class OrderEventHandler {
    private static final String ITEM_SIZE_NOT_MATCH = "주문 항목이 일치하지 않습니다.";
    private static final String NOT_EXIST_ORDER_LINE_ITEMS = "주문이 존재하지 않습니다.";
    private final MenuRepository menuRepository;

    public OrderEventHandler(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @EventListener
    public void generateOrder(OrderGeneratedEvent orderGeneratedEvent) {
        Order order = orderGeneratedEvent.getOrder();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        List<Long> menuIds = getMenuIds(orderLineItems);
        int menuExistedSize = menuRepository.countByIdIn(menuIds);
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
