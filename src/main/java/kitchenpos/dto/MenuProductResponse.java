package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long menuId;
    private int quantity;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, Long productId, Long menuId, int quantity) {
        this.seq = seq;
        this.productId = productId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getMenuId(),
                (int) menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
