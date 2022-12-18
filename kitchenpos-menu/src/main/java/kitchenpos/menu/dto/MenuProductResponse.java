package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {}

    public MenuProductResponse(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menuProduct.getMenu().getId();
        this.productId = menuProduct.getProduct().getId();
        this.quantity = menuProduct.getQuantity();
    }

    private MenuProductResponse(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menuId = menu.getId();
        this.productId = product.getId();
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu(), menuProduct.getProduct(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> list(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProductResponse::new)
                .collect(Collectors.toList());
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
