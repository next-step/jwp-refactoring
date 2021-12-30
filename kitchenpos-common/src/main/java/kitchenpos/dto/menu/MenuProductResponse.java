package kitchenpos.dto.menu;


import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {

    private Long id;

    private Long productId;

    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long id, Long productId, long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(), menuProduct.getProductId(),
            menuProduct.getQuantityValue());
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
