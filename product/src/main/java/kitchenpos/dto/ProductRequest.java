package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductRequest {

    private final String name;

    private final BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(new Name(name), new Price(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
