package kitchenpos.ordertable.domain;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.exception.EmptyOrderTableException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void checkOrderTableIsNotEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ORDER_TABLE_NOT_FOUND));

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException(ExceptionMessage.EMPTY_ORDER_TABLE);
        }
    }

}
