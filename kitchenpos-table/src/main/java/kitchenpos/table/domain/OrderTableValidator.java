package kitchenpos.table.domain;

import kitchenpos.table.message.OrderTableMessage;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;

@Component
public class OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTableIsEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityExistsException::new);
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderTableMessage.ERROR_TABLE_GROUP_IS_EMPTY.message());
        }
    }
}
