package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    public List<OrderLineItem> toEntity(Order order, List<Menu> menus) {
        return orderLineItems.stream()
            .map(orderLineItem -> new OrderLineItem(order, getMatchMenuInMenus(menus, orderLineItem.getMenuId()), orderLineItem.getQuantity()))
            .collect(Collectors.toList());
    }

    private Menu getMatchMenuInMenus(List<Menu> menus, Long menuId) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
