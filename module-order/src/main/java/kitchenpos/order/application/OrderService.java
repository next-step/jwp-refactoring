package kitchenpos.order.application;

import kitchenpos.common.OrderErrorCode;
import kitchenpos.exception.NotFoundException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
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
        Order order = Order.of(orderRequest.getOrderTableId(), orderRequest.toOrderLineItems());
        order.registerOrder(orderValidator);

        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.toList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(OrderErrorCode.ORDER_NOT_FOUND_EXCEPTION));

        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
