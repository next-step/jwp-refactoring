package kitchenpos.table.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public class OrderTableChangedEmptyHandler {
    private final OrderRepository orderRepository;

    public OrderTableChangedEmptyHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    public void changedEmpty(OrderTableChangedEmptyEvent event) {
        Long orderTableId = event.getOrderTableId();
        List<Order> orders = findOrder(orderTableId);
        new OrderTableChangedEmptyValidator(orders);
    }

    private List<Order> findOrder(final Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }
}
