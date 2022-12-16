package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public Price totalMenuPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    public List<MenuProduct> get() {
        return Collections.unmodifiableList(menuProducts);
    }

    void setMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }
}
