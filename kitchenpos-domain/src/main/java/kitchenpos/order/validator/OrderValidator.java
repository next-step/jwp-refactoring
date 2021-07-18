package kitchenpos.order.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.NotEqualsOrderCountAndMenuCount;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrder(Order order) {
        OrderTable orderTable = getOrderTable(order.getOrderTableId());
        if (isEmptyOrderTable(orderTable)) {
            throw new EmptyOrderTableException();
        }
        OrderLineItems orderLineItems = order.getOrderLineItems();
        validateOrderLineItems(orderLineItems);
        orderTable.registerOrder(order.getId());
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems.getOrderLineItems().isEmpty()) {
            throw new EmptyOrderLineItemsException();
        }
        List<Long> menuIds = orderLineItems.getMenuIds();
        if (!isEqualsMenuCount(orderLineItems, getMenus(menuIds))) {
            throw new NotEqualsOrderCountAndMenuCount();
        }
    }

    private boolean isEqualsMenuCount(OrderLineItems orderLineItems, List<Menu> menus) {
        if (menus.isEmpty()) {
            throw new NotFoundMenuException();
        }
        return orderLineItems.isEqualsMenuCount(menus.size());
    }

    private boolean isEmptyOrderTable(OrderTable orderTable) {
        return orderTable.isEmpty();
    }

    private List<Menu> getMenus(List<Long> menuIds) {
        return menuRepository.findByIdIn(menuIds);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundOrderTableException());
        return orderTable;
    }
}
