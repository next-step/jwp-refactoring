package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.event.CreateOrderEvent;
import kitchenpos.order.exception.IllegalOrderLineItemException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final MenuService menuService;
    private final ApplicationEventPublisher eventPublisher;

    public static final String ERROR_ORDER_LINE_ITEM_TOO_SMALL = "주문항목 개수는 %d 이하일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_INVALID_MENU = "주문항목에 잘못된 메뉴가 포함되어 있습니다.";
    public static final int MINIMUM_ORDER_LINE_ITEM_NUMBER = 0;

    public OrderValidator(MenuService menuService, ApplicationEventPublisher eventPublisher) {
        this.menuService = menuService;
        this.eventPublisher = eventPublisher;
    }

    public void validate(Order order){
        validateOrderLineItems(order);
        eventPublisher.publishEvent(CreateOrderEvent.from(order.getOrderTableId()));
    }

    private void validateOrderLineItems(Order order) {
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validateOrderLineItemsSize(orderLineItems);
        validateOrderLineItemsMenu(orderLineItems);
    }

    private void validateOrderLineItemsSize( List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.size() <= MINIMUM_ORDER_LINE_ITEM_NUMBER) {
            throw new IllegalOrderLineItemException(
                    String.format(ERROR_ORDER_LINE_ITEM_TOO_SMALL, MINIMUM_ORDER_LINE_ITEM_NUMBER)
            );
        }
    }

    private void validateOrderLineItemsMenu(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenuId())
                .collect(Collectors.toList());
        List<Menu> menus = menuService.findMenusByIdList(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new IllegalOrderLineItemException(ERROR_ORDER_LINE_ITEM_INVALID_MENU);
        }
    }
}
