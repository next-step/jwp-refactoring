package kitchenpos.order.application;

import kitchenpos.menu.application.exception.MenuNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final TableRepository tableRepository;

    public OrderValidator(final MenuRepository menuRepository, final TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
    }

    public void validate(Order order) {
        validateMenus(order.getMenuIds());
        OrderTable orderTable = getOrderTable(order.getTableId());
        validateTableState(orderTable);
    }

    private void validateMenus(List<Long> menuIds) {
        menuIds.forEach(this::getMenu);
    }

    private void validateTableState(OrderTable orderTable) {
        TableState tableState = orderTable.getTableState();

        if (tableState.isEmpty()) {
            throw new InvalidTableState("빈 테이블에 주문을 받을 수 없습니다.");
        }
    }

    public void validateOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.isCompleted()) {
            throw new InvalidOrderState("이미 계산을 완료하였습니다.");
        }
    }

    private Menu getMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(MenuNotFoundException::new);
    }

    private OrderTable getOrderTable(Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(TableNotFoundException::new);
    }
}
