package kitchenpos.order.application;


import static kitchenpos.order.domain.OrderStatus.getCannotUngroupTableGroupStatus;

import kitchenpos.Exception.NotFoundMenuException;
import kitchenpos.Exception.NotFoundOrderException;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
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

        final Order savedOrder = orderRepository.save(new Order(orderTable));
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
                .orElseThrow(NotFoundOrderException::new);

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

        validateNotFoundMenu(orderLineItems);
        savedOrder.addOrderLineItems(OrderLineItems.from(orderLineItems));
    }

    private void validateNotFoundMenu(List<OrderLineItem> orderLineItems) {

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuService.countByIdIn(menuIds)) {
            throw new NotFoundMenuException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }


}
