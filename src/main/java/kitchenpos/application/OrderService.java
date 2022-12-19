package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.NOT_FOUND_ORDER;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.validator.OrderValidator;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public OrderResponse create(final Order order) {
        orderValidator.validateCreate(order);

        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Order findById(final Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new KitchenposException(NOT_FOUND_ORDER));
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }
}
