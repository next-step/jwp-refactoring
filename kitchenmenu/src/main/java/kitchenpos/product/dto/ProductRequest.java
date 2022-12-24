package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {}

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(name, new ProductPrice(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
