package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    protected MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
        validPrice(menu, menuProducts);

        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
        return new MenuProducts(menuProducts);
    }

    private static void validPrice(Menu menu, List<MenuProduct> menuProducts) {
        BigDecimal productAmount = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (menu.getPrice().biggerThan(productAmount)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
