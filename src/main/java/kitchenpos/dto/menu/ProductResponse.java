package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
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
}
