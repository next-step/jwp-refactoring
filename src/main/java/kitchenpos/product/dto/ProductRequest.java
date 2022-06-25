package kitchenpos.product.dto;

import kitchenpos.product.domain.ProductV2;

public class ProductRequest {
    private String name;
    private Long price;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public ProductV2 toProduct() {
        return new ProductV2(null, this.name, this.price);
    }
}
