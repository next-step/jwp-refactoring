package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void updateMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(menu);
        }
    }

    public List<MenuProduct> values() {
        return Collections.unmodifiableList(menuProducts);
    }

    public long getTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(Long::sum)
                .orElse(0L);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
