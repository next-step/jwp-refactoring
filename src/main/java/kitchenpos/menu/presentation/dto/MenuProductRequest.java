package kitchenpos.menu.presentation.dto;

public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProductRequest() {
    }

    private MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, long quantity) {
        return new MenuProductRequest(null, null, productId, quantity);
    }

    public Long getSeq() {
        return seq;
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
