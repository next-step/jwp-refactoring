package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.event.OrderCreatedEvent;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.event.customEvent.OrderCreateEvent;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final List<OrderLineItemDTO> orderLineItems = orderRequest.getOrderLineItems();

        orderCreateEventPublish(orderRequest);

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new OrderException("주문넣을 메뉴는 존재해야합니다");
        }

        Order order = new Order();
        order.mapToTable(orderRequest.getOrderTableId());
        order.startCooking();

        setOrderLineItems(orderLineItems, order);

        return OrderResponse.of(orderRepository.save(order));
    }

    private void setOrderLineItems(List<OrderLineItemDTO> orderLineItems, Order order) {
        for (final OrderLineItemDTO orderLineItem : orderLineItems) {
            order.mapOrderLineItem(
                new OrderLineItem(order, orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }
    }

    private void orderCreateEventPublish(OrderRequest orderRequest) {
        final List<Long> menuIds = orderRequest.getMenuIds();

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
            orderRequest.getOrderTableId(),
            menuIds);
        eventPublisher.publishEvent(new OrderCreateEvent(orderCreatedEvent));
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderException("상태 변경할 주문은 저장되어있어야 합니다"));

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
