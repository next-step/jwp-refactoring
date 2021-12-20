package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> get() {
        return this.menuProducts;
    }

    public Price getTotalPrice() {
        BigDecimal sum = menuProducts.stream()
            .map(it -> it.getPrice().get())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Price.valueOf(sum);
    }

    protected void add(MenuProduct menuProduct) {
        if (!contains(menuProduct)) {
            menuProducts.add(menuProduct);
        }
    }

    protected void remove(MenuProduct menuProduct) {
        menuProduct.removeMenu();
        this.menuProducts.remove(menuProduct);
    }

    private boolean contains(MenuProduct menuProduct) {
        return menuProducts.stream()
            .anyMatch(it -> it.equalMenuProduct(menuProduct));
    }
}
