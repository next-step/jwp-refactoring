package kitchenpos.dto;

import kitchenpos.domain.OrderLineItemEntity;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private Long id;
    private String menuName;
    private BigDecimal menuPrice;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long id, String menuName, BigDecimal menuPrice, Long quantity) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItemEntity orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getId(),
                orderLineItem.getMenu().getName(),
                orderLineItem.getMenu().getPrice(),
                orderLineItem.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public Long getQuantity() {
        return quantity;
    }
}
