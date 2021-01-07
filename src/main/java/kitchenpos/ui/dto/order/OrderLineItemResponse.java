package kitchenpos.ui.dto.order;

import java.util.Objects;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItemResponse that = (OrderLineItemResponse) o;
        return Objects.equals(seq, that.seq) && Objects.equals(orderId, that.orderId) && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
                "seq=" + seq +
                ", orderId=" + orderId +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
