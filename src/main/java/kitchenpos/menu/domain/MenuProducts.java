package kitchenpos.menu.domain;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.OverMenuPriceException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private final List<MenuProduct> menuProducts;

    public MenuProducts() {
        menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
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
        if (price.compareTo(totalPrice()) > 0) {
            throw new OverMenuPriceException();
        }
    }

    public void initMenu(Menu menu) {
        menuProducts.forEach(menu::addMenuProduct);
    }
}
