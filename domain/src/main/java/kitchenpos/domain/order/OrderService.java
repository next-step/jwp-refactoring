package kitchenpos.domain.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.event.order.OrderCreatedEvent;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {

        OrderTable findOrderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new InvalidEntityException("Not found OrderTable " + orderRequest.getOrderTableId()));

        List<OrderLineItem> findOrderLineItems = orderRequest.getOrderLineItemRequests()
                .stream()
                .map(orderLineItemRequest -> {
                    Long quantity = orderLineItemRequest.getQuantity();
                    return OrderLineItem.of(null, orderLineItemRequest.getMenuId(), quantity);
                })
                .collect(Collectors.toList());

        Order order = Order.of(findOrderTable, OrderStatus.COOKING, findOrderLineItems);
        eventPublisher.publishEvent(new OrderCreatedEvent(order));

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidEntityException("Not found orderId - " + orderId));

        savedOrder.changeStatus(orderStatus);

        return savedOrder;
    }

    public boolean isOrderCompletionByOrderTableIds(List<Long> orderTableIds) {
        return !orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
