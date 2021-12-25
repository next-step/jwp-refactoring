package kitchenpos.order.application;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderValidator orderValidator, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = Order.of(
            orderRequest.getOrderTableId(), makeOrderLineItems(orderRequest.getOrderLineItems()));
        orderValidator.validateCreate(order);
        final Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }

    private List<OrderLineItem> makeOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream().map(
            OrderLineItemRequest::toEntity
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findOrders();

        return orders.stream()
            .map(OrderResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_DATA));
        savedOrder.changeOrderStatus(orderStatusRequest.getOrderStatus());
        return new OrderResponse(savedOrder);
    }
}
