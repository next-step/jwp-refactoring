package kitchenpos.order.application;


import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderGeneratedEvent;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private static final String NOT_FOUND_ORDER = "찾을 수 없는 주문 ";
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        Long orderTableId = orderRequest.getOrderTableId();
        orderValidator.validateOrderTable(orderTableId);
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItem();
        Order order = Order.of(orderTableId, orderLineItems);
        applicationEventPublisher.publishEvent(new OrderGeneratedEvent(order));
        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(NOT_FOUND_ORDER));
        order.changeOrderStatus(orderStatus);
        return new OrderResponse(order);
    }

    public boolean isUpgroupTableStatus(List<Long> orderTableId) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
