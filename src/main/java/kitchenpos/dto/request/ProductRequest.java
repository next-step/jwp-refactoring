package kitchenpos.dto.request;

import kitchenpos.domain.menus.product.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    private Long id;
    private String name;
    private BigDecimal price;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Product toEntity() {
        return new Product(id, name, price);
    }
}

