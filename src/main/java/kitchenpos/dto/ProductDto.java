package kitchenpos.dto;

import java.math.BigDecimal;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductDto() {
    }

    private ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Long id, String name, BigDecimal price) {
        return new ProductDto(id, name, price);
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), BigDecimal.valueOf(product.getPrice().value()));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product toProduct() {
        return Product.of(this.id, this.name, Price.of(this.price));
    }
}
