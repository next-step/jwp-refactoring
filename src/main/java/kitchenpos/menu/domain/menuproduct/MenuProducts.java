package kitchenpos.menu.domain.menuproduct;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.product.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(Menu menu, Product product, long quantity) {
        menuProducts.add(new MenuProduct(menu, product, quantity));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
