package kitchenpos.dto.response;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductViewResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductViewResponse of(Product product) {
        return new ProductViewResponse(product.getId(), product.getName(), product.getPrice().getPrice());
    }

    public ProductViewResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
