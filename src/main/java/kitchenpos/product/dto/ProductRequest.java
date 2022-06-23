package kitchenpos.product.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.product.domain.Product;

import java.util.Objects;

public class ProductRequest {
    private String name;
    private long price;

    @JsonCreator
    public ProductRequest(@JsonProperty("name") String name, @JsonProperty("price") long price) {
        check(name, price);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Product toProduct() {
        return new Product(this.name, this.price);
    }

    private void check(String name, long price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }
}

