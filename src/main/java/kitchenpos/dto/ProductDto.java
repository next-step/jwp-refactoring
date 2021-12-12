package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.Objects;

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

    public static ProductDto of(String name, BigDecimal price) {
        return new ProductDto(null, name, price);
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProductDto)) {
            return false;
        }
        ProductDto productDto = (ProductDto) o;
        return Objects.equals(id, productDto.id) && Objects.equals(name, productDto.name) && Objects.equals(price, productDto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}