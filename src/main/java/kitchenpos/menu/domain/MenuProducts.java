package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuProducts empty() {
        return new MenuProducts();
    }

    public Price getTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getTotalPrice());
        }
        return Price.of(sum);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }
}
