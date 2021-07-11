package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;

public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Quantity quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long seq, Long menuId, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
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

    public Quantity getQuantity() {
        return quantity;
    }

}
