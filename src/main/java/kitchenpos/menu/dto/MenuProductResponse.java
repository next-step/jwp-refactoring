package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), toMenuIdFromMenuProduct(menuProduct.getMenu()), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofMenuProductResponses(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                            .map(MenuProductResponse::from)
                            .collect(Collectors.toList());
    }

    private static Long toMenuIdFromMenuProduct(Menu menu) {
        if (Objects.isNull(menu)) {
            return null;
        }
        return menu.getId();
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
