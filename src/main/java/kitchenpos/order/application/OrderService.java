package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderSaveRequest;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import kitchenpos.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(OrderSaveRequest request) {
        Order order = request.toEntity();
        orderValidator.validate(order);
        orderRepository.save(order);
        return OrderResponse.of(order);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllJoinFetch();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.of(order);
    }
}
