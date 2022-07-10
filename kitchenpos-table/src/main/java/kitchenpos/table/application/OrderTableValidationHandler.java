package kitchenpos.table.application;

import javax.persistence.EntityNotFoundException;
import kitchenpos.common.exception.InvalidValueException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidationHandler {
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidationHandler(
        OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    public void validateOrder(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(EntityNotFoundException::new);

        if(orderTable.isEmpty()) {
            throw new InvalidValueException();
        }
    }
}
