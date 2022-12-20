package kitchenpos.table.domain;

import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {
    private static final String ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT = "존재하지 않는 주문 테이블입니다. ID : %d";
    private static final String ERROR_MESSAGE_IS_EMPTY_ORDER_TABLE = "비어있는 테이블입니다.";

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Order order) {
        OrderTable orderTable = validateEmptyOrderTable(order.orderTableId());
        validateIsNotEmpty(orderTable);
    }

    private OrderTable validateEmptyOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ERROR_MESSAGE_NOT_FOUND_ORDER_TABLE_FORMAT, orderTableId)));
    }

    private void validateIsNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidParameterException(ERROR_MESSAGE_IS_EMPTY_ORDER_TABLE);
        }
    }
}
