package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.dto.MenuProductResponse;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void makeRelations(final Menu menu, final List<MenuProduct> menuProducts) {
        menuProducts
                .stream()
                .forEach(menuProduct -> {
                    menuProduct.relateToMenu(menu);
                    this.menuProducts.add(menuProduct);
                });
    }

    public List<MenuProductResponse> getResponses() {
        return menuProducts.stream().map(MenuProductResponse::of).collect(Collectors.toList());
    }
}
