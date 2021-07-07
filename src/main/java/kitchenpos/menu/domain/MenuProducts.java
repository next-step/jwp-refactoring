package kitchenpos.menu.domain;

import kitchenpos.common.Price;

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

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public boolean isEmpty() {
        return menuProducts.isEmpty();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Price calculateSumPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(Price.ZERO, Price::add);
    }
}