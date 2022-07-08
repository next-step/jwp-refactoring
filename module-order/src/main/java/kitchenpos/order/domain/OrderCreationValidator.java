package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.CannotMakeOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotExistTableException;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderCreationValidator(MenuRepository menuRepository,
                                  OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateAllMenusExist(OrderLineItems orderLineItems) {
        List<Long> orderItemMenuIds = orderLineItems.menuIds();
        if (orderItemMenuIds.size() == menuRepository.countByIdIn(orderItemMenuIds)) {
            return;
        }
        throw new CannotMakeOrderException(CannotMakeOrderException.NOT_EXIST_MENU);
    }

    public void validateTableToMakeOrder(Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new CannotMakeOrderException(CannotMakeOrderException.TABLE_IS_EMPTY);
        }
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NotExistTableException::new);
    }

}
