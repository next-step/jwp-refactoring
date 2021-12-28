package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.common.OrderErrorCode;
import kitchenpos.exception.InvalidParameterException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

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

    public void validateRegister(Order order) {
        validateExistAndEmptyOrderTable(order.getOrderTableId());
        validateExistMenu(order.getMenuIds());
    }

    private void validateExistAndEmptyOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(
                () -> new NotFoundException(OrderErrorCode.ORDER_TABLE_NOT_FOUND_EXCEPTION));

        if (orderTable.isEmpty()) {
            throw new InvalidParameterException(OrderErrorCode.TABLE_NOT_EMPTY_EXCEPTION);
        }
    }

    private void validateExistMenu(List<Long> menuIds) {
        Long count = menuRepository.countAllByIdIn(menuIds);

        if (count != menuIds.size()) {
            throw new NotFoundException(OrderErrorCode.MENU_NOT_FOUND_EXCEPTION);
        }
    }
}
