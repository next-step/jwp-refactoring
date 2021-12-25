package kitchenpos.ordertable.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        orderTable.validateChangeableEmpty();
        validateNotCompletionOrderStatus(orderTable.getId());
    }

    private void validateNotCompletionOrderStatus(Long orderTableId) {
        if (existsNotCompletionStatusByTableId(orderTableId)) {
            throw new BadRequestException(CANNOT_CHANGE_STATUS);
        }
    }

    private boolean existsNotCompletionStatusByTableId(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
