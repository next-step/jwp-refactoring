package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validOrdersCompletionByOrderTableId(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validOrderIfNotCompletion(orders);
    }

    public void validOrdersCompletionByTableGroup(TableGroup tableGroup) {
        List<Long> orderTableIds = mapToOrderTableIds(tableGroup);
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        validOrderIfNotCompletion(orders);
    }

    private static void validOrderIfNotCompletion(List<Order> orders) {
        orders.forEach(Order::validIfNotCompletion);
    }

    private static List<Long> mapToOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                .getUnmodifiableOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
