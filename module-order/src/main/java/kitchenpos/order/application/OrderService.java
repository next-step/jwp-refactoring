package kitchenpos.order.application;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderValidator.validateCreateOrder(orderRequest);
        List<OrderLineItem> requestOrderLineItems = toOrderLineItems(orderRequest.getOrderLineItems());
        OrderLineItems orderLineItems = OrderLineItems.of(requestOrderLineItems);

        Order order = Order.of(orderRequest.getOrderTableId(), orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(
            final Long orderId,
            final ChangeOrderStatusRequest changeOrderStatusRequest
    ) {
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        OrderStatus orderStatus = OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus());
        orderValidator.validateChangeOrderStatus(order);

        order.changeOrderStatus(orderStatus);
        return ChangeOrderStatusResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream().map(orderLineItemRequest -> {
            Quantity quantity = Quantity.of(orderLineItemRequest.getQuantity());
            return OrderLineItem.of(orderLineItemRequest.getMenuId(), quantity);
        }).collect(Collectors.toList());
    }
}
