package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemResponse {

    private Long id;

    private Long orderId;

    private Long menuId;

    private long quantity;

    public OrderLineItemResponse() {}

    public OrderLineItemResponse(Long id, Order order, OrderMenu orderMenu, Quantity quantity) {
        this.id = id;
        this.orderId = order.getId();
        this.menuId = orderMenu.getMenuId();
        this.quantity = quantity.value();
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder(), orderLineItem.getOrderMenu(), orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
