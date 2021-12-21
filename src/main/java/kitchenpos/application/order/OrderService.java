package kitchenpos.application.order;

import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrdersRepository;
import kitchenpos.domain.order.OrdersValidator;
import kitchenpos.dto.order.OrderDto;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.order.NotFoundOrderException;
import kitchenpos.vo.MenuId;
import kitchenpos.vo.OrderTableId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrdersRepository orderRepository;
    private final OrdersValidator ordersValidator;
    public OrderService(
            final OrdersRepository orderRepository,
            final OrdersValidator ordersValidator
    ) {
        this.orderRepository = orderRepository;
        this.ordersValidator = ordersValidator;
    }

    @Transactional
    public OrderDto create(final OrderDto order) {
        final List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems();
        final OrderLineItems orderLineItems = createOrderLineItems(orderLineItemDtos);

        final Orders newOrder = Orders.of(OrderTableId.of(order.getOrderTableId()), OrderStatus.COOKING, orderLineItems);
        ordersValidator.validateForCreate(newOrder);
        
        return OrderDto.of(orderRepository.save(newOrder));
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
    public Orders changeOrderStatus(final Long orderId, final OrderDto order) {
        final Orders savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(NotFoundOrderException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(order.getOrderStatus()));

        return savedOrder;
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
