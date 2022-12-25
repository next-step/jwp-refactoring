package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {}

    public MenuProductResponse(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menuProduct.getMenu().getId();
        this.productId = menuProduct.getProduct();
        this.quantity = menuProduct.getQuantity();
    }

    public static List<MenuProductResponse> list(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProductResponse::new)
                .collect(toList());
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
