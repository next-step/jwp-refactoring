package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

public class MenuProducts {
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts (List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public int size() {
        return menuProducts.size();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuProducts that = (MenuProducts) o;

        return menuProducts != null ? menuProducts.equals(that.menuProducts) : that.menuProducts == null;
    }

    @Override
    public int hashCode() {
        return menuProducts != null ? menuProducts.hashCode() : 0;
    }
}
