package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        OrderTable orderTable = orderValidator.validateOrderTableExist(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = orderValidator.validateOrderLineItems(convertToOrderLineItems(orderRequest.getOrderLineItems()));
        Order order = Order.of(orderTable.getId(), orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        orderValidator.validateMenuExist(orderLineItemRequests);
        return orderLineItemRequests.stream()
            .map(orderLineItemsRequest -> OrderLineItem.of(orderLineItemsRequest.getMenuId(), orderLineItemsRequest.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.toOrderResponse(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order order = orderValidator.validateOrderExist(orderId);
        order.changeStatus(orderStatusRequest.getOrderStatus());
        return OrderResponse.from(order);
    }
}
