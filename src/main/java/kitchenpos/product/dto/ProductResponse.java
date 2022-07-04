package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private Long productId;
    private String name;
    private BigDecimal price;

    public ProductResponse() {
    }

    public ProductResponse(final Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
