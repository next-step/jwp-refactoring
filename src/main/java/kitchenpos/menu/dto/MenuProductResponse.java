package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.ProductId;

public class MenuProductResponse {

    private Long productId;
    private Long quantity;

    public static List<MenuProductResponse> ofList(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
            .map(menuProduct -> of(menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }

    public static MenuProductResponse of(Long productId, Long quantity) {
        return new MenuProductResponse(productId, quantity);
    }

    private MenuProductResponse(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductResponse() {
    }

    public Long getName() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
