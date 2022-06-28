package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderValidator;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final Order order) {
        orderValidator.checkOrderLineItems(order);
        orderValidator.checkOrderTable(order);
        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAllWithItem();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus status) {
        final Order savedOrder = orderRepository.findByIdWithItem(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeStatus(status);
        return savedOrder;
    }
}
