package kitchenpos.menu.dto;

import java.util.Objects;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

    private Long seq;
    private long quantity;

    private ProductResponse product;

    public MenuProductResponse() {}

    public MenuProductResponse(Long seq, long quantity, ProductResponse product) {
        this.seq = seq;
        this.quantity = quantity;
        this.product = product;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getQuantityValue(), ProductResponse.from(menuProduct.getProduct()));
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuProductResponse)) {
            return false;
        }
        MenuProductResponse that = (MenuProductResponse) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, quantity, product);
    }

    public static class Builder {

        private Long seq;
        private long quantity;
        private ProductResponse product;


        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder product(ProductResponse product) {
            this.product = product;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductResponse build() {
            return new MenuProductResponse(seq, quantity, product);
        }
    }
}
