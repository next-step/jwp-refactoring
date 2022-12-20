package kitchenpos.order.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
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
    public OrderResponse create(final OrderRequest request) {
        orderValidator.validate(request);
        List<OrderLineItemRequest> requestOrderLineItems = request.getOrderLineItemsRequest();
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(requestOrderLineItems);
        Order savedOrder = orderRepository.save(Order.of(request.getOrderTableId(), orderLineItems));
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> requestOrderLineItems) {
        return requestOrderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_ORDER.getMessage(), orderId)));

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
