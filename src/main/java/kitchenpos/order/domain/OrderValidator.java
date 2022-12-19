package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityNotFoundException::new);

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderMessage.CREATE_ERROR_ORDER_TABLE_IS_EMPTY.message());
        }
    }
}
