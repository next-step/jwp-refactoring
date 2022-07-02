package kitchenpos.service.product.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private long price;

    public ProductResponse() {

    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
