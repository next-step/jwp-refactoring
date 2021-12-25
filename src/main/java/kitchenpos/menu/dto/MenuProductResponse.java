package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, ProductResponse productResponse, Long quantity) {
        this.seq = seq;
        this.product = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
        return menuProducts.asList()
                .stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
