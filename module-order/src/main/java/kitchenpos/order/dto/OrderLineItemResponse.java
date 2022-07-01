package kitchenpos.order.dto;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long menuId;
    private final String menuName;
    private final Integer price;
    private final Integer quantity;

    public OrderLineItemResponse(Long menuId, String menuName, Price price, Quantity quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price.getPrice();
        this.quantity = quantity.getQuantity();
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getMenuId(),
                orderLineItem.getMenuName(),
                orderLineItem.getPrice(),
                orderLineItem.getQuantity());
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
