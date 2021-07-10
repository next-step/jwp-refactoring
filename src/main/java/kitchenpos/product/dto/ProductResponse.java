package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductEntity;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static ProductResponse of(ProductEntity productEntity) {
        return new ProductResponse(productEntity.getId(), productEntity.getName(), productEntity.getPrice());
    }
}
