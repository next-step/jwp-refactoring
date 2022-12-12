package kitchenpos.menu.domain;

import kitchenpos.price.domain.Amount;
import kitchenpos.price.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {}

    public void addAll(Menu menu, List<MenuProduct> menuProducts) {
        requireNonNull(menu, "menu");
        requireNonNull(menuProducts, "menuProducts");
        for (MenuProduct menuProduct : menuProducts) {
            add(menu, menuProduct);
        }
    }

    private void add(Menu menu, MenuProduct menuProduct) {
        if (!this.menuProducts.contains(menuProduct)) {
            menuProducts.add(menuProduct);
        }
        menuProduct.bindTo(menu);
    }

    public List<MenuProduct> get() {
        return Collections.unmodifiableList(menuProducts);
    }

    public Amount totalAmount() {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Amount.ZERO, Amount::add);

    }
}
