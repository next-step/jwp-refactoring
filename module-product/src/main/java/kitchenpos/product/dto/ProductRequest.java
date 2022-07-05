package kitchenpos.product.dto;


import static kitchenpos.common.message.ValidationMessage.NOT_EMPTY;
import static kitchenpos.common.message.ValidationMessage.POSITIVE_OR_ZERO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    @NotEmpty(message = NOT_EMPTY)
    private String name;

    @PositiveOrZero(message = POSITIVE_OR_ZERO)
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
