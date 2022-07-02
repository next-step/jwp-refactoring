package kitchenpos.order.application;

import static kitchenpos.Exception.OrderTableAlreadyEmptyException.ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;

import java.util.List;
import kitchenpos.Exception.NotFoundMenuException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderValidator(final MenuService menuService, final OrderTableService orderTableService) {
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    public void validate(Order order) {
        validateOrderTableEmpty(order.getOrderTableId());
        validateNotFoundMenu(order.getOrderLineItems());
    }

    private void validateOrderTableEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableService.findOrderTableById(orderTableId);
        if (orderTable.isEmpty()) {
            throw ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;
        }
    }

    private void validateNotFoundMenu(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getMenuIds();

        if (menuIds.size() != menuService.countByIdIn(menuIds)) {
            throw new NotFoundMenuException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }
}
