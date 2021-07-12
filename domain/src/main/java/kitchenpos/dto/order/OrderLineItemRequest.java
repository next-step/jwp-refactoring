package kitchenpos.dto.order;

import java.util.Objects;

public class OrderLineItemRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemRequest that = (OrderLineItemRequest) o;
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
