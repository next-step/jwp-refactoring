package kitchenpos.order.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.common.domain.Name;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, List<Menu> menus) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
            order.getOrderedTime(),
            createOrderLineItemResponses(order.getMenuIdQuantityPairs(), menus));
    }

    private static List<OrderLineItemResponse> createOrderLineItemResponses(List<MenuIdQuantityPair> pairs,
        List<Menu> menus) {
        return pairs.stream()
            .map(menuIdQuantityPair -> new OrderLineItemResponse(findMenuName(menus, menuIdQuantityPair.getMenuId()),
                menuIdQuantityPair.getQuantity()))
            .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    private static Name findMenuName(List<Menu> menus, Long menuId) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .getName();
    }

}
