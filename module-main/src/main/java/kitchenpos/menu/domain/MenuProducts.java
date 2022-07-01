package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.bindMenu(menu));
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getValues() {
        return menuProducts;
    }
}
