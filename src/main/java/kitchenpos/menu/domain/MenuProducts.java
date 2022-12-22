package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<MenuProduct> menuProducts;

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = Collections.unmodifiableList(menuProducts);
    }

    protected MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> value() {
        return menuProducts;
    }

    public void updateMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }
}
