package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    public static final String ORDER_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 주문 입니다.";

    private final OrderCreationValidator orderCreationValidator;
    private final OrderRepository orderRepository;

    public OrderService(
        OrderCreationValidator orderCreationValidator,
        OrderRepository orderRepository
    ) {
        this.orderCreationValidator = orderCreationValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest createOrderRequest) {
        orderCreationValidator.validate(createOrderRequest);
        return OrderResponse.from(orderRepository.save(createOrderRequest.toOrder()));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = findOrderById(orderId);
        savedOrder.changeOrderStatus(changeOrderStatusRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND_ERROR_MESSAGE));
    }
}
