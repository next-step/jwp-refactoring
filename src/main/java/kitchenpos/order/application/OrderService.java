package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private static final String ORDER_IS_NOT_EXIST = "주문이 존재하지 않습니다";
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

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAllWithItem();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus status) {
        final Order savedOrder = orderRepository.findByIdWithItem(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_IS_NOT_EXIST));
        savedOrder.changeStatus(status);
        return savedOrder;
    }
}
