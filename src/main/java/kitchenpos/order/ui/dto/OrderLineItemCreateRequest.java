package kitchenpos.order.ui.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private String menuName;
    private BigDecimal price;
    private long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, String menuName, BigDecimal price, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(new OrderMenu(menuId, menuName, new Price(price)), quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
