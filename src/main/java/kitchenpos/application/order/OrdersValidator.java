package kitchenpos.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import kitchenpos.application.menu.MenuService;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.Orders;
import kitchenpos.event.orders.ValidateEmptyTableEvent;
import kitchenpos.exception.order.EmptyOrderLineItemOrderException;
import kitchenpos.exception.order.NotRegistedMenuOrderException;
import kitchenpos.vo.MenuId;

@Component
public class OrdersValidator {
    private final MenuService menuService;
    private final ApplicationEventPublisher eventPublisher;

    public OrdersValidator (
        final MenuService menuService,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.menuService = menuService;
        this.eventPublisher = eventPublisher;
    }

    public void validateForCreate(Orders newOrder) {
        eventPublisher.publishEvent(new ValidateEmptyTableEvent(newOrder.getOrderTableId().value()));
        
        checkEmptyOfOrderLineItems(newOrder.getOrderLineItems());
        checkExistOfMenu(newOrder.getOrderLineItems());
    }

    private void checkExistOfMenu(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                                            .map(OrderLineItem::getMenuId)
                                            .map(MenuId::value)
                                            .collect(Collectors.toList());

        Menus menus = Menus.of(menuService.findAllByIdIn(menuIds));

        if (menus.size() != menuIds.size()) {
            throw new NotRegistedMenuOrderException();
        }
    }

    private static void checkEmptyOfOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemOrderException();
        }
    }
}
