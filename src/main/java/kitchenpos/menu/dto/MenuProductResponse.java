package kitchenpos.menu.dto;

public class MenuProductResponse {
    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

}
