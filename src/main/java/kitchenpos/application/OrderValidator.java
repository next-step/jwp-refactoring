package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyMenuException;
import kitchenpos.common.exceptions.EmptyOrderTableException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

    private final MenuService menuService;
    private final TableService tableService;

    public OrderValidator(final MenuService menuService, final TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public void validatorTableService(final OrderRequest request) {
        final OrderTable orderTable = tableService.findOrderTableById(request.getOrderTableId());
        if (Objects.isNull(orderTable)) {
            throw new EmptyOrderTableException();
        }
    }

    public void validatorMenu(final OrderLineItemRequest request) {
        final Menu menu = menuService.getMenuById(request.getMenuId());
        if (Objects.isNull(menu)) {
            throw new EmptyMenuException();
        }
    }
}
