package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public MenuProducts mapMenu(Menu menu) {
        return menuProducts.stream()
            .map(menuProduct -> menuProduct.mapMenu(menu))
            .collect(Collectors.collectingAndThen(Collectors.toList(), MenuProducts::of));
    }

    public BigDecimal getSumPrice() {
        return menuProducts.stream()
            .map(MenuProduct::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
