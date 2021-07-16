package kitchenpos.product.domain;

import kitchenpos.generic.price.domain.Price;

public class ProductOption {
    private Long productId;
    private String name;
    private Price price;

    public ProductOption(Long productId, String name, Price price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
