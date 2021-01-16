package kitchenpos.menu.domain;

import kitchenpos.common.Money;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    private static final int MIN_PRODUCT_COUNT = 1;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<MenuProduct> menuProducts, final Menu menu) {
        if (menuProducts.size() < MIN_PRODUCT_COUNT) {
            throw new IllegalArgumentException(String.format("상품은 최소 %d개 이상이어야 합니다.", MIN_PRODUCT_COUNT));
        }

        this.menuProducts.addAll(menuProducts);
        assign(menu);
    }

    public void assign(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.assign(menu));
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
