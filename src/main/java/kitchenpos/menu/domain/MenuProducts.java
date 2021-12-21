package kitchenpos.menu.domain;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.OverMenuPriceException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {
    private static final int MIN_PRICE = 0;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private final List<MenuProduct> menuProducts;

    protected MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts ofEmpty() {
        return new MenuProducts();
    }
    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    private BigDecimal totalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::totalPrice)
                .reduce(BigDecimal::add)
                .orElseThrow(InvalidPriceException::new);
    }

    public void validateOverPrice(BigDecimal price) {
        if (price.compareTo(totalPrice()) > MIN_PRICE) {
            throw new OverMenuPriceException();
        }
    }

    public void initMenu(Menu menu) {
        validateOverPrice(menu.getPrice());
        menuProducts.forEach(menuProduct -> menuProduct.assignMenu(menu));
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
