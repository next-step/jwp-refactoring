package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderLineItems orderLineItems = findOrderLineItems(orderRequest.getOrderLineItems());

        final List<Long> menuIds = orderLineItems.findMenuIds();
        orderLineItems.vaildateSize(orderValidator.menuCountByIdIn(menuIds));

        Order saveOrder = new Order(orderRequest.getOrderTableId(), OrderStatus.COOKING, orderLineItems);
        final Order savedOrder = saveOrder(saveOrder);

        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = findOrders();

        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest orderChangeStatusRequest) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.updateOrderStatus(orderChangeStatusRequest.getOrderStatus());
        saveOrder(savedOrder);

        return OrderResponse.of(savedOrder);
    }

    private Order saveOrder(Order saveOrder) {
        return orderRepository.save(saveOrder);
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);
    }

    private List<Order> findOrders() {
        return orderRepository.findAll();
    }

    private OrderLineItems findOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        return new OrderLineItems(orderLineItems.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList()));
    }

    private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return new OrderLineItem(null, null, orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity());
    }
}
