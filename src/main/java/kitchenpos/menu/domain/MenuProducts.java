package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<BigDecimal> prices = menuProducts.stream()
                .map(menuProduct -> menuProduct.getTotalPrice().getPrice())
                .collect(Collectors.toList());
        return Price.sum(prices);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProducts)) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
