package kitchenpos.menu.domain;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(Menu menu, Product product, Long quantity) {
        menuProducts.add(new MenuProduct(menu, product, quantity));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
