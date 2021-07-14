package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.multiplyPrice();
            sum = sum.add(menuProduct.multiplyPrice());
        }
        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
