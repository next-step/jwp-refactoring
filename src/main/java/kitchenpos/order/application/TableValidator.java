package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompletion(OrderTable orderTable) {
        if (!orderRepository.existsAllByOrderTableIdInAndOrderStatus(Arrays.asList(orderTable.getId()),
                OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
    }
    public void validateCompletion(List<OrderTable> orderTables){
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (!orderRepository.existsAllByOrderTableIdInAndOrderStatus(orderTableIds,
                OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
    }
}
