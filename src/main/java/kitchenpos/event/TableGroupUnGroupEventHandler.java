package kitchenpos.event;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.order.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TableGroupUnGroupEventHandler {

    private final OrderRepository orderRepository;

    public TableGroupUnGroupEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @EventListener
    public void unGroupOrderTables(TableGroupUnGroupEvent event) {

        List<OrderTable> orderTables = event.getOrderTables();

        orderTables.forEach(orderTable -> {
            List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            orders.forEach(Order::checkOrderStatusCookingOrMeal);
            orderTable.unGroup();
        });
    }

}
