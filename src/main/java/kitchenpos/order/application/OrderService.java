package kitchenpos.order.application;

import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.mapper.OrderMapper;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        final Order order = request.toEntity();
        orderValidator.orderCreateValidator(order);

        final Order savedOrder = orderRepository.save(order);
        savedOrder.addOrder();

        return OrderMapper.toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderMapper.toOrderResponses(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("order not found. find order id is %d", orderId)));

        findOrder.changeOrderStatus(request.getOrderStatus(), orderValidator);

        return OrderMapper.toOrderResponse(findOrder);
    }
}
