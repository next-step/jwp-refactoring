package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final BigDecimal unitPrice) {
        this.id = id;
        this.name = name;
        this.price = unitPrice;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getUnitPrice()
        );
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
}
