package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(Menu menu, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public BigDecimal sumOfProductsPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculateTotalPrice());
        }
        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
