package kitchenpos.menuproduct.dto;

import kitchenpos.menuproduct.domain.MenuProduct;

public class MenuProductResponse {
    private Long id;
    private Long productId;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long id, Long productId, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(), menuProduct.productId(), menuProduct.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
