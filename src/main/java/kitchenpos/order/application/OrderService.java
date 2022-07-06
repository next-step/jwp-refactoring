package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private static final String ORDER_IS_NOT_EXIST = "주문이 존재하지 않습니다";
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest order) {
        orderValidator.checkOrderTable(order);
        List<OrderMenu> orderMenus = orderValidator.checkItems(order);
        Order savedOrder = orderRepository.save(order.toEntity(orderMenus));
        return new OrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus status) {
        final Order savedOrder = orderRepository.findByIdWithItem(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_IS_NOT_EXIST));
        savedOrder.changeStatus(status);
        return new OrderResponse(savedOrder);
    }
}
