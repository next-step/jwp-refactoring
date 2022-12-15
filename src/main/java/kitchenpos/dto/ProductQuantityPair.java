package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductQuantityPair {
    private Product product;
    private Long quantity;

    public ProductQuantityPair() {
    }

    public ProductQuantityPair(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long sumOfPrice() {
        return this.quantity * product.getPrice().longValue();
    }
}
