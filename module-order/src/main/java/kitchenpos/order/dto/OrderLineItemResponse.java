package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return getQuantity() == that.getQuantity()
                && Objects.equals(getOrderId(), that.getOrderId())
                && Objects.equals(getMenuId(), that.getMenuId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getMenuId(), getQuantity());
    }
}
