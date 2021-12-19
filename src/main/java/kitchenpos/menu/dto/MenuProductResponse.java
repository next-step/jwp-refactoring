package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

    private Long seq;
    private Long quantity;
    private ProductResponse product;

    private MenuProductResponse(Long seq, Long quantity, ProductResponse product) {
        this.seq = seq;
        this.quantity = quantity;
        this.product = product;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(),
            menuProduct.getQuantity(),
            ProductResponse.of(menuProduct.getProduct()));
    }

    public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(it -> MenuProductResponse.of(it))
            .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
