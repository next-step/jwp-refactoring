package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.event.OrderLineItemCreateEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher publisher;

    public OrderService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository, final OrderValidator orderValidator,
        final ApplicationEventPublisher publisher) {

        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
        this.publisher = publisher;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validate(orderRequest);
        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());
        final Order order = new Order(orderTable.getId());
        final Order saved = orderRepository.save(order);
        publisher.publishEvent(new OrderLineItemCreateEvent(saved, orderRequest.getOrderLineItems()));

        return OrderResponse.of(saved);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = findOrder(orderId);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeStatus(orderStatus);
        final Order saved = orderRepository.save(order);

        return OrderResponse.of(saved);
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
