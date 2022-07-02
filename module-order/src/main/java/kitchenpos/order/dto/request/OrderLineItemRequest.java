package kitchenpos.order.dto.request;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem toEntity(OrderLineItemRequest request, String menuName, BigDecimal menuPrice) {
        return OrderLineItem.of(null, null, request.getMenuId(), menuName, menuPrice, request.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
