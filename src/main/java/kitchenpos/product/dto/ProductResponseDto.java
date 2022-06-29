package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice());
    }

    public ProductResponseDto(Long id, String name, BigDecimal price) {
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

}
