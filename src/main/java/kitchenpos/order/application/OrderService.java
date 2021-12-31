package kitchenpos.order.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderValidator.validateCreateOrder(orderRequest);

        Order order = orderRepository.save(orderRequest.toOrder(orderTable));

        return OrderResponse.of(order);
    }

    public List<OrderResponse> list() {
        return OrderResponse.listOf(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findById(orderId);

        orderValidator.validateChangeOrderStatus(order);

        order.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(order);
    }

    private Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NotFoundEntityException::new);
    }
}
