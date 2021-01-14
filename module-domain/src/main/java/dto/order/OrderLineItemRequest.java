package dto.order;

import java.util.Objects;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
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
        final OrderLineItemRequest that = (OrderLineItemRequest) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemRequest{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
