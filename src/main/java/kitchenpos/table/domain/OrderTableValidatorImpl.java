package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

import kitchenpos.exception.NotExistIdException;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.table.exception.NotEmptyTableException;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Long id) {
        OrderTable orderTable = findOrderTable(id);
        if (!orderTable.isEmpty()) {
            throw new NotEmptyTableException();
        }
    }

    private OrderTable findOrderTable(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(NotExistIdException::new);
    }
}
