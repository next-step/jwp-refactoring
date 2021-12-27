package kitchenpos.validator;

import kitchenpos.common.exception.NotFoundMenuException;
import kitchenpos.common.exception.NotFoundOrderTableException;
import kitchenpos.common.exception.NotOrderedEmptyTableException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidator {
    private final MenuService menuService;
    private final TableService tableService;

    public OrderValidator(
            final MenuService menuService,
            final TableService tableService
    ) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public void validate(Order order) {
        validateEmptyTableByTableId(order.getOrderTableId());
        validateOrderLineItemByMenuIds(order.getOrderItemMenuIds());
    }

    public void validateEmptyTableByTableId(Long tableId) {
        if (!tableService.isExistsById(tableId)) {
            throw new NotFoundOrderTableException();
        }

        if (tableService.isExistsByIdAndEmptyTrue(tableId)) {
            throw new NotOrderedEmptyTableException();
        }
    }

    public void validateOrderLineItemByMenuIds(Set<Long> menuIds) {
        if (menuService.isNotExistsAllByIdIn(menuIds)) {
            throw new NotFoundMenuException();
        }
    }
}
