package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.menu.dto.MenuProductResponse;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private final List<MenuProduct> menuProducts;

    public MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProductResponse> toResponses() {
        return this.menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }
}
