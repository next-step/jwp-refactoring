package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(final OrderRepository orderRepository, final OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final Order order = generateOrder(orderRequest);
        orderValidator.validateSave(orderRequest);
        return OrderResponse.from(orderRepository.save(order));
    }

    private Order generateOrder(OrderRequest orderRequest) {
        return Order.generate(
            orderRequest.getOrderTableId(),
            orderRequest.getOrderLineItems().stream()
                .map(this::generateOrderLineItem)
                .collect(Collectors.toList())
        );
    }

    private OrderLineItem generateOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return OrderLineItem.generate(
            orderLineItemRequest.getMenuId(),
            orderLineItemRequest.getQuantity()
        );
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.updateOrderStatus(order.getOrderStatus());

        return savedOrder;
    }
}
