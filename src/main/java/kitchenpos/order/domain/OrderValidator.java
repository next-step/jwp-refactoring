package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.menu.domain.MenuRepository;
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
        checkOrderLineItemEmpty(orderLineItems);
        checkMenuExist(orderLineItems);
        checkOrderTableExist(order);
    }

    private void checkOrderLineItemEmpty(OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.ORDER_LINE_ITEM_EMPTY);
        }
    }

    private void checkMenuExist(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getMenuIds();
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkOrderTableExist(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void checkOrderStatusChangeAble(Order savedOrder) {
        if (savedOrder.isOrderStatusComplete()) {
            throw new IllegalArgumentException(ErrorMessages.CANNOT_CHANGE_STATUS_OF_COMPLETED_ORDER);
        }
    }
}
