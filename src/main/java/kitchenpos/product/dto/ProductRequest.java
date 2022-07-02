package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

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
