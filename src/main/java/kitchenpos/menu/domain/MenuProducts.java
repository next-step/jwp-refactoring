package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.menu.dto.MenuProductResponse;

@Embeddable
public class MenuProducts {
    public MenuProducts() {}

    @OneToMany(mappedBy = "menu")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts(Menu menu, ProductsQuantities productsQuantities) {
        menuProducts.addAll(productsQuantities.toMenuProduct(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<MenuProductResponse> toResponse() {
        return menuProducts.stream().map(MenuProduct::toResponse).collect(Collectors.toList());
    }
}
