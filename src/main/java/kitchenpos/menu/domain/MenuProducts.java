package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {

    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        addMenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void updateMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }
}
