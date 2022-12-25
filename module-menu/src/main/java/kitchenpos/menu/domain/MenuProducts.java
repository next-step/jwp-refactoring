package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public boolean isEmpty() {
        return menuProducts.isEmpty();
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public void mapMenu(Menu menu) {
        for (MenuProduct menuProduct : this.menuProducts) {
            menuProduct.mapMenu(menu);
        }
    }
}
