package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "Menu")
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
        this.menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getValue() {
        return menuProducts;
    }

    public void add(List<Product> products) {
    }
}
