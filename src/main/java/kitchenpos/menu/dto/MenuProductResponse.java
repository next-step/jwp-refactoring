package kitchenpos.menu.dto;

import kitchenpos.product.dto.ProductResponse;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private long quantity;

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
            ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity().value());
    }

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
