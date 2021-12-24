package kitchenpos.order.dto;

import java.util.Objects;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public boolean isSameMenuId(Long menuId) {
        return Objects.equals(this.menuId, menuId);
    }

    public OrderLineItem toOrderLineItem() {
        return OrderLineItem.of(menuId, quantity);
    }
}
