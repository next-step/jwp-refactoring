package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableLinker;
import kitchenpos.table.exception.IllegalOrderTableException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableLinkerImplementation implements OrderTableLinker {

    private final OrderRepository orderRepository;

    public OrderTableLinkerImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatusByOrderTableId(Long orderTableId) {
        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        if (!orders.isCompletedAllOrders()) {
            throw new IllegalOrderTableException();
        }
    }
}
