package kichenpos.order.ui.dto;

import kichenpos.common.domain.Price;
import kichenpos.order.domain.OrderLineItem;
import kichenpos.order.domain.OrderMenu;

import java.math.BigDecimal;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, String menuName, BigDecimal price, long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = price;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(new OrderMenu(menuId, menuName, new Price(menuPrice)), quantity);
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
}
