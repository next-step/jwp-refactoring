package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.NoSuchOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderRequest.toOrder();
        orderValidator.validate(order);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.asListFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeStatus(orderRequest.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchOrderException(orderId));
    }
}
