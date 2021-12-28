package kitchenpos.order.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.event.ValidateEmptyTableEvent;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.order.domain.MenuId;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableId;
import kitchenpos.order.domain.OrdersValidator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrdersRepository orderRepository;
    private final OrdersValidator ordersValidator;
    private final ApplicationEventPublisher eventPublisher;
    public OrderService(
            final OrdersRepository orderRepository,
            final OrdersValidator ordersValidator,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.ordersValidator = ordersValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();

        final OrderLineItems orderLineItems = createOrderLineItems(orderLineItemDtos);

        eventPublisher.publishEvent(new ValidateEmptyTableEvent(orderDto.getOrderTableId()));

        final Orders order = Orders.of(OrderTableId.of(orderDto.getOrderTableId()), OrderStatus.COOKING, orderLineItems, ordersValidator);

        return OrderDto.of(orderRepository.save(order));
    }
    private OrderLineItems createOrderLineItems(final List<OrderLineItemDto> orderLineItemDtos) {
        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                                                                .map(orderLineItemDto -> OrderLineItem.of(MenuId.of(orderLineItemDto.getMenuId()), orderLineItemDto.getQuantity()))
                                                                .collect(Collectors.toList());

        return OrderLineItems.of(orderLineItems);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll().stream()
                                .map(OrderDto::of)
                                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto order) {
        final Orders savedOrder = orderRepository.findById(orderId)
                                                    .orElseThrow(NotFoundOrderException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()), ordersValidator);

        return OrderDto.of(savedOrder);
    }

    public Orders findByOrderTableId(Long orderTableId) {
        return orderRepository.findByOrderTableId(OrderTableId.of(orderTableId));
    }

    public List<Orders> findAllByOrderTableIdIn(List<Long> orderTableIds) {
        return orderRepository.findAllByOrderTableIdIn(orderTableIds.stream().map(OrderTableId::of).collect(Collectors.toList()));
    }

    public boolean hasNotComplateStatus(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds.stream().map(OrderTableId::of).collect(Collectors.toList()), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
