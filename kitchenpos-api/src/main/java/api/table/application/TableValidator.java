package api.table.application;

import api.exception.table.TableEmptyUpdateException;
import api.order.domain.Order;
import api.order.domain.OrderRepository;
import api.table.domain.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName : kitchenpos.table.application
 * fileName : TableValidator
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (orderTable.isGrouped() || usingTable(orderTable)) {
            throw new TableEmptyUpdateException();
        }
    }

    public boolean usingTable(OrderTable orderTable) {
        List<Order> orders = getOrders(orderTable);
        return !orders.isEmpty() && !orderCompleted(orders);
    }

    public List<Order> getOrders(OrderTable orderTable) {
        return orderRepository.findByOrderTableId(orderTable.getId());
    }

    public boolean orderCompleted(List<Order> orders) {
        return orders.stream()
                .allMatch(Order::isCompleted);
    }
}
