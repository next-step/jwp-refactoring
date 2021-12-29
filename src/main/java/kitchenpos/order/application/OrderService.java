package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCompletionEvent;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
        final OrderMapper orderMapper,
        final OrderValidator orderValidator,
        final OrderRepository orderRepository,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = orderMapper.mapFrom(orderRequest);
        order.place(orderValidator);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderMapper.mapFrom(orderId);
        savedOrder.changeOrderStatus(orderValidator, orderRequest.getOrderStatus());
        if (savedOrder.isCompleted()) {
            eventPublisher.publishEvent(new OrderCompletionEvent(savedOrder));
        }
        return OrderResponse.from(savedOrder);
    }
}
