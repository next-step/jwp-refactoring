package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemResponse {

    private Long seq;

    private Long orderId;

    private Long menuId;

    private long quantity;

    public OrderLineItemResponse() {}

    public OrderLineItemResponse(Long seq, Order order, OrderMenu orderMenu, Quantity quantity) {
        this.seq = seq;
        this.orderId = order.getId();
        this.menuId = orderMenu.getMenuId();
        this.quantity = quantity.value();
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder(), orderLineItem.getOrderMenu(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
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
