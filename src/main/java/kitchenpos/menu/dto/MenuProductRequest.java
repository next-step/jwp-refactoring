package kitchenpos.menu.dto;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long menuId, Long productId, long quantity) {
        return new MenuProductRequest(menuId, productId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
