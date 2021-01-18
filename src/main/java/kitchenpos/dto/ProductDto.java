package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;

public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDto() {
    }

    public ProductDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductDto of(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice().amount);
    }

    public Product toEntity() {
        return new Product(name, Money.won(price.longValue()));
    }
}
