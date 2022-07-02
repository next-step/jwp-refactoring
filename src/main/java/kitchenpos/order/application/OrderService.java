package kitchenpos.order.application;


import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.Exception.NotFoundOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.save(new Order(orderRequest.getOrderTableId()));
        connectOrderToOrderLineItems(orderRequest, savedOrder);

        orderValidator.validate(savedOrder);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        OrderStatus orderStatus = orderStatusRequest.toOrderStatus();
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);

        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.from(savedOrder);
    }

    public boolean existsByOrderTableIdUnCompletedOrderStatus(List<Long> ids) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                ids, getCannotUngroupTableGroupStatus());
    }

    public boolean existsByOrderTableIdUnCompletedOrderStatus(Long id) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                id, getCannotUngroupTableGroupStatus());
    }

    private void connectOrderToOrderLineItems(OrderRequest orderRequest, Order savedOrder) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());

        savedOrder.addOrderLineItems(OrderLineItems.from(orderLineItems));
    }
}
