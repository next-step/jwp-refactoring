package kitchenpos.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPriceValue());
    }

    public ProductResponse(Long id, String name, Integer price) {
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

    public Integer getPrice() {
        return price;
    }
}
