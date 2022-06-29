package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;

    private String name;

    private Integer price;

    protected ProductResponse() {
    }

    private ProductResponse(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(),
                product.getPriceIntValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
