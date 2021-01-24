package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private ProductResponse productResponse;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long menuId, ProductResponse productResponse, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    private static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq()
                , menuProduct.getMenu().getId()
                , ProductResponse.of(menuProduct.getProduct())
                , menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
