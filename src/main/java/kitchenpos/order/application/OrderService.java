package kitchenpos.order.application;

import kitchenpos.order.application.event.OrderCreatedEvent;
import kitchenpos.order.application.exception.NotExistOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.OrderRequest;
import kitchenpos.order.presentation.dto.OrderResponse;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderTableService orderTableService, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = getOrderLineItemsBy(orderRequest.getOrderLineItems());
        OrderTable orderTable = orderTableService.findById(orderRequest.getOrderTableId());
        Order order = orderTable.ordered(orderLineItems);
        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> getOrderLineItemsBy(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> OrderLineItem.of(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrderResponses() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = findById(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(NotExistOrderException::new);
    }
}
