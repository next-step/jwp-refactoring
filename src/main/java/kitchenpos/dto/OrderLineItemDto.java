package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {
    private long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;


    protected OrderLineItemDto() {
    }

    private OrderLineItemDto(long seq, Order order, Menu menu, long quantity) {
        this.seq= seq;
        this.orderId = order.getId();
        this.menuId = menu.getId();
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItemDto(seq, order, menu, quantity);
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrder(), orderLineItem.getMenu(), orderLineItem.getQuantity());
    }

    public long getSeq() {
        return this.seq;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public long getQuantity() {
        return this.quantity;
    }

    
}
