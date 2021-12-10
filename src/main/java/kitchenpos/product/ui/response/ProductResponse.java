package kitchenpos.product.ui.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public final class ProductResponse {

    private final long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return null;
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
