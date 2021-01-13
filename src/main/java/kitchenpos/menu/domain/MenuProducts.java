package kitchenpos.menu.domain;

import kitchenpos.common.Money;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        this.menuProducts.addAll(menuProducts);
        assign(menu);
    }

    public void assign(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.assign(menu));
    }

    public Money price() {
        return menuProducts.stream()
                .map(MenuProduct::price)
                .reduce(Money.zero(), Money::add);
    }

    public int size() {
        return menuProducts.size();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
