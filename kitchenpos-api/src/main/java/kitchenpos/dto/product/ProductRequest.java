package kitchenpos.dto.product;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import kitchenpos.domain.product.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {
        // empty
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(this.name, Price.of(this.price));
    }
}
