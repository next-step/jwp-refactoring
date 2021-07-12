package kitchenpos.ordering.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Ordering order) {
        order.validateOrderLineItemsSize(getMenuIdsCountOf(order));

        validateOrderTable(order.getOrderTableId());
    }

    private long getMenuIdsCountOf(Ordering order) {
        return menuRepository.countByIdIn(order.menuIds());
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.isValidForOrdering();
    }
}
