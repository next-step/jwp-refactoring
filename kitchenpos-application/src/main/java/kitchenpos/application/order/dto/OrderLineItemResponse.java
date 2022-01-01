package kitchenpos.application.order.dto;

import kitchenpos.core.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
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
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        OrderLineItemResponse that = (OrderLineItemResponse) target;

        if (quantity != that.quantity) return false;
        if (!Objects.equals(seq, that.seq)) return false;
        return Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, quantity);
    }
}
