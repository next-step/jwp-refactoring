package kitchenpos.order.domain;

import kitchenpos.menu.application.MenuService;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateValidator {

    private final MenuService menuService;
    private final TableService tableService;

    public OrderCreateValidator(MenuService menuService, TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    public void validate(Order order) {
        tableService.checkValidNullAndEmpty(order.getOrderTableId());
        menuService.validMenuCount(order.menuIds());
    }
}
