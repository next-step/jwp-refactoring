package kitchenpos.table.domain;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUngroup(TableGroup tableGroup) {
        List<Long> orderTableIds = mapToOrderTableIds(tableGroup);
        if (hasUncompletedOrder(orderTableIds)) {
            throw new IllegalArgumentException();
        }
    }

    private static List<Long> mapToOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    private boolean hasUncompletedOrder(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, OrderStatus.getNotCompletedStatuses());
    }
}
