package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public void add(final MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            this.add(menuProduct);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
