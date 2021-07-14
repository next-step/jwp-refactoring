package kitchenpos.menu.domain.value;

import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menu.domain.entity.MenuProduct;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void toMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.toMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
