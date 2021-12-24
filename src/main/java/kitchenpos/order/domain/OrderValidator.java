package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
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

    public void registerValidate(Order order) {
        existAndEmptyOrderTableValidate(order.getOrderTableId());
        existMenuValidate(order.getMenuIds());
    }

    private void existAndEmptyOrderTableValidate(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(
                () -> new NotFoundException(CommonErrorCode.ORDER_TABLE_NOT_FOUND_EXCEPTION));

        if (orderTable.isEmpty()) {
            throw new InvalidParameterException(CommonErrorCode.TABLE_NOT_EMPTY_EXCEPTION);
        }
    }

    private void existMenuValidate(List<Long> menuIds) {
        Long count = menuRepository.countAllByIdIn(menuIds);

        if (count != menuIds.size()) {
            throw new NotFoundException(CommonErrorCode.MENU_NOT_FOUND_EXCEPTION);
        }
    }
}
