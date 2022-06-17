package kitchenpos.domain;

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
    private List<MenuProductEntity> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public void addAll(MenuEntity menu, List<MenuProductEntity> menuProducts) {
        requireNonNull(menu, "menu");
        requireNonNull(menuProducts, "menuProducts");
        for (MenuProductEntity menuProduct : menuProducts) {
            add(menu, menuProduct);
        }
    }

    private void add(MenuEntity menu, MenuProductEntity menuProduct) {
        if (!this.menuProducts.contains(menuProduct)) {
            menuProducts.add(menuProduct);
        }
        menuProduct.bindTo(menu);
    }

    public List<MenuProductEntity> get() {
        return Collections.unmodifiableList(menuProducts);
    }
}
