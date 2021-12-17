package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateService {

    private final OrderRepository orderRepository;
    private final OrderValidator validator;
    private final OrderTableRepository orderTableRepository;

    public OrderCreateService(OrderRepository orderRepository,
        OrderValidator validator, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.validator = validator;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = orderRepository.save(newOrder(orderTableId, orderLineItems));

        orderTableRepository.table(order.tableId())
            .ordered();
        return order;
    }

    private Order newOrder(long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems)
            .get(validator);
    }
}
