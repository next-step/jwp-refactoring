package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = new Order(orderRequest.getOrderTableId(), OrderLineItemRequest.toOrderLineItems(orderRequest.getOrderLineItems()));
        OrderResponse orderResponse = OrderResponse.of(orderRepository.save(order));
        orderEventPublisher.publishCreateOrderEvent(orderResponse);
        return orderResponse;
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    public boolean isNotCompletedByOrderTableId(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (orders.isEmpty()) {
            return false;
        }
        return orders.stream()
            .allMatch(Order::isCompleted);
    }

    public boolean isNotCompletedByOrderTableIds(List<Long> orderTableIds) {
        List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);
        if (orders.isEmpty()) {
            return false;
        }
        return orders.stream()
            .allMatch(Order::isCompleted);
    }

}
