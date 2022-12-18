package kitchenpos.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final int ZERO = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @ReadOnlyProperty
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public void addMenuProduct(Menu menu, MenuProduct menuProduct) {
        if(hasMenuProduct(menuProduct)) {
            return;
        }
        this.menuProducts.add(menuProduct);
        menuProduct.updateMenu(menu);
    }

    private boolean hasMenuProduct(MenuProduct menuProduct) {
        return menuProducts.contains(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
