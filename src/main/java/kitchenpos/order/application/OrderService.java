package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.order.port.OrderPort;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.validator.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.constants.ErrorCodeType.MATCH_NOT_MENU;

@Service
@Transactional
public class OrderService {
    private final MenuPort menuPort;
    private final OrderPort orderPort;
    private final OrderTablePort orderTablePort;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuPort menuPort,
            final OrderPort orderPort,
            final OrderTablePort orderTablePort,
            final OrderValidator orderValidator) {
        this.menuPort = menuPort;
        this.orderPort = orderPort;
        this.orderTablePort = orderTablePort;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(OrderRequest request) {
        List<Menu> menus = menuPort.findAllByMenuId(request.getMenuIds());
        orderValidator.validOrder(request, menus);
        OrderTable orderTable = orderTablePort.findById(request.getOrderTableId());
        Order saveOrder = makeOrder(orderTable.getId(), menus, request);

        return OrderResponse.from(saveOrder);
    }

    private Order makeOrder(Long orderTableId, List<Menu> menus, OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItemRequest().stream()
                .map(item -> createOrderLineItemRequest(menus, item))
                .collect(Collectors.toList());
        Order order = new Order(orderTableId, OrderStatus.COOKING, new OrderLineItems(orderLineItems));

        return orderPort.save(order);
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
        List<Order> orders = orderPort.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        Order order = orderPort.findById(orderId);
        orderValidator.validChangeOrderStatus(order);

        order.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(order);
    }
}
