package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().value());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
