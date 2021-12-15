package kitchenpos.domain.order.application;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateEmptyTable(Long orderTableId) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.validateEmptyTable();
    }

    public void validateMenus(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)) {
            throw new IllegalArgumentException();
        }

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
