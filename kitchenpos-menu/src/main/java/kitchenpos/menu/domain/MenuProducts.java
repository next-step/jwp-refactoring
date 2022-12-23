package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public void setMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
    }

    public List<MenuProduct> getList() {
        return this.menuProducts;
    }
}
