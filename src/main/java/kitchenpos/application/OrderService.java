package kitchenpos.application;


import static kitchenpos.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final OrderTableService orderTableService
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableService.findOrderTableById(orderRequest.getOrderTableId());

        final Order savedOrder = orderRepository.save(orderRequest.toOrder(orderTable));
        connectOrderToOrderLineItems(orderRequest, savedOrder);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        return OrderResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        OrderStatus orderStatus = orderStatusRequest.toOrderStatus();
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);
        return OrderResponse.from(savedOrder);
    }

    public boolean existsByOrderTableIdUnCompletedOrderStatus(List<Long> ids) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                ids, getCannotUngroupTableGroupStatus());
    }

    public boolean existsByOrderTableIdUnCompletedOrderStatus(Long id) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                id, getCannotUngroupTableGroupStatus());
    }

    private void connectOrderToOrderLineItems(OrderRequest orderRequest, Order savedOrder) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        savedOrder.addOrderLineItems(OrderLineItems.of(orderLineItems, menuService.countByIdIn(menuIds)));
    }


}
