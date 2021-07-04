package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.menuproduct.domain.MenuProduct;
import kitchenpos.menuproduct.dto.MenuProductResponse;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProductResponse> toResponse() {
        return menuProducts.stream().map(MenuProduct::toResponse).collect(Collectors.toList());
    }
}
