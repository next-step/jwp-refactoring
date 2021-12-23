package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTableValidateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MenuRepository menuRepository;

    public OrderValidator(ApplicationEventPublisher applicationEventPublisher, MenuRepository menuRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.menuRepository = menuRepository;
    }

    public void validateCreateOrder(Order order) {
        checkUsableOrderTable(order.getOrderTableId());
        validateOrderItemRequests(order.getOrderLineItems());
    }

    private void checkUsableOrderTable(Long orderTableId) {
        applicationEventPublisher.publishEvent(OrderTableValidateEvent.of(orderTableId));
    }

    private void validateOrderItemRequests(List<OrderLineItem> orderLineItems) {
        List<Menu> menus = getMenus(orderLineItems);

        orderLineItems.forEach(checkExistMenuId(menus));
    }

    private List<Menu> getMenus(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        return menuRepository.findAllById(menuIds);
    }

    private Consumer<OrderLineItem> checkExistMenuId(List<Menu> menus) {
        return orderLineItem -> menus.stream()
                .filter(menu -> orderLineItem.isEqualMenuId(menu.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
