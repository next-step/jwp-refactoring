package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.event.OrderCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher publisher;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order persistOrder = orderRepository.save(new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING));
        publisher.publishEvent(new OrderCreatedEvent(persistOrder, orderRequest.toOrderLineItems()));
        return OrderResponse.of(persistOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderResponse(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest order) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.updateOrderStatus(order.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
    }
}
