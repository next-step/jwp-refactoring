package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal menuProductsPrice() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.price(menuProduct.quantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> menuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
