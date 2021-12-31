package kitchenpos.order.application;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.Validator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
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
            .orElseThrow(() -> new NoSuchElementException("요청되지 않은 주문을 변경할 수 없습니다."));
        order.changeStatus(request.getStatus());
        return OrderResponse.of(order);
    }
}
