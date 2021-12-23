package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    public Long seq;
    public Long productName;
    public Long quantity;

    public MenuProductResponse(Long seq, Long productName, Long quantity) {
        this.seq = seq;
        this.productName = productName;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public void setProductName(Long productName) {
        this.productName = productName;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }
}
