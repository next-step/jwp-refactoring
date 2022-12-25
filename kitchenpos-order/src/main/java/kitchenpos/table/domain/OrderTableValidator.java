package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.EntityNotFoundException;
import kitchenpos.common.exception.ErrorMessage;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void unGroupOrderStatusValidate(Long orderTableId) {
        if (!isAllFinished(orderTableId)) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_UNGROUP_WHEN_ORDER_NOT_COMPLETED);
        }
    }

    public void changeEmptyOrderStatusValidate(Long orderTableId) {
        if (!isAllFinished(orderTableId)) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_ORDER_NOT_COMPLETED);
        }
    }

    public void orderTableEmptyValidate(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new EntityNotFoundException(OrderTable.ENTITY_NAME, orderTableId));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_ORDER_WHEN_TABLE_IS_EMPTY);
        }
    }

    private boolean isAllFinished(Long orderTableId) {
        return !orderRepository.existsOrdersByOrderTableIdAndOrderStatusNot(orderTableId, OrderStatus.COMPLETION);
    }
}
