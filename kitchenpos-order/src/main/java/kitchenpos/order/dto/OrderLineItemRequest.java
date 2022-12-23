package kitchenpos.order.dto;

import java.math.BigDecimal;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemRequest {
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private int quantity;

    private OrderLineItemRequest(Long menuId, String menuName, BigDecimal price, int quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = price;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(Long menuId, String menuName, int price, int quantity) {
        return new OrderLineItemRequest(menuId, menuName, BigDecimal.valueOf(price), quantity);
    }

    public static OrderLineItemRequest of(Long menuId, String menuName, BigDecimal price, int quantity) {
        return new OrderLineItemRequest(menuId, menuName, price, quantity);
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.of(OrderMenu.of(menuId, Name.of(menuName), Price.of(menuPrice)), quantity);
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

    public int getQuantity() {
        return quantity;
    }
}
