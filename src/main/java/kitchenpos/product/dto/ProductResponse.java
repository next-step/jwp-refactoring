package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    public ProductResponse(long id, Name name, Price price) {
        this.id = id;
        this.name = name.value();
        this.price = price.value();
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
