package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

public class OrderLineItemRequest {
    private Long menuId;
    private String name;
    private BigDecimal price;
    private long quantity;
    private List<OrderLineItemDetailRequest> products;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, String name, BigDecimal price, long quantity,
        List<OrderLineItemDetailRequest> products) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.products = products;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Price price() {
        return Price.valueOf(price);
    }

    public Quantity quantity() {
        return Quantity.valueOf(quantity);
    }
}
