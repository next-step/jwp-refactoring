package kitchenpos.menu.domain;

import kitchenpos.common.Price;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = ALL)
    private final List<MenuProduct> menuProducts;

    public MenuProducts() {
        this(new ArrayList<>());
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> values() {
        return menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public Price totalPrice() {
        Price sum = new Price();
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.plus(
                    menuProduct.getProduct()
                            .getPrice()
                            .multiply(menuProduct.getQuantity()));
        }
        return sum;
    }

    public void setMenu(Menu menu) {
        menuProducts.forEach(m -> m.setMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
