package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyOrderException;
import kitchenpos.common.exceptions.OrderStatusNotProcessingException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateUnGroup(final List<OrderTable> orderTables) {
        final List<Order> orders = orderRepository.findByOrderTableIdIn(orderTables);
        if (CollectionUtils.isEmpty(orders)) {
            throw new EmptyOrderException();
        }
        final List<Order> checkOrders = orders.stream()
                .filter(Order::existsOrderStatus)
                .collect(Collectors.toList());
        if (orders.containsAll(checkOrders)) {
            throw new OrderStatusNotProcessingException();
        }
    }

    public void validateEmpty(final Long orderTableId) {
        final Order order = orderRepository.findByOrderTableId(orderTableId);
        if (Objects.isNull(order)) {
            throw new EmptyOrderException();
        }
        if (order.existsOrderStatus()) {
            throw new OrderStatusNotProcessingException();
        }
    }
}
