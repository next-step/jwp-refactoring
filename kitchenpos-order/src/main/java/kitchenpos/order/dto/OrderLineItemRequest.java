package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemRequest {
    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItemRequest(Long menuId, String menuName, BigDecimal price, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = price;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return new OrderLineItem(new OrderMenu(menuId, menuName, menuPrice), quantity);
    }
}
