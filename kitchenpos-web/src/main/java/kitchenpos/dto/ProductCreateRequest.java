package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.model.Money;
import kitchenpos.domain.model.Product;

public class ProductCreateRequest {

    private String name;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    protected ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, Money.won(price.longValue()));
    }
}
