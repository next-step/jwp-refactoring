package kitchenpos.menu.domain;

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

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public Quantity getMenuProductQuantity(Long productId) {
        return menuProducts.stream()
                .filter(m -> Objects.equals(m.getProductId(), productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
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
