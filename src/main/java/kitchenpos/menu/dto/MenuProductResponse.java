package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {

    }
    public MenuProductResponse(long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> from(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map((MenuProductResponse::from))
                .collect(Collectors.toList());
    }


    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().value());
    }


    public long getSeq() {
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
