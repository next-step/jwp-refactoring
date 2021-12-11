package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;


    protected OrderLineItemDto() {
    }

    private OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq= seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(Long seq, Orders order, Menu menu, long quantity) {
        return new OrderLineItemDto(seq, order.getId(), menu.getId(), quantity);
    }

    public static OrderLineItemDto of(Long orderId, Long menuId, long quantity) {
        return new OrderLineItemDto(null, orderId, menuId, quantity);
    }

    public static OrderLineItemDto of(Long menuId, long quantity) {
        return new OrderLineItemDto(null, null, menuId, quantity);
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
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
