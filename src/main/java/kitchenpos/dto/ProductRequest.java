package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;

public class ProductRequest {
    private String name;
    private BigDecimal price;

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    protected ProductRequest() {
    }

    public Product toProduct() {
        return new Product(ProductName.from(name), Price.from(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
