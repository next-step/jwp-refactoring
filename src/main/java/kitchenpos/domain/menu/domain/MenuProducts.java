package kitchenpos.domain.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateSum() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.calculatePrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
