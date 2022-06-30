package kitchenpos.order.dto;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private Long id;
    private String menuName;
    private BigDecimal menuPrice;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(
            final Long id,
            final String menuName,
            final BigDecimal menuPrice,
            final Long quantity) {
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
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
