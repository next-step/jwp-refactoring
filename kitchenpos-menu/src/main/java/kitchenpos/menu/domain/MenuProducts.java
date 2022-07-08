package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu, Price price) {
        setMenu(menuProducts, menu);
        this.menuProducts.addAll(menuProducts);
    }

    private void setMenu(List<MenuProduct> menuProducts, Menu menu) {
        menuProducts.forEach(it -> it.setMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
