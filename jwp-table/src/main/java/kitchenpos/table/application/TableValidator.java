package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.EmptyOrderException;
import kitchenpos.table.exception.OrderStatusNotProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class TableValidator {

    private final OrderService orderService;

    public TableValidator(final OrderService orderService) {
        this.orderService = orderService;
    }

    public void validateUnGroup(final List<OrderTable> orderTables) {
        final List<Order> orders = orderService.findByOrderTableIdIn(listOfOrderTableIds(orderTables));
        if (CollectionUtils.isEmpty(orders)) {
            throw new EmptyOrderException();
        }
        final List<Order> checkOrders = orders.stream()
                .filter(Order::existsCookingOrMeal)
                .collect(Collectors.toList());
        if (orders.containsAll(checkOrders)) {
            throw new OrderStatusNotProcessingException();
        }
    }

    public void validateEmpty(final Long orderTableId) {
        final Order order = orderService.findByOrderTableId(orderTableId);
        if (Objects.isNull(order)) {
            throw new EmptyOrderException();
        }
        if (order.existsCookingOrMeal()) {
            throw new OrderStatusNotProcessingException();
        }
    }

    public void validateGroup(final List<OrderTable> orderTables) {
        final List<Order> orders = orderService.findByOrderTableIdIn(listOfOrderTableIds(orderTables));
        if (CollectionUtils.isEmpty(orders)) {
            throw new EmptyOrderException();
        }
    }

    private List<Long> listOfOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
