package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.product.Product;

public class MenuProductRequests {

    private List<MenuProductRequest> menuProducts;

    public MenuProductRequests() {
    }

    public MenuProductRequests(List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    public MenuProducts toMenuProducts(List<Product> products) {
        return menuProducts.stream()
            .map(menuProductRequest -> menuProductRequest.toMenuProduct(products))
            .collect(Collectors.collectingAndThen(Collectors.toList(), MenuProducts::of));
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
