package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class OrderTableService {
    private final OrderRepository orderRepository;

    public OrderTableService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(Long id) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(id, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
