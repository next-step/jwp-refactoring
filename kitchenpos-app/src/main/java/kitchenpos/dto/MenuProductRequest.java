package kitchenpos.dto;

public class MenuProductRequest {
    private Long productId;
    private int quantity;

    protected MenuProductRequest() {}

    public MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public static MenuProductRequestBuilder builder() {
        return new MenuProductRequestBuilder();
    }

    public static final class MenuProductRequestBuilder {
        private Long productId;
        private int quantity;

        private MenuProductRequestBuilder() {}

        public MenuProductRequestBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductRequestBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductRequest build() {
            return new MenuProductRequest(productId, quantity);
        }
    }
}
