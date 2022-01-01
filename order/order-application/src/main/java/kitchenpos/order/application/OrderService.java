package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderResponses;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository,
        OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = orderRequest.toEntity();
        orderValidator.validate(order);

        return OrderResponse.from(orderRepository.save(order));
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();

        return OrderResponses.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(KitchenposNotFoundException::new);

        order.updateOrderStatus(request.getOrderStatus());

        orderRepository.save(order);

        return OrderResponse.from(order);
    }
}
