package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private ProductResponse productResponse;
    private int quantity;

    protected MenuProductResponse() {}

    private MenuProductResponse(Long seq, Long menuId, Product product, int quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productResponse = ProductResponse.from(product);
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
            menuProduct.getMenu().getId(),
            menuProduct.getProduct(),
            menuProduct.getQuantity().value());
    }

    public static List<MenuProductResponse> ofMenuProducts(MenuProducts menuProducts) {
        return menuProducts.value()
            .stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());
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

    public int getQuantity() {
        return quantity;
    }
}
