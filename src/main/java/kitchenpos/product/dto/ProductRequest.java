package kitchenpos.product.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    @NotEmpty
    private String name;

    @Min(0)
    private Long price;

    public ProductRequest() {
    }

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

    public Product toProduct() {
        return new Product(null, this.name, Price.of(this.price));
    }
}
