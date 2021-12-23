package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public void validateCreateOrder(Order order) {
        checkUsableOrderTable(order.getOrderTableId());
        validateOrderItemRequests(order.getOrderLineItems());
    }

    private void checkUsableOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
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
