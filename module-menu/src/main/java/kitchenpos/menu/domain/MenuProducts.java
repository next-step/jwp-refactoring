package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() { }

    public MenuProducts(List<MenuProduct> menuProducts, Long menuId) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> get() {
        return menuProducts;
    }

}
