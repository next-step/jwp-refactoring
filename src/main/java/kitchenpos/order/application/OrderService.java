package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.constants.ErrorCodeType.MATCH_NOT_MENU;
import static kitchenpos.constants.ErrorCodeType.ORDER_LINE_ITEM_REQUEST;

@Service
@Transactional
public class OrderService {
    private final MenuPort menuPort;
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;

    public OrderService(
            final MenuPort menuPort,
            final OrderPort orderPort,
            final OrderTablePort orderTablePort
    ) {
        this.menuPort = menuPort;
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
    }

    public OrderResponse create(final OrderRequest request) {
        if (Objects.isNull(request.getOrderLineItemRequest())) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUEST.getMessage());
        }

        final OrderTable orderTable = orderTablePort.findById(request.getOrderTableId());
        final List<Menu> menu = menuPort.findAllByMenuId(request.getMenuIds());

        Order order = makeOrder(orderTable.getId(), menu, request);

        final Order saveOrder = orderPort.save(order);

        return OrderResponse.from(saveOrder);
    }

    private Order makeOrder(Long orderTableId, List<Menu> menus, OrderRequest request) {
        Order order = new Order(orderTableId, OrderStatus.COOKING);

        List<OrderLineItem> orderLineItems = request.getOrderLineItemRequest().stream()
                .map(item -> createOrderLineItemRequest(menus, item))
                .collect(Collectors.toList());

        order.addOrderLineItems(orderLineItems, menus);

        return order;
    }

    private OrderLineItem createOrderLineItemRequest(List<Menu> menus, OrderLineItemRequest item) {
        Menu menu = menus.stream()
                .filter(target -> target.getId().equals(item.getMenuId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MATCH_NOT_MENU.getMessage()));

        return new OrderLineItem(menu, item.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderPort.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderPort.findById(orderId);
        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
