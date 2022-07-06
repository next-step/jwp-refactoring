package kitchenpos.domain.menu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    private static final int ZERO = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private Set<MenuProduct> menuProducts = new HashSet<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new HashSet<>(menuProducts);
    }

    public static MenuProducts createMenuProducts(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public void addAll(Menu menu, MenuProducts menuProducts) {
        for (MenuProduct menuProduct : menuProducts.getValue()) {
            addMenuProduct(menu, menuProduct);
        }
    }

    private void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.addMenu(menu);
    }

    public Set<MenuProduct> getValue() {
        return menuProducts;
    }
}
