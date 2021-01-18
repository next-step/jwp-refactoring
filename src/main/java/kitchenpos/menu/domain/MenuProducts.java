package kitchenpos.menu.domain;

import kitchenpos.common.Money;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    private static final int MIN_PRODUCT_COUNT = 1;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts.size() < MIN_PRODUCT_COUNT) {
            throw new IllegalArgumentException(String.format("상품은 최소 %d개 이상이어야 합니다.", MIN_PRODUCT_COUNT));
        }

        this.menuProducts.addAll(menuProducts);
    }

    public Money price() {
        return menuProducts.stream()
                .map(MenuProduct::price)
                .reduce(Money.zero(), Money::add);
    }

    public int size() {
        return menuProducts.size();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
