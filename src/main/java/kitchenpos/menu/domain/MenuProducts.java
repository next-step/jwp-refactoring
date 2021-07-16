package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> values() {
        return Collections.unmodifiableList(this.menuProducts);
    }

    public void updateMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(menu);
        }
    }

    public BigDecimal getTotalPrice() {
        return menuProducts.stream().map(MenuProduct::menuPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
