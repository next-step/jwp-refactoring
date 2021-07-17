package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.handler.OrderMapper;
import kitchenpos.order.handler.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderMapper orderMapper, OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderMapper.mapFormToOrder(orderRequest);
        order.validateOrder(orderValidator);
        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundOrderException());
        savedOrder.changeOrderStatusCooking();
        return OrderResponse.of(savedOrder);
    }
}
