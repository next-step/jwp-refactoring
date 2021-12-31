package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final MenuService menuService;
    private final TableService tableService;

    public OrderValidator(MenuService menuService, TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public Long findOrderTableById(final OrderRequest request) {
        final OrderTable orderTable = tableService.findOrderTableById(request.getOrderTableId());
        return orderTable.getId();
    }

    public Long findMenuById(final OrderLineItemRequest request) {
        final Menu menu = menuService.getMenuById(request.getMenuId());
        return menu.getId();
    }
}
