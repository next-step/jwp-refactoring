package kitchenpos.dto.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductDTO {
    private Long productId;
    private Long quantity;

    public MenuProductDTO(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDTO of(MenuProduct menuProduct){
        return new MenuProductDTO(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
