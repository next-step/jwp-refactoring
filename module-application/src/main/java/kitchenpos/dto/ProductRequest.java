package kitchenpos.dto;


import kitchenpos.common.Money;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest() {
    }

    public Product toProduct() {
        return new Product(name, new Money(price));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
