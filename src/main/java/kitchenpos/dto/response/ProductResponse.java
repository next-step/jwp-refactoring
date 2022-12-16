package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Long id, String name, BigDecimal price){
        return new ProductResponse(id, name, price);
    }

    public static ProductResponse of(Product product){
        return ProductResponse.of(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
