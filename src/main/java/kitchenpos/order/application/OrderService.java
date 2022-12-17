package kitchenpos.order.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private static final String ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT = "주문이 존재하지 않습니다. ID : %d";

    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = orderValidator.validate(request);
        return OrderResponse.from(orderRepository.save(toOrder(request.getOrderTableId(), orderLineItems)));
    }

    public List<OrderResponse> list() {
        return OrderResponse.toList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long id, OrderStatusRequest request) {
        Order order = findById(id);
        order.changeStatus(request.status());
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order toOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_BY_ORDER_FORMAT, id)));
    }
}
