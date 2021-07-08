package kitchenpos.ui.dto.menu;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

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

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductRequest> ofList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductRequest::of)
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

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(seq, menuId, productId, quantity);
    }

}
