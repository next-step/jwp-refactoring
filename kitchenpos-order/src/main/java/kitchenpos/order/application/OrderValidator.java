package kitchenpos.order.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {
    private static final int MINIMUM_ORDER_MENU_SIZE = 1;

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateCreateOrder(Long orderTableId, List<Long> menuIds) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        List<Menu> menus = findAllMenuById(menuIds);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
        }

        if (menus.size() < MINIMUM_ORDER_MENU_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage()));
    }

    private List<Menu> findAllMenuById(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if(menuIds.size() != menus.size()) {
            throw new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND_BY_ID.getMessage());
        }

        return menus;
    }
}
