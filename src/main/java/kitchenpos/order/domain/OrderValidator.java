package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.orderconstants.OrderErrorMessages;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateBeforeCreateOrder(Order order) {
        OrderLineItems orderLineItems = order.getOrderLineItems();
        orderLineItems.checkOrderLineItemEmpty();
        checkMenuExist(orderLineItems);
        checkOrderTableExist(order);
    }

    private void checkMenuExist(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getMenuIds();
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderTableExist(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException(OrderErrorMessages.ORDER_TABLE_DOES_NOT_EXIST));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderErrorMessages.CANNOT_CREATE_ORDER_WHEN_ORDER_TABLE_EMPTY);
        }
    }
}
