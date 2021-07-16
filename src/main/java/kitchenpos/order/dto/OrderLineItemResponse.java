package kitchenpos.order.dto;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long menuId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getMenuId(),
            orderLineItem.getName(),
            orderLineItem.getPrice().value(),
            orderLineItem.getQuantity().value());
    }

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long menuId, String name, BigDecimal price, long quantity) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
