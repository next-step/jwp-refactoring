package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.exception.EntityNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTableValidator;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderTableValidator orderTableValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        orderTableValidator.orderTableEmptyValidate(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests()
            .stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toList());
        Order saved = orderRepository.save(Order.of(orderRequest.getOrderTableId(), OrderLineItems.of(orderLineItems)));
        return OrderResponse.of(saved);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        Order order = findOrderById(orderId);
        order.updateOrderStatus(OrderStatus.valueOf(orderStatus));
        return OrderResponse.of(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException(Order.ENTITY_NAME, orderId)
        );
    }

}
