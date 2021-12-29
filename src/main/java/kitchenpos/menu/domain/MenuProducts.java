package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.common.price.domain.Price;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price calculateSum() {
        return menuProducts.stream()
            .map(MenuProduct::calculateTotal)
            .reduce(Price.ZERO, Price::add);
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
