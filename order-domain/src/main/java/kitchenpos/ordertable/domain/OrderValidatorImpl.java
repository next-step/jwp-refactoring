package kitchenpos.ordertable.domain;

import kitchenpos.common.domain.EntityNotFoundException;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Long orderTableId) {
        validate(findOrderTableById(orderTableId));
    }

    void validate(OrderTable orderTable) {
        orderTable.validateOrderTableChangable();
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException("OrderTable"));
    }
}
