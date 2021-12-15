package kitchenpos.product.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public final class ProductRequest {

    private final String name;
    private final BigDecimal price;

    @JsonCreator
    public ProductRequest(@JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product toEntity() {
        return Product.of(Name.from(name), Price.from(price));
    }
}
