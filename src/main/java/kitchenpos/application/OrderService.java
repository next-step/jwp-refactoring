package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.event.OrderCreateEventDTO;
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
        List<Long> menuIds = orderRequest.getOrderLineItems().stream()
            .map(OrderLineItemDTO::getMenuId)
            .collect(Collectors.toList());
        OrderCreateEventDTO orderCreateEventDTO = new OrderCreateEventDTO(
            orderRequest.getOrderTableId(),
            menuIds);
        eventPublisher.publishEvent(new OrderCreateEvent(orderCreateEventDTO));

        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new OrderException("MENU FOR ORDER IS EMPTY");
        }

        final List<OrderLineItemDTO> orderLineItems = orderRequest.getOrderLineItems();

        Order order = new Order();
        order.mapToTable(orderRequest.getOrderTableId());
        order.startCooking();

        for (final OrderLineItemDTO orderLineItem : orderLineItems) {
            order.mapOrderLineItem(
                new OrderLineItem(order, orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }

        return OrderResponse.of(orderRepository.save(order));
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
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(orderRepository.save(savedOrder));
    }
}
