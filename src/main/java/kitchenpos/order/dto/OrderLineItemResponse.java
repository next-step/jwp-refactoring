package kitchenpos.order.dto;

import java.util.Objects;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderLineItemResponse))
            return false;
        OrderLineItemResponse that = (OrderLineItemResponse)o;
        return quantity == that.quantity &&
            Objects.equals(seq, that.seq) &&
            Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, quantity);
    }
}
