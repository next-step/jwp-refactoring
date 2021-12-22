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

    public void add(Menu menu, Product product, long quantity) {
        menuProducts.add(new MenuProduct(menu, product, quantity));
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
}
