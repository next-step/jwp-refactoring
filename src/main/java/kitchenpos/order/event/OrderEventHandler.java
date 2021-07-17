package kitchenpos.order.event;

import kitchenpos.exception.OrderException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventHandler {

    private static final String NOT_EQUAL_MENU_COUNT_ERROR_MESSAGE = "존재하지않는 메뉴가 주문 항목에 포함되어 있습니다.";
    private static final String ORDER_TABLE_EMPTY_ERROR_MESSAGE = "비어있는 테이블의 주문은 생성할 수 없습니다.";

    private final MenuService menuService;
    private final TableService tableService;

    public OrderEventHandler(MenuService menuService, TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @EventListener
    public void createOrderValidEvent(OrderValidEvent orderValidEvent) {
        validateMenu(orderValidEvent.getMenuIds());
        validateOrderTable(orderValidEvent.getOrderTableId());
    }

    private void validateMenu(List<Long> menuIds) {
        long menuCount = menuService.countByMenuId(menuIds);
        if (menuIds.size() != menuCount) {
            throw new OrderException(NOT_EQUAL_MENU_COUNT_ERROR_MESSAGE);
        }
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = tableService.findOrderTable(orderTableId);
        if (orderTable.isEmpty()) {
            throw new OrderException(ORDER_TABLE_EMPTY_ERROR_MESSAGE);
        }
    }
}
