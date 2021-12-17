package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusChangeService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderStatusChangeService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void change(long id, OrderStatus status) {
        Order order = orderRepository.order(id);
        order.changeStatus(status);

        if (order.isCompleted()) {
            orderTableRepository.table(order.tableId())
                .finish();
        }
    }
}
