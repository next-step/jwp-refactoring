package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.product.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private Price productPrice;

    public ProductResponse() {
    }

    private ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.productPrice = product.getProductPrice();
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getProductPrice() {
        return productPrice;
    }
}
