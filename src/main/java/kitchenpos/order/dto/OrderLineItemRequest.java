package kitchenpos.order.dto;

import java.math.BigDecimal;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;
    private String orderLineItemMenuName;
    private BigDecimal orderLineItemMenuPrice;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity, String orderLineItemMenuName,
                                BigDecimal orderLineItemMenuPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderLineItemMenuName = orderLineItemMenuName;
        this.orderLineItemMenuPrice = orderLineItemMenuPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getOrderLineItemMenuName() {
        return orderLineItemMenuName;
    }

    public void setOrderLineItemMenuName(String orderLineItemMenuName) {
        this.orderLineItemMenuName = orderLineItemMenuName;
    }

    public BigDecimal getOrderLineItemMenuPrice() {
        return orderLineItemMenuPrice;
    }

    public void setOrderLineItemMenuPrice(BigDecimal orderLineItemMenuPrice) {
        this.orderLineItemMenuPrice = orderLineItemMenuPrice;
    }
}
