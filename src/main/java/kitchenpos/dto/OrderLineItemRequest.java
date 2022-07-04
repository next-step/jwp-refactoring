package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(OrderLineItem orderLineItem) {
        Long orderId = setOrderLineItemOrderId(orderLineItem);
        Long menuId = setOrderLineItemMenuId(orderLineItem);

        this.seq = orderLineItem.getSeq();
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    private Long setOrderLineItemOrderId(OrderLineItem orderLineItem) {
        Long orderId = null;
        Order order = orderLineItem.getOrder();
        if (order != null) {
            orderId = order.getId();
        }
        return orderId;
    }

    private Long setOrderLineItemMenuId(OrderLineItem orderLineItem) {
        Long menuId = null;
        Menu menu = orderLineItem.getMenu();
        if (menu != null) {
            menuId = menu.getId();
        }
        return menuId;
    }
}
