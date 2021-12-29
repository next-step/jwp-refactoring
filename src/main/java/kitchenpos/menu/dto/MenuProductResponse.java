package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
        return menuProducts
                .stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
