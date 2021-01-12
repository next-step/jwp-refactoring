package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
        this.menuProducts = menuProducts;
    }

    public BigDecimal priceSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.priceForQuantity());
        }
        return sum;
    }

    public List<MenuProduct> list() {
        return this.menuProducts;
    }
}
