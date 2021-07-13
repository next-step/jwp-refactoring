package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.event.OrderTableEmptiedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class OrderTableEmptiedEventHandler {
    private final OrderRepository orderRepository;

    public OrderTableEmptiedEventHandler(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(OrderTableEmptiedEvent event) {
        OrderTable orderTable = event.getOrderTable();

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        if (isCookingAndMealStatus(orders)) {
            orderTable.clearTableGroup();
        }
    }

    private boolean isCookingAndMealStatus(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletionStatus);
    }
}
