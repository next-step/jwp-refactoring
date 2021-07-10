package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.menu.domain.MenuProducts;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    protected MenuProductResponse() {
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

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> ofList(List<MenuProductEntity> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public static MenuProductResponse of(MenuProductEntity menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq()
                , menuProduct.getMenuId()
                , menuProduct.getProductId()
                , menuProduct.getQuantity());

    }
}
