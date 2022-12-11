package kitchenpos.order.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderExceptionCode;
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
    public OrderResponse create(OrderRequest request) {
        orderValidator.validateToCreateOrder(request.getOrderTableId(), request.toMenuIds());
        Order order = request.toOrder(request.getOrderTableId(), OrderStatus.COOKING);

        return OrderResponse.of(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.list(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus request) {
        Order order = findOrderById(orderId);
        order.changeOrderStatus(request);

        return OrderResponse.of(orderRepository.save(order));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(OrderExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }
}
