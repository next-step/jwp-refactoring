package kitchenpos.dto;

import java.math.BigDecimal;

import kitchenpos.domain.OrderLineItemDetail;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

public class OrderLineItemDetailRequest {
    private Long menuProductSeq;
    private Long productId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemDetailRequest() {
    }

    public OrderLineItemDetailRequest(Long menuProductSeq, Long productId,
            String name, BigDecimal price, long quantity) {
        this.menuProductSeq = menuProductSeq;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getMenuProductSeq() {
        return menuProductSeq;
    }

    public void setMenuProductSeq(Long menuProductSeq) {
        this.menuProductSeq = menuProductSeq;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public OrderLineItemDetail toEntity() {
        return new OrderLineItemDetail(this.name, this.price(), this.quantity());
    }
}
