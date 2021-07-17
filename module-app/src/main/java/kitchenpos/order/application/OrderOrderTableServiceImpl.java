package kitchenpos.order.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;

@Service
public class OrderOrderTableServiceImpl implements OrderOrderTableService{
    private final OrderRepository orderRepository;

    public OrderOrderTableServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findOrderByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(orderTableId);
    }
}
