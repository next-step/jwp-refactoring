package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct.Builder()
                .product(product)
                .quantity(this.quantity)
                .build();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class Builder {

        private Long productId;
        private long quantity;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductRequest build() {
            return new MenuProductRequest(productId, quantity);
        }
    }
}
