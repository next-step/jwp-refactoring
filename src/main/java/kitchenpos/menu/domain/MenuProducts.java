package kitchenpos.menu.domain;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public void add(Long menuId, Product product, Long quantity) {
        menuProducts.add(new MenuProduct(menuId, product, quantity));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
