package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void addAll(Menu menu, List<MenuProduct> menuProducts) {
        requireNonNull(menu, "menu");
        requireNonNull(menuProducts, "menuProducts");
        for (MenuProduct menuProduct : menuProducts) {
            add(menu, menuProduct);
        }
    }

    private void add(Menu menu, MenuProduct menuProduct) {
        if (!this.menuProducts.contains(menuProduct)) {
            this.menuProducts.add(menuProduct);
        }
        menuProduct.bindTo(menu);
    }

    public List<MenuProduct> get() {
        return Collections.unmodifiableList(menuProducts);
    }
}
