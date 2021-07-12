package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse productResponse;
    private Long quantity;

    public MenuProductResponse() {}

    private MenuProductResponse(Long seq, ProductResponse productResponse, Long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        Quantity quantity = menuProduct.getQuantity();
        ProductResponse response = ProductResponse.of(menuProduct.getProduct());
        return new MenuProductResponse(menuProduct.getSeq(), response, quantity.amount());
    }

    public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
