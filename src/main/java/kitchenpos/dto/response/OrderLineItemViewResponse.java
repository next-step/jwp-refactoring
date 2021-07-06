package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

import java.util.Objects;

public class OrderLineItemViewResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemViewResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemViewResponse(
                orderLineItem.getId(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().toLong()
        );
    }

    public OrderLineItemViewResponse() {
    }

    public OrderLineItemViewResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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
        OrderLineItemViewResponse that = (OrderLineItemViewResponse) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(orderId, that.orderId) && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }
}
