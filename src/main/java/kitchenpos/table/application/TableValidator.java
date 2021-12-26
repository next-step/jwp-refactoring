package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.TableEmptyUpdateException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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

    private boolean usingTable(OrderTable orderTable) {
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

//    public void ungroup() {
//        if (orders.checkOccupied()) {
//            throw new NotSupportUngroupException();
//        }
//        //tableGroup = null;
//    }