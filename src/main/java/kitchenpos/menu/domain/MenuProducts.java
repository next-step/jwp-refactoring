package kitchenpos.menu.domain;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.OverMenuPriceException;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    private static final int MIN_PRICE = 0;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
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

    private BigDecimal totalPrice(List<Product> products) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.totalPrice(products))
                .reduce(BigDecimal::add)
                .orElseThrow(InvalidPriceException::new);
    }

    public void validateOverPrice(BigDecimal price, List<Product> products) {
        if (price.compareTo(totalPrice(products)) > MIN_PRICE) {
            throw new OverMenuPriceException();
        }
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
