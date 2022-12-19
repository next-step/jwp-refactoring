package kitchenpos.table.domain;

import kitchenpos.ExceptionMessage;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    private final OrderTableRepository orderTableRepository;

    public TableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable getTableById(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_TABLE.getMessage());
        }

        return orderTable;
    }
}
