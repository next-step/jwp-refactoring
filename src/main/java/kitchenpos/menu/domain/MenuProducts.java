package kitchenpos.menu.domain;

import kitchenpos.common.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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
        int sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .mapToInt(Price::intValue)
                .sum();
        return Price.of(sum);
    }
}
