package kitchenpos.product.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {
    String name;
    BigDecimal price;

    public ProductRequest() {

    }

    private ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static ProductRequest of(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public Product toProduct() {
        return Product.of(Name.of(name), Price.of(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
