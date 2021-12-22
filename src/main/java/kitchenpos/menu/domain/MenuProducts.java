package kitchenpos.menu.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Amount;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    protected MenuProducts() {
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public Amount sum() {
        return menuProducts.stream()
            .map(MenuProduct::multiply)
            .reduce(Amount.ZERO, Amount::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProducts that = (MenuProducts) o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
