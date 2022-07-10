package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductResponse from(Product product) {
        ProductResponse response = new ProductResponse();

        response.id = product.getId();
        response.name = product.getName();
        response.price = product.getPrice().getValue();

        return response;
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
