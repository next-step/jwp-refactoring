package kitchenpos.table.application;

import java.util.List;
import kitchenpos.exception.NotCompletionStatusException;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateNotCompletionOrderTables(List<Long> orderTableIds) {
        if (orderRepository.existNotCompletionOrderTables(orderTableIds)) {
            throw new NotCompletionStatusException();
        }
    }

    public void validateNotCompletionOrderTable(Long orderTableId) {
        if (orderRepository.existNotCompletionOrderTable(orderTableId)) {
            throw new NotCompletionStatusException();
        }
    }
}
