package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;

@Service
public class OrderTableGroupServiceImpl implements OrderTableGroupService{
    private final OrderRepository orderRepository;

    public OrderTableGroupServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findOrdersByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.findByOrderTableIdIn(orderTableIds);
    }
}
