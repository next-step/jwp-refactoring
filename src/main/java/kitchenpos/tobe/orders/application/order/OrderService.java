package kitchenpos.tobe.orders.application.order;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.order.Order;
import kitchenpos.tobe.orders.domain.order.OrderRepository;
import kitchenpos.tobe.orders.dto.order.OrderChangeStatusRequest;
import kitchenpos.tobe.orders.dto.order.OrderRequest;
import kitchenpos.tobe.orders.dto.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Validator<Order> orderValidator;

    public OrderService(
        final OrderRepository orderRepository,
        final Validator<Order> orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse place(final OrderRequest request) {
        final Order order = orderRepository.save(request.toOrder(orderValidator));
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeStatus(
        final Long orderId,
        final OrderChangeStatusRequest request
    ) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(NoSuchElementException::new);
        order.changeStatus(request.getStatus());
        return OrderResponse.of(order);
    }
}
