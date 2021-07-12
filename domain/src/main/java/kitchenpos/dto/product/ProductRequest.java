package kitchenpos.dto.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;

public class ProductRequest {

    private String name;
    private Long price;

    public ProductRequest() {
    }

    @JsonCreator
    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Product toEntity() {
        return Product.of(name, Price.of(price));
    }
}
