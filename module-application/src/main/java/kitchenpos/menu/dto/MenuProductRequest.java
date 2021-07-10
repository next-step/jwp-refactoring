package kitchenpos.menu.dto;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private Long quantity;

    protected MenuProductRequest() {}

    public MenuProductRequest(Long menuId, Long productId, Long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Boolean isEqualProductId(Long productId) {
        return this.productId.equals(productId);
    }
}
