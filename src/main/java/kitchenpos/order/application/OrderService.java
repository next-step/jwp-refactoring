package kitchenpos.order.application;

import java.time.LocalDateTime;
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
import kitchenpos.order.exception.OrderNotFoundException;

@Service
@Transactional
public class OrderService {
    private final OrderOrderTableValidator orderOrderTableValidator;
    private final OrderMenuValidator orderMenuValidator;
    private final OrderRepository orderRepository;

    public OrderService(OrderOrderTableValidator orderOrderTableValidator, OrderMenuValidator orderMenuValidator, OrderRepository orderRepository) {
        this.orderOrderTableValidator = orderOrderTableValidator;
        this.orderMenuValidator = orderMenuValidator;
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        orderOrderTableValidator.validateExistsOrderTableByIdAndEmptyIsFalse(orderRequest.getOrderTableId());
        Order order = orderRepository.save(makeOrderWithOrderLineItemRequests(new Order(LocalDateTime.now(), orderRequest.getOrderTableId()),
                orderRequest.getOrderLineItemRequests()));
        return OrderResponse.of(order);
    }

    private Order makeOrderWithOrderLineItemRequests(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("입력된 주문 항목이 없습니다.");
        }
        orderLineItemRequests.forEach(o -> order.addOrderLineItem(createOrderLineItem(order, o)));
        return order;
    }

    private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest orderLineItemRequest) {
        orderMenuValidator.validateExistsMenuById(orderLineItemRequest.getMenuId());
        return new OrderLineItem(order, orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity());
    }
}
