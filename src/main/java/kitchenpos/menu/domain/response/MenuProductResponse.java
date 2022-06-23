package kitchenpos.menu.domain.response;

import kitchenpos.menu.domain.MenuProductEntity;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProductEntity menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(),
            ProductResponse.of(menuProduct.getProduct()),
            menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
